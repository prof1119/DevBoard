package com.devboard.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.devboard.entity.Priority;
import com.devboard.entity.TaskStatus;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * Task Response DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Task Response")
public class TaskResponseDTO {
    
    @Schema(description = "Task ID")
    private Long id;
    
    @Schema(description = "Task title")
    private String title;
    
    @Schema(description = "Task description")
    private String description;
    
    @Schema(description = "Task priority")
    private Priority priority;
    
    @Schema(description = "Task status")
    private TaskStatus status;
    
    @Schema(description = "Task position in column")
    private Integer position;
    
    @Schema(description = "Is task archived")
    private Boolean isArchived;
    
    @Schema(description = "Due date")
    private LocalDateTime dueDate;
    
    @Schema(description = "Start date")
    private LocalDateTime startDate;
    
    @Schema(description = "Completed at")
    private LocalDateTime completedAt;
    
    @Schema(description = "Task creator")
    private UserResponseDTO creator;
    
    @Schema(description = "Task assignee")
    private UserResponseDTO assignee;
    
    @Schema(description = "Column information")
    private BoardColumnDTO column;
    
    @Schema(description = "Task tags")
    private Set<String> tags;
    
    @Schema(description = "Number of comments")
    private Integer commentCount;
    
    @Schema(description = "GitHub issue URL")
    private String githubIssueUrl;
    
    @Schema(description = "Created at")
    private LocalDateTime createdAt;
    
    @Schema(description = "Updated at")
    private LocalDateTime updatedAt;
}
