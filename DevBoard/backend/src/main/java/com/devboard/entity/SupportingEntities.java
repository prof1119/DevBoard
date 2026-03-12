package com.devboard.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.*;

// ==================== BOARD COLUMN ====================
@Entity
@Table(name = "board_columns", indexes = {
    @Index(name = "idx_board", columnList = "board_id"),
    @Index(name = "idx_position", columnList = "board_id, position")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
class BoardColumn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private Integer position;

    @Column(length = 7)
    private String color;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToMany(mappedBy = "column", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy("position ASC")
    private Set<Task> tasks = new HashSet<>();

    public int getTaskCount() {
        return this.tasks != null ? this.tasks.size() : 0;
    }
}

// ==================== BOARD MEMBER ====================
@Entity
@Table(name = "board_members", indexes = {
    @Index(name = "idx_board", columnList = "board_id"),
    @Index(name = "idx_user", columnList = "user_id")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
class BoardMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BoardRole role = BoardRole.EDITOR;

    @Column(nullable = false, updatable = false)
    private LocalDateTime addedAt = LocalDateTime.now();

    public enum BoardRole {
        OWNER,      // Full control
        EDITOR,     // Can create/edit/delete tasks
        COMMENTER,  // Can comment and view
        VIEWER      // Read-only
    }

    public boolean canEdit() {
        return this.role == BoardRole.OWNER || this.role == BoardRole.EDITOR;
    }

    public boolean canComment() {
        return this.canEdit() || this.role == BoardRole.COMMENTER;
    }

    public boolean canDelete() {
        return this.role == BoardRole.OWNER;
    }
}

// ==================== LABEL ====================
@Entity
@Table(name = "labels", indexes = {
    @Index(name = "idx_board", columnList = "board_id")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
class Label {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 7)
    private String color = "#0366d6";

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToMany(mappedBy = "labels", fetch = FetchType.LAZY)
    private Set<Task> tasks = new HashSet<>();
}

// ==================== TASK COMMENT ====================
@Entity
@Table(name = "task_comments", indexes = {
    @Index(name = "idx_task", columnList = "task_id"),
    @Index(name = "idx_user", columnList = "user_id"),
    @Index(name = "idx_created", columnList = "created_at")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
class TaskComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column
    private Long githubCommentId;

    @Column(nullable = false)
    private Boolean isEdited = false;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
        this.isEdited = true;
    }
}

// ==================== TASK ATTACHMENT ====================
@Entity
@Table(name = "task_attachments", indexes = {
    @Index(name = "idx_task", columnList = "task_id")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
class TaskAttachment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;

    @Column(nullable = false, length = 255)
    private String fileName;

    @Column(nullable = false, length = 500)
    private String fileUrl;

    @Column
    private Long fileSize;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uploaded_by", nullable = false)
    private User uploadedBy;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}

// ==================== TASK ACTIVITY LOG ====================
@Entity
@Table(name = "task_activity_log", indexes = {
    @Index(name = "idx_task", columnList = "task_id"),
    @Index(name = "idx_user", columnList = "user_id"),
    @Index(name = "idx_action", columnList = "action"),
    @Index(name = "idx_created", columnList = "created_at")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
class TaskActivityLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false, length = 50)
    private String action;

    @Column(length = 500)
    private String oldValue;

    @Column(length = 500)
    private String newValue;

    @Column(length = 100)
    private String fieldName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ActivityType activityType = ActivityType.UPDATED;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public enum ActivityType {
        CREATED,
        UPDATED,
        MOVED,
        ASSIGNED,
        COMMENTED,
        GITHUB_SYNC
    }
}

// ==================== BOARD INVITATION ====================
@Entity
@Table(name = "board_invitations", indexes = {
    @Index(name = "idx_board", columnList = "board_id"),
    @Index(name = "idx_email", columnList = "email"),
    @Index(name = "idx_token", columnList = "token")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
class BoardInvitation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @Column(nullable = false, length = 255)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BoardMember.BoardRole role = BoardMember.BoardRole.EDITOR;

    @Column(nullable = false, unique = true, length = 255)
    private String token;

    @Column(nullable = false)
    private Boolean isAccepted = false;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column
    private LocalDateTime expiresAt;

    public boolean isExpired() {
        return this.expiresAt != null && LocalDateTime.now().isAfter(this.expiresAt);
    }

    public boolean isValid() {
        return !this.isAccepted && !this.isExpired();
    }
}

// ==================== GITHUB WEBHOOK EVENT ====================
@Entity
@Table(name = "github_webhook_events", indexes = {
    @Index(name = "idx_board", columnList = "board_id"),
    @Index(name = "idx_event_type", columnList = "github_event_type"),
    @Index(name = "idx_processed", columnList = "processed")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
class GitHubWebhookEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @Column(nullable = false, length = 50)
    private String githubEventType;

    @Column(unique = true, length = 255)
    private String githubDeliveryId;

    @Column(columnDefinition = "JSON")
    private String payload;

    @Column(nullable = false)
    private Boolean processed = false;

    @Column(columnDefinition = "TEXT")
    private String errorMessage;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}

// ==================== NOTIFICATION ====================
@Entity
@Table(name = "notifications", indexes = {
    @Index(name = "idx_user", columnList = "user_id"),
    @Index(name = "idx_read", columnList = "is_read"),
    @Index(name = "idx_created", columnList = "created_at")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id")
    private Task task;

    @Column(nullable = false, length = 50)
    private String type;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String message;

    @Column(nullable = false)
    private Boolean isRead = false;

    @Column(length = 500)
    private String actionUrl;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}
