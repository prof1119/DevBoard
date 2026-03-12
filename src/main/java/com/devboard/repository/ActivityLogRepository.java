package com.devboard.repository;


import com.devboard.entity.ActivityLog;
import com.devboard.entity.Board;
import com.devboard.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * ActivityLogRepository - يتعامل مع عمليات قاعدة البيانات لسجلات الأنشطة
 */
@Repository
public interface ActivityLogRepository extends JpaRepository<ActivityLog, Long> {
    
    /**
     * البحث عن جميع الأنشطة على لوحة معينة
     */
    @Query("SELECT a FROM ActivityLog a WHERE a.board.id = :boardId ORDER BY a.createdAt DESC")
    Page<ActivityLog> findBoardActivities(@Param("boardId") Long boardId, Pageable pageable);
    
    /**
     * البحث عن جميع الأنشطة على مهمة معينة
     */
    @Query("SELECT a FROM ActivityLog a WHERE a.task.id = :taskId ORDER BY a.createdAt DESC")
    List<ActivityLog> findTaskActivities(@Param("taskId") Long taskId);
    
    /**
     * البحث عن الأنشطة للمستخدم معين
     */
    @Query("SELECT a FROM ActivityLog a WHERE a.user.id = :userId ORDER BY a.createdAt DESC")
    Page<ActivityLog> findUserActivities(@Param("userId") Long userId, Pageable pageable);
    
    /**
     * البحث عن الأنشطة في نطاق زمني معين
     */
    @Query("SELECT a FROM ActivityLog a WHERE a.createdAt BETWEEN :startDate AND :endDate ORDER BY a.createdAt DESC")
    List<ActivityLog> findActivitiesByDateRange(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);
    
    /**
     * البحث عن الأنشطة بنوع معين
     */
    @Query("SELECT a FROM ActivityLog a WHERE a.entityType = :entityType AND a.action = :action ORDER BY a.createdAt DESC")
    List<ActivityLog> findActivitiesByTypeAndAction(
            @Param("entityType") String entityType,
            @Param("action") String action);
    
    /**
     * حذف الأنشطة القديمة (قبل تاريخ معين)
     */
    void deleteByCreatedAtBefore(LocalDateTime cutoffDate);
    
    /**
     * عد الأنشطة على لوحة معينة
     */
    long countByBoard(Board board);
    
    /**
     * عد الأنشطة على مهمة معينة
     */
    long countByTask(Task task);
}
