package com.devboard.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Create Comment Request DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Create Comment Request")
public class CreateCommentDTO {
    
    @NotBlank(message = "Comment content is required")
    @Size(min = 1, max = 5000, message = "Comment must be between 1 and 5000 characters")
    @Schema(description = "Comment content")
    private String content;
    
    @Schema(description = "Parent comment ID (for replies)")
    private Long parentCommentId;
}
