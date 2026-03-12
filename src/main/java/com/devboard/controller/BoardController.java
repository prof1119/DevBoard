package com.devboard.controller;


import com.devboard.dto.*;
import com.devboard.service.BoardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * BoardController - متحكم الألواح
 */
@RestController
@RequestMapping("/api/boards")
@Tag(name = "Board Management", description = "Board management endpoints")
@SecurityRequirement(name = "Bearer Token")
@Slf4j
public class BoardController {
    
    @Autowired
    private BoardService boardService;
    
    /**
     * إنشاء لوحة جديدة
     */
    @PostMapping
    @Operation(summary = "Create new board")
    public ResponseEntity<BoardResponseDTO> createBoard(@Valid @RequestBody CreateBoardDTO createBoardDTO) {
        log.info("Creating new board: {}", createBoardDTO.getName());
        BoardResponseDTO board = boardService.createBoard(createBoardDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(board);
    }
    
    /**
     * الحصول على لوحة معينة
     */
    @GetMapping("/{boardId}")
    @Operation(summary = "Get board by ID")
    public ResponseEntity<BoardResponseDTO> getBoardById(@PathVariable Long boardId) {
        log.info("Getting board with ID: {}", boardId);
        BoardResponseDTO board = boardService.getBoardById(boardId);
        return ResponseEntity.ok(board);
    }
    
    /**
     * الحصول على تفاصيل شاملة للوحة
     */
    @GetMapping("/{boardId}/details")
    @Operation(summary = "Get detailed board information")
    public ResponseEntity<BoardDetailDTO> getBoardDetail(@PathVariable Long boardId) {
        log.info("Getting board details for ID: {}", boardId);
        BoardDetailDTO boardDetail = boardService.getBoardDetail(boardId);
        return ResponseEntity.ok(boardDetail);
    }
    
    /**
     * تحديث لوحة
     */
    @PutMapping("/{boardId}")
    @Operation(summary = "Update board")
    public ResponseEntity<BoardResponseDTO> updateBoard(
            @PathVariable Long boardId,
            @Valid @RequestBody UpdateBoardDTO updateBoardDTO) {
        log.info("Updating board with ID: {}", boardId);
        BoardResponseDTO board = boardService.updateBoard(boardId, updateBoardDTO);
        return ResponseEntity.ok(board);
    }
    
    /**
     * حذف لوحة
     */
    @DeleteMapping("/{boardId}")
    @Operation(summary = "Delete board")
    public ResponseEntity<Void> deleteBoard(@PathVariable Long boardId) {
        log.info("Deleting board with ID: {}", boardId);
        boardService.deleteBoard(boardId);
        return ResponseEntity.noContent().build();
    }
    
    /**
     * الحصول على ألواح المستخدم
     */
    @GetMapping
    @Operation(summary = "Get user's boards")
    public ResponseEntity<List<BoardResponseDTO>> getUserBoards() {
        log.info("Getting user boards");
        List<BoardResponseDTO> boards = boardService.getUserBoards();
        return ResponseEntity.ok(boards);
    }
    
    /**
     * إضافة عضو إلى اللوحة
     */
    @PostMapping("/{boardId}/members")
    @Operation(summary = "Add member to board")
    public ResponseEntity<BoardResponseDTO> addMemberToBoard(
            @PathVariable Long boardId,
            @Valid @RequestBody AddMemberDTO addMemberDTO) {
        log.info("Adding member to board: {}", boardId);
        BoardResponseDTO board = boardService.addMemberToBoard(boardId, addMemberDTO);
        return ResponseEntity.ok(board);
    }
    
    /**
     * إزالة عضو من اللوحة
     */
    @DeleteMapping("/{boardId}/members/{userId}")
    @Operation(summary = "Remove member from board")
    public ResponseEntity<BoardResponseDTO> removeMemberFromBoard(
            @PathVariable Long boardId,
            @PathVariable Long userId) {
        log.info("Removing member from board: {}", boardId);
        BoardResponseDTO board = boardService.removeMemberFromBoard(boardId, userId);
        return ResponseEntity.ok(board);
    }
}
