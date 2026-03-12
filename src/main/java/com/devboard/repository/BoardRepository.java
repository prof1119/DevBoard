package com.devboard.repository;


import com.devboard.entity.Board;
import com.devboard.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * BoardRepository - يتعامل مع عمليات قاعدة البيانات للألواح
 */
@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    
    /**
     * البحث عن الألواح المملوكة لمستخدم معين
     */
    List<Board> findByOwnerOrderByCreatedAtDesc(User owner);
    
    /**
     * البحث عن الألواح التي يشارك فيها مستخدم معين
     */
    @Query("SELECT b FROM Board b JOIN b.members m WHERE m.id = :userId AND b.isArchived = false ORDER BY b.updatedAt DESC")
    List<Board> findBoardsByMember(@Param("userId") Long userId);
    
    /**
     * البحث عن الألواح العامة
     */
    @Query("SELECT b FROM Board b WHERE b.isPublic = true AND b.isArchived = false ORDER BY b.createdAt DESC")
    List<Board> findPublicBoards();
    
    /**
     * البحث عن لوحة معينة مع التحقق من الوصول
     */
    @Query("SELECT b FROM Board b WHERE b.id = :boardId AND " +
           "(b.owner.id = :userId OR (SELECT COUNT(m) FROM b.members m WHERE m.id = :userId) > 0 OR b.isPublic = true)")
    Optional<Board> findByIdWithAccess(@Param("boardId") Long boardId, @Param("userId") Long userId);
    
    /**
     * البحث عن الألواح بناءً على الاسم
     */
    @Query("SELECT b FROM Board b WHERE LOWER(b.name) LIKE LOWER(CONCAT('%', :search, '%')) AND b.isArchived = false ORDER BY b.name")
    List<Board> searchBoards(@Param("search") String search);
    
    /**
     * عد الألواح المملوكة لمستخدم معين
     */
    long countByOwner(User owner);
    
    /**
     * البحث عن الألواح المؤرشفة
     */
    @Query("SELECT b FROM Board b WHERE b.owner.id = :userId AND b.isArchived = true ORDER BY b.updatedAt DESC")
    List<Board> findArchivedBoardsByOwner(@Param("userId") Long userId);
}
