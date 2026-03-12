package com.devboard.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
 * Update Task Request DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Update Task Request")
public class UpdateTaskDTO {
    
    @Size(min = 2, max = 200, message = "Task title must be between 2 and 200 characters")
    @Schema(description = "Task title")
    private String title;
    
    @Size(max = 2000, message = "Description must not exceed 2000 characters")
    @Schema(description = "Task description")
    private String description;
    
    @Schema(description = "Task priority")
    private Priority priority;
    
    @Schema(description = "Task status")
    private TaskStatus status;
    
    @Schema(description = "Assignee user ID")
    private Long assigneeId;
    
    @Schema(description = "Due date")
    private LocalDateTime dueDate;
    
    @Schema(description = "Start date")
    private LocalDateTime startDate;
    
    @Schema(description = "Task tags")
    private Set<String> tags;
}
