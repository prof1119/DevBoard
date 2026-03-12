package com.devboard.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.devboard.entity.Priority;
import com.devboard.entity.TaskStatus;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * Create Task Request DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Create Task Request")
public class CreateTaskDTO {
    
    @NotBlank(message = "Task title is required")
    @Size(min = 2, max = 200, message = "Task title must be between 2 and 200 characters")
    @Schema(description = "Task title", example = "Implement user authentication")
    private String title;
    
    @Size(max = 2000, message = "Description must not exceed 2000 characters")
    @Schema(description = "Task description")
    private String description;
    
    @NotNull(message = "Column ID is required")
    @Schema(description = "Column ID where task will be placed")
    private Long columnId;
    
    @Schema(description = "Task priority")
    private Priority priority = Priority.MEDIUM;
    
    @Schema(description = "Task status")
    private TaskStatus status = TaskStatus.TODO;
    
    @Schema(description = "Assignee user ID")
    private Long assigneeId;
    
    @Schema(description = "Due date for task")
    private LocalDateTime dueDate;
    
    @Schema(description = "Start date for task")
    private LocalDateTime startDate;
    
    @Schema(description = "Task tags/labels")
    private Set<String> tags;
}
