package com.devboard.repository;


import com.devboard.entity.Comment;
import com.devboard.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * CommentRepository - يتعامل مع عمليات قاعدة البيانات للتعليقات
 */
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    
    /**
     * البحث عن جميع التعليقات على مهمة معينة
     */
    @Query("SELECT c FROM Comment c WHERE c.task.id = :taskId AND c.isDeleted = false ORDER BY c.createdAt DESC")
    List<Comment> findByTaskOrderByCreatedAtDesc(@Param("taskId") Long taskId);
    
    /**
     * البحث عن التعليقات الرئيسية على مهمة معينة
     */
    @Query("SELECT c FROM Comment c WHERE c.task.id = :taskId AND c.parentComment IS NULL AND c.isDeleted = false ORDER BY c.createdAt DESC")
    List<Comment> findParentCommentsByTask(@Param("taskId") Long taskId);
    
    /**
     * البحث عن الردود على تعليق معين
     */
    @Query("SELECT c FROM Comment c WHERE c.parentComment.id = :parentCommentId AND c.isDeleted = false ORDER BY c.createdAt ASC")
    List<Comment> findRepliesByParentComment(@Param("parentCommentId") Long parentCommentId);
    
    /**
     * عد التعليقات على مهمة معينة
     */
    @Query("SELECT COUNT(c) FROM Comment c WHERE c.task.id = :taskId AND c.isDeleted = false")
    long countCommentsByTask(@Param("taskId") Long taskId);
    
    /**
     * حذف جميع التعليقات على مهمة معينة
     */
    void deleteByTask(Task task);
    
    /**
     * البحث عن آخر التعليقات على مهمة
     */
    @Query("SELECT c FROM Comment c WHERE c.task.id = :taskId AND c.isDeleted = false ORDER BY c.createdAt DESC LIMIT 5")
    List<Comment> findLatestCommentsByTask(@Param("taskId") Long taskId);
}
