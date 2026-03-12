package com.devboard.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * BoardColumn Entity - يمثل عمود في لوحة كانبان
 * الأعمدة الافتراضية: Backlog, In Progress, Done
 */
@Entity
@Table(name = "board_columns", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"board_id", "name"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardColumn {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 50)
    private String name;
    
    @Column(length = 255)
    private String description;
    
    @Column(nullable = false)
    private Integer position = 0;
    
    @Column(nullable = false)
    private Boolean isArchived = false;
    
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(nullable = false)
    private LocalDateTime updatedAt;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;
    
    @OneToMany(mappedBy = "column", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Task> tasks = new HashSet<>();
    
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
