package com.devboard.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.*;

@Entity
@Table(name = "tasks", indexes = {
    @Index(name = "idx_board", columnList = "board_id"),
    @Index(name = "idx_column", columnList = "column_id"),
    @Index(name = "idx_assigned", columnList = "assigned_to"),
    @Index(name = "idx_creator", columnList = "created_by"),
    @Index(name = "idx_github_issue", columnList = "github_issue_number"),
    @Index(name = "idx_status", columnList = "status"),
    @Index(name = "idx_priority", columnList = "priority")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "column_id")
    private BoardColumn column;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Priority priority = Priority.MEDIUM;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskStatus status = TaskStatus.TODO;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_to")
    private User assignedTo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @Column(nullable = false)
    private Integer position = 0;

    @Column
    private LocalDate dueDate;

    // GitHub Integration Fields
    @Column
    private Integer githubIssueNumber;

    @Column(length = 500)
    private String githubIssueUrl;

    @Column
    private Integer githubPrNumber;

    @Column(length = 500)
    private String githubPrUrl;

    @Column(length = 50)
    private String githubPrStatus;

    @Column
    private LocalDateTime githubPrMergedAt;

    // Time Tracking
    @Column(precision = 5, scale = 2)
    private BigDecimal estimatedHours;

    @Column(precision = 5, scale = 2)
    private BigDecimal actualHours;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "task_labels",
        joinColumns = @JoinColumn(name = "task_id"),
        inverseJoinColumns = @JoinColumn(name = "label_id"),
        indexes = {
            @Index(name = "idx_task_label_task", columnList = "task_id"),
            @Index(name = "idx_task_label_label", columnList = "label_id")
        }
    )
    private Set<Label> labels = new HashSet<>();

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy("createdAt DESC")
    private Set<TaskComment> comments = new HashSet<>();

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<TaskAttachment> attachments = new HashSet<>();

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy("createdAt DESC")
    private Set<TaskActivityLog> activityLog = new HashSet<>();

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public void addLabel(Label label) {
        this.labels.add(label);
    }

    public void removeLabel(Label label) {
        this.labels.remove(label);
    }

    public void addComment(TaskComment comment) {
        comment.setTask(this);
        this.comments.add(comment);
    }

    public void addActivity(TaskActivityLog activity) {
        activity.setTask(this);
        this.activityLog.add(activity);
    }

    public boolean isLinkedToGitHub() {
        return this.githubIssueNumber != null || this.githubPrNumber != null;
    }

    public boolean isPullRequestMerged() {
        return this.githubPrMergedAt != null;
    }

    public enum Priority {
        LOW,
        MEDIUM,
        HIGH,
        CRITICAL
    }

    public enum TaskStatus {
        TODO,
        IN_PROGRESS,
        IN_REVIEW,
        DONE,
        BLOCKED
    }

    public boolean canTransitionTo(TaskStatus newStatus) {
        // Implement business rules for status transitions
        // For now, all transitions are allowed
        return !this.status.equals(newStatus);
    }

    public String getGitHubLink() {
        return this.githubPrUrl != null ? this.githubPrUrl : this.githubIssueUrl;
    }
}
