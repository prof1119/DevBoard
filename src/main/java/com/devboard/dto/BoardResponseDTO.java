package com.devboard.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * Board Response DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Board Response")
public class BoardResponseDTO {
    
    @Schema(description = "Board ID")
    private Long id;
    
    @Schema(description = "Board name")
    private String name;
    
    @Schema(description = "Board description")
    private String description;
    
    @Schema(description = "Background image URL")
    private String backgroundImage;
    
    @Schema(description = "Is board public")
    private Boolean isPublic;
    
    @Schema(description = "Is board archived")
    private Boolean isArchived;
    
    @Schema(description = "Board owner")
    private UserResponseDTO owner;
    
    @Schema(description = "Number of members")
    private Integer memberCount;
    
    @Schema(description = "Number of columns")
    private Integer columnCount;
    
    @Schema(description = "Number of tasks")
    private Integer taskCount;
    
    @Schema(description = "Created at")
    private LocalDateTime createdAt;
    
    @Schema(description = "Updated at")
    private LocalDateTime updatedAt;
}
