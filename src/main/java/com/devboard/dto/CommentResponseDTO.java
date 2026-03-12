package com.devboard.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * Comment Response DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Comment Response")
public class CommentResponseDTO {
    
    @Schema(description = "Comment ID")
    private Long id;
    
    @Schema(description = "Comment content")
    private String content;
    
    @Schema(description = "Comment author")
    private UserResponseDTO author;
    
    @Schema(description = "Is comment edited")
    private Boolean isEdited;
    
    @Schema(description = "Is comment deleted")
    private Boolean isDeleted;
    
    @Schema(description = "Parent comment ID")
    private Long parentCommentId;
    
    @Schema(description = "Created at")
    private LocalDateTime createdAt;
    
    @Schema(description = "Updated at")
    private LocalDateTime updatedAt;
}
