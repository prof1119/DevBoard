package com.devboard.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Reorder Columns Request DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Reorder Columns Request")
public class ReorderColumnsDTO {
    
    @Schema(description = "Column ID")
    private Long columnId;
    
    @Schema(description = "New position")
    private Integer newPosition;
}
