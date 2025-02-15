package org.example.transportschedule.service.user;

import org.example.transportschedule.model.dto.UserDTO;

import java.util.List;

public interface UserService {
    UserDTO getUserById(long id);

    List<UserDTO> getAllUsers();

    UserDTO createUser(UserDTO user);

    UserDTO updateUser(long id, UserDTO user);

    void deleteUser(long id);

}
