package com.devboard.repository;


import com.devboard.entity.Board;
import com.devboard.entity.BoardColumn;
import com.devboard.entity.Priority;
import com.devboard.entity.Task;
import com.devboard.entity.TaskStatus;
import com.devboard.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * TaskRepository - يتعامل مع عمليات قاعدة البيانات للمهام
 */
@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    
    /**
     * البحث عن جميع المهام في عمود معين
     */
    List<Task> findByColumnOrderByPosition(BoardColumn column);
    
    /**
     * البحث عن جميع المهام في لوحة معينة
     */
    List<Task> findByBoardOrderByCreatedAtDesc(Board board);
    
    /**
     * البحث عن المهام المسندة لمستخدم معين
     */
    @Query("SELECT t FROM Task t WHERE t.assignee.id = :userId AND t.isArchived = false ORDER BY t.dueDate, t.priority DESC")
    List<Task> findTasksByAssignee(@Param("userId") Long userId);
    
    /**
     * البحث عن المهام المسندة لمستخدم معين في لوحة معينة
     */
    @Query("SELECT t FROM Task t WHERE t.assignee.id = :userId AND t.board.id = :boardId AND t.isArchived = false")
    List<Task> findTasksByAssigneeAndBoard(@Param("userId") Long userId, @Param("boardId") Long boardId);
    
    /**
     * البحث عن المهام المنتهية الأجل
     */
    @Query("SELECT t FROM Task t WHERE t.dueDate < :now AND t.status != 'DONE' AND t.isArchived = false ORDER BY t.dueDate")
    List<Task> findOverdueTasks(@Param("now") LocalDateTime now);
    
    /**
     * البحث عن المهام عالية الأولوية
     */
    @Query("SELECT t FROM Task t WHERE t.board.id = :boardId AND t.priority IN ('HIGH', 'CRITICAL') " +
           "AND t.status != 'DONE' AND t.isArchived = false ORDER BY t.priority DESC, t.createdAt DESC")
    List<Task> findHighPriorityTasks(@Param("boardId") Long boardId);
    
    /**
     * البحث عن المهام بالحالة معينة
     */
    @Query("SELECT t FROM Task t WHERE t.board.id = :boardId AND t.status = :status AND t.isArchived = false ORDER BY t.position")
    List<Task> findTasksByStatus(@Param("boardId") Long boardId, @Param("status") TaskStatus status);
    
    /**
     * البحث عن المهام التي أنشأها مستخدم معين
     */
    List<Task> findByCreatorOrderByCreatedAtDesc(User creator);
    
    /**
     * البحث عن المهام بناءً على الكلمة المفتاحية
     */
    @Query("SELECT t FROM Task t WHERE t.board.id = :boardId AND " +
           "(LOWER(t.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(t.description) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
           "t.isArchived = false ORDER BY t.title")
    List<Task> searchTasks(@Param("boardId") Long boardId, @Param("keyword") String keyword);
    
    /**
     * عد المهام في عمود معين
     */
    long countByColumn(BoardColumn column);
    
    /**
     * البحث عن الحد الأقصى للموضع في عمود
     */
    @Query("SELECT MAX(t.position) FROM Task t WHERE t.column.id = :columnId")
    Optional<Integer> findMaxPositionByColumn(@Param("columnId") Long columnId);
    
    /**
     * البحث عن المهام بناءً على الوسم
     */
    @Query("SELECT t FROM Task t WHERE t.board.id = :boardId AND :tag MEMBER OF t.tags AND t.isArchived = false")
    List<Task> findTasksByTag(@Param("boardId") Long boardId, @Param("tag") String tag);
    
    /**
     * البحث عن مهمة بمعرف مشكلة GitHub
     */
    Optional<Task> findByGithubIssueId(String githubIssueId);
}
