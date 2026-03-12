package com.devboard.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * Board Column Response DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Board Column Response")
public class BoardColumnDTO {
    
    @Schema(description = "Column ID")
    private Long id;
    
    @Schema(description = "Column name")
    private String name;
    
    @Schema(description = "Column description")
    private String description;
    
    @Schema(description = "Column position")
    private Integer position;
    
    @Schema(description = "Is column archived")
    private Boolean isArchived;
    
    @Schema(description = "Number of tasks in column")
    private Integer taskCount;
    
    @Schema(description = "Created at")
    private LocalDateTime createdAt;
    
    @Schema(description = "Updated at")
    private LocalDateTime updatedAt;
}
