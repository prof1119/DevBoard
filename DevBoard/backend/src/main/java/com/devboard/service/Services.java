package com.devboard.service;

import com.devboard.dto.*;
import com.devboard.entity.*;
import com.devboard.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

// ==================== TASK SERVICE ====================
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class TaskService {

    private final TaskRepository taskRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final BoardColumnRepository columnRepository;
    private final LabelRepository labelRepository;
    private final TaskActivityLogRepository activityLogRepository;
    private final ModelMapper modelMapper;

    /**
     * Create a new task in a board column
     */
    public TaskDto createTask(Long boardId, TaskCreateRequest request, Long userId) {
        Board board = boardRepository.findById(boardId)
            .orElseThrow(() -> new RuntimeException("Board not found"));

        User creator = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));

        BoardColumn column = columnRepository.findById(request.getColumnId())
            .orElseThrow(() -> new RuntimeException("Column not found"));

        // Validate column belongs to board
        if (!column.getBoard().getId().equals(boardId)) {
            throw new RuntimeException("Column does not belong to this board");
        }

        // Get max position in column
        Integer maxPosition = taskRepository.findMaxPositionByColumnId(request.getColumnId());
        int newPosition = (maxPosition != null ? maxPosition : -1) + 1;

        Task task = Task.builder()
            .board(board)
            .column(column)
            .title(request.getTitle())
            .description(request.getDescription())
            .priority(Task.Priority.valueOf(request.getPriority()))
            .status(Task.TaskStatus.TODO)
            .createdBy(creator)
            .position(newPosition)
            .dueDate(request.getDueDate() != null ? java.time.LocalDate.parse(request.getDueDate()) : null)
            .build();

        if (request.getAssignedToId() != null) {
            User assignee = userRepository.findById(request.getAssignedToId())
                .orElseThrow(() -> new RuntimeException("Assignee not found"));
            task.setAssignedTo(assignee);
        }

        task = taskRepository.save(task);

        // Log activity
        TaskActivityLog log = TaskActivityLog.builder()
            .task(task)
            .user(creator)
            .action("CREATED")
            .activityType(TaskActivityLog.ActivityType.CREATED)
            .build();
        activityLogRepository.save(log);

        return mapTaskToDto(task);
    }

    /**
     * Update an existing task
     */
    public TaskDto updateTask(Long taskId, TaskUpdateRequest request) {
        Task task = taskRepository.findById(taskId)
            .orElseThrow(() -> new RuntimeException("Task not found"));

        if (request.getTitle() != null) {
            task.setTitle(request.getTitle());
        }
        if (request.getDescription() != null) {
            task.setDescription(request.getDescription());
        }
        if (request.getPriority() != null) {
            task.setPriority(Task.Priority.valueOf(request.getPriority()));
        }
        if (request.getStatus() != null && task.canTransitionTo(Task.TaskStatus.valueOf(request.getStatus()))) {
            task.setStatus(Task.TaskStatus.valueOf(request.getStatus()));
        }
        if (request.getAssignedToId() != null) {
            User assignee = userRepository.findById(request.getAssignedToId())
                .orElseThrow(() -> new RuntimeException("Assignee not found"));
            task.setAssignedTo(assignee);
        }
        if (request.getDueDate() != null) {
            task.setDueDate(java.time.LocalDate.parse(request.getDueDate()));
        }

        task = taskRepository.save(task);

        // Log activity
        logTaskActivity(task, "UPDATED", null, null);

        return mapTaskToDto(task);
    }

    /**
     * Move task to a different column
     */
    public TaskDto moveTask(MoveTaskRequest request) {
        Task task = taskRepository.findById(request.getTaskId())
            .orElseThrow(() -> new RuntimeException("Task not found"));

        BoardColumn targetColumn = columnRepository.findById(request.getTargetColumnId())
            .orElseThrow(() -> new RuntimeException("Target column not found"));

        BoardColumn oldColumn = task.getColumn();
        task.setColumn(targetColumn);
        task.setPosition(request.getNewPosition());

        task = taskRepository.save(task);

        // Log activity
        logTaskActivity(task, "MOVED", 
            oldColumn != null ? oldColumn.getName() : "None", 
            targetColumn.getName());

        return mapTaskToDto(task);
    }

    /**
     * Get task by ID with full details
     */
    @Transactional(readOnly = true)
    public TaskDto getTaskById(Long taskId) {
        Task task = taskRepository.findById(taskId)
            .orElseThrow(() -> new RuntimeException("Task not found"));
        return mapTaskToDto(task);
    }

    /**
     * Get all tasks in a board
     */
    @Transactional(readOnly = true)
    public List<TaskDto> getTasksByBoard(Long boardId) {
        return taskRepository.findByBoardId(boardId).stream()
            .map(this::mapTaskToDto)
            .collect(Collectors.toList());
    }

    /**
     * Get tasks by column
     */
    @Transactional(readOnly = true)
    public List<TaskDto> getTasksByColumn(Long columnId) {
        return taskRepository.findByColumnIdOrderByPosition(columnId).stream()
            .map(this::mapTaskToDto)
            .collect(Collectors.toList());
    }

    /**
     * Delete a task
     */
    public void deleteTask(Long taskId) {
        Task task = taskRepository.findById(taskId)
            .orElseThrow(() -> new RuntimeException("Task not found"));
        
        taskRepository.delete(task);
        log.info("Task deleted: {}", taskId);
    }

    /**
     * Add label to task
     */
    public TaskDto addLabelToTask(Long taskId, Long labelId) {
        Task task = taskRepository.findById(taskId)
            .orElseThrow(() -> new RuntimeException("Task not found"));

        Label label = labelRepository.findById(labelId)
            .orElseThrow(() -> new RuntimeException("Label not found"));

        task.addLabel(label);
        task = taskRepository.save(task);

        return mapTaskToDto(task);
    }

    /**
     * Remove label from task
     */
    public TaskDto removeLabelFromTask(Long taskId, Long labelId) {
        Task task = taskRepository.findById(taskId)
            .orElseThrow(() -> new RuntimeException("Task not found"));

        Label label = labelRepository.findById(labelId)
            .orElseThrow(() -> new RuntimeException("Label not found"));

        task.removeLabel(label);
        task = taskRepository.save(task);

        return mapTaskToDto(task);
    }

    /**
     * Map Task entity to TaskDto
     */
    private TaskDto mapTaskToDto(Task task) {
        TaskDto dto = modelMapper.map(task, TaskDto.class);
        
        // Set additional fields
        if (task.getAssignedTo() != null) {
            dto.setAssignedTo(modelMapper.map(task.getAssignedTo(), UserDto.class));
        }
        if (task.getCreatedBy() != null) {
            dto.setCreatedBy(modelMapper.map(task.getCreatedBy(), UserDto.class));
        }
        
        dto.setLabels(task.getLabels().stream()
            .map(Label::getName)
            .collect(Collectors.toList()));
        
        dto.setCommentCount(task.getComments().size());
        
        return dto;
    }

    /**
     * Log task activity
     */
    private void logTaskActivity(Task task, String action, String oldValue, String newValue) {
        TaskActivityLog log = TaskActivityLog.builder()
            .task(task)
            .action(action)
            .oldValue(oldValue)
            .newValue(newValue)
            .activityType(TaskActivityLog.ActivityType.UPDATED)
            .build();
        activityLogRepository.save(log);
    }
}

// ==================== BOARD SERVICE ====================
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class BoardService {

    private final BoardRepository boardRepository;
    private final BoardColumnRepository columnRepository;
    private final BoardMemberRepository memberRepository;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final LabelRepository labelRepository;
    private final ModelMapper modelMapper;

    /**
     * Create a new board
     */
    public BoardDto createBoard(BoardCreateRequest request, Long userId) {
        User owner = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));

        Board board = Board.builder()
            .name(request.getName())
            .description(request.getDescription())
            .owner(owner)
            .isPublic(request.getIsPublic() != null ? request.getIsPublic() : false)
            .githubRepoOwner(request.getGithubRepoOwner())
            .githubRepoName(request.getGithubRepoName())
            .build();

        board = boardRepository.save(board);

        // Add owner as member
        BoardMember ownerMember = BoardMember.builder()
            .board(board)
            .user(owner)
            .role(BoardMember.BoardRole.OWNER)
            .build();
        memberRepository.save(ownerMember);

        // Create default columns
        createDefaultColumns(board);

        return mapBoardToDto(board);
    }

    /**
     * Get board by ID with all details
     */
    @Transactional(readOnly = true)
    public BoardDto getBoardById(Long boardId) {
        Board board = boardRepository.findById(boardId)
            .orElseThrow(() -> new RuntimeException("Board not found"));
        return mapBoardToDto(board);
    }

    /**
     * Get all boards for a user
     */
    @Transactional(readOnly = true)
    public List<BoardDto> getBoardsByUser(Long userId) {
        List<Board> boards = boardRepository.findByOwnerId(userId);
        return boards.stream()
            .map(this::mapBoardToDto)
            .collect(Collectors.toList());
    }

    /**
     * Update board
     */
    public BoardDto updateBoard(Long boardId, BoardUpdateRequest request) {
        Board board = boardRepository.findById(boardId)
            .orElseThrow(() -> new RuntimeException("Board not found"));

        if (request.getName() != null) {
            board.setName(request.getName());
        }
        if (request.getDescription() != null) {
            board.setDescription(request.getDescription());
        }
        if (request.getIsPublic() != null) {
            board.setIsPublic(request.getIsPublic());
        }

        board = boardRepository.save(board);
        return mapBoardToDto(board);
    }

    /**
     * Delete board
     */
    public void deleteBoard(Long boardId) {
        Board board = boardRepository.findById(boardId)
            .orElseThrow(() -> new RuntimeException("Board not found"));
        boardRepository.delete(board);
        log.info("Board deleted: {}", boardId);
    }

    /**
     * Create default columns for new board
     */
    private void createDefaultColumns(Board board) {
        String[] defaultColumns = {"Todo", "In Progress", "In Review", "Done"};
        String[] colors = {"#6f42c1", "#fd7e14", "#ffc107", "#28a745"};

        for (int i = 0; i < defaultColumns.length; i++) {
            BoardColumn column = BoardColumn.builder()
                .board(board)
                .name(defaultColumns[i])
                .position(i)
                .color(colors[i])
                .build();
            columnRepository.save(column);
        }
    }

    /**
     * Map Board entity to BoardDto
     */
    private BoardDto mapBoardToDto(Board board) {
        BoardDto dto = modelMapper.map(board, BoardDto.class);
        
        // Map columns with tasks
        dto.setColumns(board.getColumns().stream()
            .map(col -> BoardColumnDto.builder()
                .id(col.getId())
                .name(col.getName())
                .position(col.getPosition())
                .color(col.getColor())
                .taskCount(col.getTaskCount())
                .tasks(col.getTasks().stream()
                    .map(task -> modelMapper.map(task, TaskDto.class))
                    .collect(Collectors.toList()))
                .build())
            .collect(Collectors.toList()));
        
        // Map members
        dto.setMembers(board.getMembers().stream()
            .map(member -> BoardMemberDto.builder()
                .id(member.getId())
                .user(modelMapper.map(member.getUser(), UserDto.class))
                .role(member.getRole().name())
                .addedAt(member.getAddedAt())
                .build())
            .collect(Collectors.toList()));

        return dto;
    }
}

// ==================== LABEL SERVICE ====================
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class LabelService {

    private final LabelRepository labelRepository;
    private final BoardRepository boardRepository;
    private final ModelMapper modelMapper;

    /**
     * Create a new label
     */
    public LabelDto createLabel(Long boardId, LabelCreateRequest request) {
        Board board = boardRepository.findById(boardId)
            .orElseThrow(() -> new RuntimeException("Board not found"));

        Label label = Label.builder()
            .board(board)
            .name(request.getName())
            .color(request.getColor() != null ? request.getColor() : "#0366d6")
            .build();

        label = labelRepository.save(label);
        return modelMapper.map(label, LabelDto.class);
    }

    /**
     * Get all labels for a board
     */
    @Transactional(readOnly = true)
    public List<LabelDto> getLabelsByBoard(Long boardId) {
        return labelRepository.findByBoardId(boardId).stream()
            .map(label -> modelMapper.map(label, LabelDto.class))
            .collect(Collectors.toList());
    }

    /**
     * Delete a label
     */
    public void deleteLabel(Long labelId) {
        Label label = labelRepository.findById(labelId)
            .orElseThrow(() -> new RuntimeException("Label not found"));
        labelRepository.delete(label);
        log.info("Label deleted: {}", labelId);
    }
}
