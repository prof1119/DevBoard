package com.devboard.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "boards", indexes = {
    @Index(name = "idx_owner", columnList = "owner_id"),
    @Index(name = "idx_github_repo", columnList = "github_repo_owner, github_repo_name")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @Column(nullable = false)
    private Boolean isPublic = false;

    @Column(length = 500)
    private String githubRepoUrl;

    @Column(length = 100)
    private String githubRepoOwner;

    @Column(length = 100)
    private String githubRepoName;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy("position ASC")
    private Set<BoardColumn> columns = new HashSet<>();

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<BoardMember> members = new HashSet<>();

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Task> tasks = new HashSet<>();

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Label> labels = new HashSet<>();

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<BoardInvitation> invitations = new HashSet<>();

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<GitHubWebhookEvent> webhookEvents = new HashSet<>();

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public boolean isOwnedBy(User user) {
        return this.owner != null && this.owner.getId().equals(user.getId());
    }

    public BoardMember getMemberByUser(User user) {
        return this.members.stream()
            .filter(m -> m.getUser().getId().equals(user.getId()))
            .findFirst()
            .orElse(null);
    }

    public boolean hasGitHubIntegration() {
        return this.githubRepoOwner != null && this.githubRepoName != null;
    }

    public String getGitHubRepositoryFullName() {
        return this.githubRepoOwner + "/" + this.githubRepoName;
    }
}
