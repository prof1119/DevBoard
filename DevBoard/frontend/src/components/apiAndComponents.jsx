// src/services/api.js
import axios from 'axios';

const API_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080/api';

const api = axios.create({
  baseURL: API_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

/**
 * Add token to request headers
 */
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

/**
 * Handle response errors
 */
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      // Token expired or invalid
      localStorage.removeItem('token');
      window.location.href = '/login';
    }
    return Promise.reject(error.response?.data?.message || error.message);
  }
);

export default api;

// ============================================================
// src/components/KanbanBoard.jsx
// ============================================================

import React, { useEffect, useState } from 'react';
import { DragDropContext, Droppable, Draggable } from 'react-beautiful-dnd';
import { useBoardStore } from '../store/boardStore';
import TaskCard from './TaskCard';
import TaskModal from './TaskModal';
import { toast } from 'react-toastify';
import { FiPlus, FiLoader } from 'react-icons/fi';

const KanbanBoard = ({ boardId }) => {
  const {
    currentBoard,
    columns,
    tasks,
    loading,
    fetchBoardById,
    moveTask,
    createTask,
  } = useBoardStore();

  const [showNewTaskModal, setShowNewTaskModal] = useState(false);
  const [selectedColumnId, setSelectedColumnId] = useState(null);

  useEffect(() => {
    fetchBoardById(boardId);
  }, [boardId, fetchBoardById]);

  /**
   * Handle drag and drop
   */
  const handleDragEnd = async (result) => {
    const { source, destination, draggableId } = result;

    if (!destination) return;

    if (
      source.droppableId === destination.droppableId &&
      source.index === destination.index
    ) {
      return; // No change
    }

    try {
      await moveTask({
        taskId: parseInt(draggableId),
        targetColumnId: parseInt(destination.droppableId),
        newPosition: destination.index,
      });
      toast.success('Task moved successfully!');
    } catch (error) {
      toast.error('Failed to move task');
    }
  };

  /**
   * Open new task modal
   */
  const handleAddTask = (columnId) => {
    setSelectedColumnId(columnId);
    setShowNewTaskModal(true);
  };

  /**
   * Handle new task creation
   */
  const handleCreateTask = async (taskData) => {
    try {
      await createTask(boardId, {
        ...taskData,
        columnId: selectedColumnId,
      });
      toast.success('Task created successfully!');
      setShowNewTaskModal(false);
    } catch (error) {
      toast.error('Failed to create task');
    }
  };

  if (loading && !currentBoard) {
    return (
      <div className="flex items-center justify-center h-screen">
        <FiLoader className="animate-spin text-3xl text-blue-500" />
      </div>
    );
  }

  if (!currentBoard) {
    return <div className="p-4 text-red-500">Board not found</div>;
  }

  return (
    <div className="p-6 bg-gray-100 min-h-screen">
      <div className="mb-6">
        <h1 className="text-3xl font-bold text-gray-800">{currentBoard.name}</h1>
        {currentBoard.description && (
          <p className="text-gray-600 mt-2">{currentBoard.description}</p>
        )}
      </div>

      <DragDropContext onDragEnd={handleDragEnd}>
        <div className="flex gap-6 overflow-x-auto pb-4">
          {columns.map((column) => (
            <Droppable key={column.id} droppableId={column.id.toString()}>
              {(provided, snapshot) => (
                <div
                  ref={provided.innerRef}
                  {...provided.droppableProps}
                  className={`flex-shrink-0 w-80 bg-gray-200 rounded-lg p-4 ${
                    snapshot.isDraggingOver ? 'bg-blue-100' : ''
                  }`}
                >
                  {/* Column Header */}
                  <div className="flex items-center justify-between mb-4">
                    <div className="flex items-center gap-2">
                      <div
                        className="w-3 h-3 rounded-full"
                        style={{ backgroundColor: column.color }}
                      />
                      <h2 className="font-semibold text-gray-800">
                        {column.name}
                      </h2>
                      <span className="text-sm text-gray-600 bg-white px-2 py-1 rounded">
                        {column.taskCount || 0}
                      </span>
                    </div>
                  </div>

                  {/* Tasks */}
                  <div className="space-y-3">
                    {tasks
                      .filter((task) => task.columnId === column.id)
                      .map((task, index) => (
                        <Draggable
                          key={task.id}
                          draggableId={task.id.toString()}
                          index={index}
                        >
                          {(provided, snapshot) => (
                            <div
                              ref={provided.innerRef}
                              {...provided.draggableProps}
                              {...provided.dragHandleProps}
                              className={`${
                                snapshot.isDragging
                                  ? 'opacity-50 bg-blue-200'
                                  : ''
                              }`}
                            >
                              <TaskCard task={task} />
                            </div>
                          )}
                        </Draggable>
                      ))}
                    {provided.placeholder}
                  </div>

                  {/* Add Task Button */}
                  <button
                    onClick={() => handleAddTask(column.id)}
                    className="w-full mt-4 p-2 bg-white hover:bg-gray-300 text-gray-700 rounded-lg flex items-center justify-center gap-2 transition"
                  >
                    <FiPlus /> Add Task
                  </button>
                </div>
              )}
            </Droppable>
          ))}
        </div>
      </DragDropContext>

      {/* New Task Modal */}
      {showNewTaskModal && (
        <TaskModal
          isOpen={showNewTaskModal}
          onClose={() => setShowNewTaskModal(false)}
          onSubmit={handleCreateTask}
          isNew={true}
        />
      )}
    </div>
  );
};

export default KanbanBoard;

// ============================================================
// src/components/TaskCard.jsx
// ============================================================

import React from 'react';
import { FiUser, FiCalendar, FiGithub } from 'react-icons/fi';
import classNames from 'classnames';

const TaskCard = ({ task, onClick }) => {
  const priorityColors = {
    LOW: 'bg-blue-100 text-blue-800',
    MEDIUM: 'bg-yellow-100 text-yellow-800',
    HIGH: 'bg-orange-100 text-orange-800',
    CRITICAL: 'bg-red-100 text-red-800',
  };

  return (
    <div
      onClick={onClick}
      className="bg-white rounded-lg shadow-sm hover:shadow-md p-3 cursor-pointer transition"
    >
      {/* Title */}
      <h3 className="font-semibold text-gray-800 mb-2 line-clamp-2">
        {task.title}
      </h3>

      {/* Priority Badge */}
      <div className="mb-2">
        <span
          className={classNames(
            'text-xs font-semibold px-2 py-1 rounded',
            priorityColors[task.priority] || priorityColors.MEDIUM
          )}
        >
          {task.priority}
        </span>
      </div>

      {/* Description Preview */}
      {task.description && (
        <p className="text-sm text-gray-600 mb-3 line-clamp-2">
          {task.description}
        </p>
      )}

      {/* Labels */}
      {task.labels && task.labels.length > 0 && (
        <div className="flex flex-wrap gap-1 mb-3">
          {task.labels.slice(0, 3).map((label) => (
            <span
              key={label}
              className="text-xs bg-gray-200 text-gray-700 px-2 py-1 rounded"
            >
              {label}
            </span>
          ))}
          {task.labels.length > 3 && (
            <span className="text-xs text-gray-600">
              +{task.labels.length - 3}
            </span>
          )}
        </div>
      )}

      {/* Footer Info */}
      <div className="flex items-center justify-between text-xs text-gray-500">
        <div className="flex items-center gap-2">
          {task.assignedTo && (
            <div className="flex items-center gap-1">
              <FiUser size={12} />
              <span>{task.assignedTo.fullName || task.assignedTo.username}</span>
            </div>
          )}
        </div>

        {/* GitHub Link */}
        {task.githubIssueUrl && (
          <a
            href={task.githubIssueUrl}
            target="_blank"
            rel="noopener noreferrer"
            className="text-blue-600 hover:text-blue-800"
            onClick={(e) => e.stopPropagation()}
          >
            <FiGithub size={14} />
          </a>
        )}
      </div>

      {/* Due Date */}
      {task.dueDate && (
        <div className="flex items-center gap-1 text-xs text-gray-500 mt-2">
          <FiCalendar size={12} />
          <span>{new Date(task.dueDate).toLocaleDateString()}</span>
        </div>
      )}
    </div>
  );
};

export default TaskCard;
