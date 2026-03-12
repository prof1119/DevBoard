package com.devboard.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Set;

/**
 * Board Detail Response DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Board Detail Response")
public class BoardDetailDTO {
    
    @Schema(description = "Board information")
    private BoardResponseDTO board;
    
    @Schema(description = "Board members")
    private Set<UserResponseDTO> members;
    
    @Schema(description = "Board columns")
    private Set<BoardColumnDTO> columns;
}
