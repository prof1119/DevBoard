package com.devboard.mapper;


import com.devboard.dto.UserResponseDTO;
import com.devboard.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * UserMapper - تحويل بيانات المستخدم بين Entity و DTO
 */
@Mapper(componentModel = "spring")
public interface UserMapper {
    
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
    
    /**
     * تحويل User Entity إلى UserResponseDTO
     */
    UserResponseDTO toUserResponseDTO(User user);
    
    /**
     * تحويل UserResponseDTO إلى User Entity
     */
    User toUser(UserResponseDTO userResponseDTO);
}
