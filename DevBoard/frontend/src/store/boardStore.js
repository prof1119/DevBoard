// src/store/boardStore.js
import create from 'zustand';
import api from '../services/api';

export const useBoardStore = create((set, get) => ({
  // State
  boards: [],
  currentBoard: null,
  tasks: [],
  columns: [],
  members: [],
  labels: [],
  loading: false,
  error: null,

  // Actions
  setLoading: (loading) => set({ loading }),
  setError: (error) => set({ error }),

  /**
   * Fetch all boards for current user
   */
  fetchBoards: async () => {
    set({ loading: true });
    try {
      const response = await api.get('/boards');
      set({ boards: response.data.data, error: null });
    } catch (error) {
      set({ error: error.message });
    } finally {
      set({ loading: false });
    }
  },

  /**
   * Fetch single board by ID
   */
  fetchBoardById: async (boardId) => {
    set({ loading: true });
    try {
      const response = await api.get(`/boards/${boardId}`);
      const board = response.data.data;
      set({ 
        currentBoard: board,
        columns: board.columns || [],
        tasks: board.columns?.flatMap(col => col.tasks || []) || [],
        members: board.members || [],
        error: null 
      });
      return board;
    } catch (error) {
      set({ error: error.message });
    } finally {
      set({ loading: false });
    }
  },

  /**
   * Create new board
   */
  createBoard: async (boardData) => {
    set({ loading: true });
    try {
      const response = await api.post('/boards', boardData);
      const newBoard = response.data.data;
      set(state => ({
        boards: [...state.boards, newBoard],
        error: null
      }));
      return newBoard;
    } catch (error) {
      set({ error: error.message });
    } finally {
      set({ loading: false });
    }
  },

  /**
   * Update board
   */
  updateBoard: async (boardId, boardData) => {
    set({ loading: true });
    try {
      const response = await api.put(`/boards/${boardId}`, boardData);
      const updatedBoard = response.data.data;
      set(state => ({
        boards: state.boards.map(b => b.id === boardId ? updatedBoard : b),
        currentBoard: state.currentBoard?.id === boardId ? updatedBoard : state.currentBoard,
        error: null
      }));
      return updatedBoard;
    } catch (error) {
      set({ error: error.message });
    } finally {
      set({ loading: false });
    }
  },

  /**
   * Delete board
   */
  deleteBoard: async (boardId) => {
    set({ loading: true });
    try {
      await api.delete(`/boards/${boardId}`);
      set(state => ({
        boards: state.boards.filter(b => b.id !== boardId),
        currentBoard: state.currentBoard?.id === boardId ? null : state.currentBoard,
        error: null
      }));
    } catch (error) {
      set({ error: error.message });
    } finally {
      set({ loading: false });
    }
  },

  /**
   * Create new task
   */
  createTask: async (boardId, taskData) => {
    set({ loading: true });
    try {
      const response = await api.post(`/boards/${boardId}/tasks`, taskData);
      const newTask = response.data.data;
      set(state => ({
        tasks: [...state.tasks, newTask],
        error: null
      }));
      return newTask;
    } catch (error) {
      set({ error: error.message });
    } finally {
      set({ loading: false });
    }
  },

  /**
   * Update task
   */
  updateTask: async (taskId, taskData) => {
    set({ loading: true });
    try {
      const response = await api.put(`/tasks/${taskId}`, taskData);
      const updatedTask = response.data.data;
      set(state => ({
        tasks: state.tasks.map(t => t.id === taskId ? updatedTask : t),
        error: null
      }));
      return updatedTask;
    } catch (error) {
      set({ error: error.message });
    } finally {
      set({ loading: false });
    }
  },

  /**
   * Move task to different column
   */
  moveTask: async (moveData) => {
    try {
      const response = await api.patch('/tasks/move', moveData);
      const movedTask = response.data.data;
      set(state => ({
        tasks: state.tasks.map(t => t.id === moveData.taskId ? movedTask : t),
        error: null
      }));
      return movedTask;
    } catch (error) {
      set({ error: error.message });
    }
  },

  /**
   * Delete task
   */
  deleteTask: async (taskId) => {
    set({ loading: true });
    try {
      await api.delete(`/tasks/${taskId}`);
      set(state => ({
        tasks: state.tasks.filter(t => t.id !== taskId),
        error: null
      }));
    } catch (error) {
      set({ error: error.message });
    } finally {
      set({ loading: false });
    }
  },

  /**
   * Fetch all labels for board
   */
  fetchLabels: async (boardId) => {
    try {
      const response = await api.get(`/boards/${boardId}/labels`);
      set({ labels: response.data.data, error: null });
    } catch (error) {
      set({ error: error.message });
    }
  },

  /**
   * Create label
   */
  createLabel: async (boardId, labelData) => {
    set({ loading: true });
    try {
      const response = await api.post(`/boards/${boardId}/labels`, labelData);
      const newLabel = response.data.data;
      set(state => ({
        labels: [...state.labels, newLabel],
        error: null
      }));
      return newLabel;
    } catch (error) {
      set({ error: error.message });
    } finally {
      set({ loading: false });
    }
  },

  /**
   * Delete label
   */
  deleteLabel: async (labelId) => {
    try {
      await api.delete(`/labels/${labelId}`);
      set(state => ({
        labels: state.labels.filter(l => l.id !== labelId),
        error: null
      }));
    } catch (error) {
      set({ error: error.message });
    }
  },

  /**
   * Add member to board
   */
  addBoardMember: async (boardId, memberData) => {
    set({ loading: true });
    try {
      const response = await api.post(`/boards/${boardId}/members`, memberData);
      const newMember = response.data.data;
      set(state => ({
        members: [...state.members, newMember],
        error: null
      }));
      return newMember;
    } catch (error) {
      set({ error: error.message });
    } finally {
      set({ loading: false });
    }
  },

  /**
   * Remove member from board
   */
  removeBoardMember: async (boardId, memberId) => {
    try {
      await api.delete(`/boards/${boardId}/members/${memberId}`);
      set(state => ({
        members: state.members.filter(m => m.id !== memberId),
        error: null
      }));
    } catch (error) {
      set({ error: error.message });
    }
  },

  /**
   * Clear errors
   */
  clearError: () => set({ error: null }),
}));

/**
 * Auth Store
 */
export const useAuthStore = create((set) => ({
  // State
  user: null,
  token: localStorage.getItem('token'),
  isAuthenticated: !!localStorage.getItem('token'),
  loading: false,
  error: null,

  // Actions
  setLoading: (loading) => set({ loading }),
  setError: (error) => set({ error }),

  /**
   * Register new user
   */
  register: async (userData) => {
    set({ loading: true });
    try {
      const response = await api.post('/auth/register', userData);
      const { accessToken, user } = response.data.data;
      localStorage.setItem('token', accessToken);
      set({
        user,
        token: accessToken,
        isAuthenticated: true,
        error: null
      });
      return { user, token: accessToken };
    } catch (error) {
      set({ error: error.message });
    } finally {
      set({ loading: false });
    }
  },

  /**
   * Login user
   */
  login: async (credentials) => {
    set({ loading: true });
    try {
      const response = await api.post('/auth/login', credentials);
      const { accessToken, user } = response.data.data;
      localStorage.setItem('token', accessToken);
      set({
        user,
        token: accessToken,
        isAuthenticated: true,
        error: null
      });
      return { user, token: accessToken };
    } catch (error) {
      set({ error: error.message });
    } finally {
      set({ loading: false });
    }
  },

  /**
   * Logout user
   */
  logout: () => {
    localStorage.removeItem('token');
    set({
      user: null,
      token: null,
      isAuthenticated: false
    });
  },

  /**
   * Get current user
   */
  getCurrentUser: async () => {
    set({ loading: true });
    try {
      const response = await api.get('/auth/me');
      const user = response.data.data;
      set({ user, error: null });
      return user;
    } catch (error) {
      set({ error: error.message });
      // Token might be expired
      localStorage.removeItem('token');
      set({ isAuthenticated: false });
    } finally {
      set({ loading: false });
    }
  },

  /**
   * Clear errors
   */
  clearError: () => set({ error: null }),
}));
