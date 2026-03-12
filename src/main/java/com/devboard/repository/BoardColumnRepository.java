package com.devboard.repository;


import com.devboard.entity.Board;
import com.devboard.entity.BoardColumn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * BoardColumnRepository - يتعامل مع عمليات قاعدة البيانات لأعمدة اللوحة
 */
@Repository
public interface BoardColumnRepository extends JpaRepository<BoardColumn, Long> {
    
    /**
     * البحث عن جميع أعمدة لوحة معينة
     */
    List<BoardColumn> findByBoardOrderByPosition(Board board);
    
    /**
     * البحث عن أعمدة غير مؤرشفة
     */
    @Query("SELECT c FROM BoardColumn c WHERE c.board.id = :boardId AND c.isArchived = false ORDER BY c.position")
    List<BoardColumn> findActiveBoardColumns(@Param("boardId") Long boardId);
    
    /**
     * البحث عن عمود بالاسم والمجلس
     */
    Optional<BoardColumn> findByBoardIdAndName(Long boardId, String name);
    
    /**
     * عد الأعمدة في لوحة معينة
     */
    long countByBoard(Board board);
    
    /**
     * البحث عن الحد الأقصى للموضع في لوحة
     */
    @Query("SELECT MAX(c.position) FROM BoardColumn c WHERE c.board.id = :boardId")
    Optional<Integer> findMaxPositionByBoard(@Param("boardId") Long boardId);
    
    /**
     * حذف جميع أعمدة لوحة معينة
     */
    void deleteByBoard(Board board);
}
