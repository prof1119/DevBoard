package com.devboard.service;


import com.devboard.dto.AddMemberDTO;
import com.devboard.dto.BoardDetailDTO;
import com.devboard.dto.BoardResponseDTO;
import com.devboard.dto.CreateBoardDTO;
import com.devboard.dto.UpdateBoardDTO;
import com.devboard.entity.Board;
import com.devboard.entity.BoardColumn;
import com.devboard.entity.User;
import com.devboard.exception.AccessDeniedException;
import com.devboard.exception.BadRequestException;
import com.devboard.exception.ResourceNotFoundException;
import com.devboard.repository.BoardRepository;
import com.devboard.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * BoardService - خدمة إدارة الألواح
 * تتعامل مع إنشاء وتعديل وحذف الألواح
 */
@Service
@Transactional
@Slf4j
public class BoardService {
    
    @Autowired
    private BoardRepository boardRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private BoardMapper boardMapper;
    
    @Autowired
    private UserMapper userMapper;
    
    /**
     * إنشاء لوحة جديدة
     */
    public BoardResponseDTO createBoard(CreateBoardDTO createBoardDTO) {
        log.info("Creating new board: {}", createBoardDTO.getName());
        
        User owner = getCurrentUser();
        
        Board board = Board.builder()
                .name(createBoardDTO.getName())
                .description(createBoardDTO.getDescription())
                .backgroundImage(createBoardDTO.getBackgroundImage())
                .isPublic(createBoardDTO.getIsPublic() != null ? createBoardDTO.getIsPublic() : false)
                .owner(owner)
                .build();
        
        board.getMembers().add(owner);
        board = boardRepository.save(board);
        
        // إنشاء الأعمدة الافتراضية
        createDefaultColumns(board);
        
        log.info("Board created successfully: {}", board.getId());
        
        return boardMapper.toBoardResponseDTO(board);
    }
    
    /**
     * الحصول على لوحة معينة
     */
    public BoardResponseDTO getBoardById(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new ResourceNotFoundException("Board not found with id: " + boardId));
        
        User currentUser = getCurrentUser();
        verifyBoardAccess(board, currentUser);
        
        return boardMapper.toBoardResponseDTO(board);
    }
    
    /**
     * الحصول على تفاصيل شاملة للوحة
     */
    public BoardDetailDTO getBoardDetail(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new ResourceNotFoundException("Board not found with id: " + boardId));
        
        User currentUser = getCurrentUser();
        verifyBoardAccess(board, currentUser);
        
        return BoardDetailDTO.builder()
                .board(boardMapper.toBoardResponseDTO(board))
                .members(board.getMembers().stream()
                        .map(userMapper::toUserResponseDTO)
                        .collect(Collectors.toSet()))
                .build();
    }
    
    /**
     * تحديث لوحة
     */
    public BoardResponseDTO updateBoard(Long boardId, UpdateBoardDTO updateBoardDTO) {
        log.info("Updating board: {}", boardId);
        
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new ResourceNotFoundException("Board not found with id: " + boardId));
        
        User currentUser = getCurrentUser();
        if (!board.getOwner().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("Only board owner can update the board");
        }
        
        if (updateBoardDTO.getName() != null) {
            board.setName(updateBoardDTO.getName());
        }
        if (updateBoardDTO.getDescription() != null) {
            board.setDescription(updateBoardDTO.getDescription());
        }
        if (updateBoardDTO.getBackgroundImage() != null) {
            board.setBackgroundImage(updateBoardDTO.getBackgroundImage());
        }
        if (updateBoardDTO.getIsPublic() != null) {
            board.setIsPublic(updateBoardDTO.getIsPublic());
        }
        if (updateBoardDTO.getIsArchived() != null) {
            board.setIsArchived(updateBoardDTO.getIsArchived());
        }
        
        board = boardRepository.save(board);
        log.info("Board updated successfully: {}", board.getId());
        
        return boardMapper.toBoardResponseDTO(board);
    }
    
    /**
     * حذف لوحة
     */
    public void deleteBoard(Long boardId) {
        log.info("Deleting board: {}", boardId);
        
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new ResourceNotFoundException("Board not found with id: " + boardId));
        
        User currentUser = getCurrentUser();
        if (!board.getOwner().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("Only board owner can delete the board");
        }
        
        boardRepository.delete(board);
        log.info("Board deleted successfully: {}", boardId);
    }
    
    /**
     * الحصول على الألواح الخاصة بالمستخدم الحالي
     */
    public List<BoardResponseDTO> getUserBoards() {
        User user = getCurrentUser();
        List<Board> boards = boardRepository.findBoardsByMember(user.getId());
        
        return boards.stream()
                .map(boardMapper::toBoardResponseDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * إضافة عضو إلى اللوحة
     */
    public BoardResponseDTO addMemberToBoard(Long boardId, AddMemberDTO addMemberDTO) {
        log.info("Adding member to board: {}", boardId);
        
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new ResourceNotFoundException("Board not found with id: " + boardId));
        
        User currentUser = getCurrentUser();
        if (!board.getOwner().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("Only board owner can add members");
        }
        
        User member = userRepository.findById(Long.parseLong(addMemberDTO.getUserId()))
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        if (board.getMembers().contains(member)) {
            throw new BadRequestException("User is already a member of this board");
        }
        
        board.getMembers().add(member);
        board = boardRepository.save(board);
        
        log.info("Member added to board: {}", boardId);
        
        return boardMapper.toBoardResponseDTO(board);
    }
    
    /**
     * إزالة عضو من اللوحة
     */
    public BoardResponseDTO removeMemberFromBoard(Long boardId, Long userId) {
        log.info("Removing member from board: {}", boardId);
        
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new ResourceNotFoundException("Board not found with id: " + boardId));
        
        User currentUser = getCurrentUser();
        if (!board.getOwner().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("Only board owner can remove members");
        }
        
        User member = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        if (!board.getMembers().contains(member)) {
            throw new BadRequestException("User is not a member of this board");
        }
        
        board.getMembers().remove(member);
        board = boardRepository.save(board);
        
        log.info("Member removed from board: {}", boardId);
        
        return boardMapper.toBoardResponseDTO(board);
    }
    
    /**
     * إنشاء الأعمدة الافتراضية
     */
    private void createDefaultColumns(Board board) {
        String[] defaultColumns = {"Backlog", "In Progress", "Done"};
        for (int i = 0; i < defaultColumns.length; i++) {
            BoardColumn column = BoardColumn.builder()
                    .name(defaultColumns[i])
                    .position(i)
                    .board(board)
                    .build();
            board.getColumns().add(column);
        }
    }
    
    /**
     * التحقق من وصول المستخدم إلى اللوحة
     */
    private void verifyBoardAccess(Board board, User user) {
        if (!board.getIsPublic() && 
            !board.getOwner().getId().equals(user.getId()) &&
            !board.getMembers().contains(user)) {
            throw new AccessDeniedException("Access denied to this board");
        }
    }
    
    /**
     * الحصول على المستخدم الحالي
     */
    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Current user not found"));
    }
}
