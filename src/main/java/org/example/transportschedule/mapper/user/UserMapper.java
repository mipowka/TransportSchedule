package org.example.transportschedule.mapper.user;

import org.example.transportschedule.model.dto.UserDTO;
import org.example.transportschedule.model.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDTO mapToUserDTO(User user);

    User mapToUserEntity(UserDTO userDTO);
}
