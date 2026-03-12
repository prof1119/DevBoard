package com.devboard.mapper;


import com.devboard.dto.BoardResponseDTO;
import com.devboard.entity.Board;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * BoardMapper - تحويل بيانات اللوحة بين Entity و DTO
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface BoardMapper {
    
    BoardMapper INSTANCE = Mappers.getMapper(BoardMapper.class);
    
    /**
     * تحويل Board Entity إلى BoardResponseDTO
     */
    @Mapping(target = "memberCount", expression = "java(board.getMembers().size())")
    @Mapping(target = "columnCount", expression = "java(board.getColumns().size())")
    @Mapping(target = "taskCount", expression = "java(board.getTasks().size())")
    BoardResponseDTO toBoardResponseDTO(Board board);
}
