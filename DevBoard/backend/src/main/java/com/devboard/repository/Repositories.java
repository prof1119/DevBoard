package com.devboard.repository;

import com.devboard.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    List<User> findByRole(User.UserRole role);
    List<User> findByIsActiveTrue();
    
    @Query("SELECT u FROM User u WHERE u.email LIKE %:email%")
    List<User> searchByEmail(@Param("email") String email);
}

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    List<Board> findByOwnerId(Long ownerId);
    List<Board> findByIsPublicTrue();
    
    @Query("SELECT b FROM Board b WHERE b.githubRepoOwner = :owner AND b.githubRepoName = :name")
    Optional<Board> findByGitHubRepository(@Param("owner") String owner, @Param("name") String name);
    
    @Query("SELECT b FROM Board b JOIN b.members m WHERE m.user.id = :userId")
    List<Board> findByMemberId(@Param("userId") Long userId);
}

@Repository
public interface BoardColumnRepository extends JpaRepository<BoardColumn, Long> {
    List<BoardColumn> findByBoardIdOrderByPosition(Long boardId);
}

@Repository
public interface BoardMemberRepository extends JpaRepository<BoardMember, Long> {
    List<BoardMember> findByBoardId(Long boardId);
    List<BoardMember> findByUserId(Long userId);
    Optional<BoardMember> findByBoardIdAndUserId(Long boardId, Long userId);
}

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByBoardId(Long boardId);
    List<Task> findByColumnIdOrderByPosition(Long columnId);
    List<Task> findByAssignedToId(Long userId);
    List<Task> findByCreatedById(Long userId);
    List<Task> findByStatus(Task.TaskStatus status);
    List<Task> findByPriority(Task.Priority priority);
    
    @Query("SELECT COALESCE(MAX(t.position), -1) FROM Task t WHERE t.column.id = :columnId")
    Integer findMaxPositionByColumnId(@Param("columnId") Long columnId);
    
    @Query("SELECT t FROM Task t WHERE t.githubIssueNumber = :issueNumber AND t.board.githubRepoOwner = :owner AND t.board.githubRepoName = :name")
    Optional<Task> findByGitHubIssue(@Param("issueNumber") Integer issueNumber, 
                                     @Param("owner") String owner, 
                                     @Param("name") String name);
    
    @Query("SELECT t FROM Task t WHERE t.githubPrNumber = :prNumber AND t.board.githubRepoOwner = :owner AND t.board.githubRepoName = :name")
    Optional<Task> findByGitHubPullRequest(@Param("prNumber") Integer prNumber, 
                                           @Param("owner") String owner, 
                                           @Param("name") String name);
    
    @Query("SELECT COUNT(t) FROM Task t WHERE t.board.id = :boardId AND t.status = 'DONE'")
    Long countCompletedTasks(@Param("boardId") Long boardId);
    
    @Query("SELECT COUNT(t) FROM Task t WHERE t.board.id = :boardId")
    Long countTotalTasks(@Param("boardId") Long boardId);
}

@Repository
public interface LabelRepository extends JpaRepository<Label, Long> {
    List<Label> findByBoardId(Long boardId);
    Optional<Label> findByBoardIdAndName(Long boardId, String name);
}

@Repository
public interface TaskCommentRepository extends JpaRepository<TaskComment, Long> {
    List<TaskComment> findByTaskIdOrderByCreatedAtDesc(Long taskId);
    List<TaskComment> findByUserId(Long userId);
}

@Repository
public interface TaskAttachmentRepository extends JpaRepository<TaskAttachment, Long> {
    List<TaskAttachment> findByTaskId(Long taskId);
}

@Repository
public interface TaskActivityLogRepository extends JpaRepository<TaskActivityLog, Long> {
    List<TaskActivityLog> findByTaskIdOrderByCreatedAtDesc(Long taskId);
    List<TaskActivityLog> findByUserIdOrderByCreatedAtDesc(Long userId);
    
    @Query("SELECT l FROM TaskActivityLog l WHERE l.task.board.id = :boardId ORDER BY l.createdAt DESC")
    List<TaskActivityLog> findByBoardIdOrderByCreatedAtDesc(@Param("boardId") Long boardId);
}

@Repository
public interface BoardInvitationRepository extends JpaRepository<BoardInvitation, Long> {
    List<BoardInvitation> findByBoardId(Long boardId);
    Optional<BoardInvitation> findByToken(String token);
    List<BoardInvitation> findByEmail(String email);
}

@Repository
public interface GitHubWebhookEventRepository extends JpaRepository<GitHubWebhookEvent, Long> {
    List<GitHubWebhookEvent> findByBoardIdAndProcessedFalse(Long boardId);
    Optional<GitHubWebhookEvent> findByGithubDeliveryId(String deliveryId);
}

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserIdOrderByCreatedAtDesc(Long userId);
    List<Notification> findByUserIdAndIsReadFalse(Long userId);
    Long countByUserIdAndIsReadFalse(Long userId);
}
