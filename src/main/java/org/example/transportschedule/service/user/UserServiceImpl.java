package org.example.transportschedule.service.user;

import lombok.RequiredArgsConstructor;
import org.example.transportschedule.exception.UserNotFoundException;
import org.example.transportschedule.mapper.user.UserMapper;
import org.example.transportschedule.model.dto.UserDTO;
import org.example.transportschedule.model.entity.User;
import org.example.transportschedule.model.enums.Role;
import org.example.transportschedule.repository.user.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDTO getUserById(long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        return userMapper.mapToUserDTO(user);
    }

    @Override
    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();

        return users.stream()
                .map(userMapper::mapToUserDTO)
                .toList();
    }

    @Override
    public UserDTO createUser(UserDTO userDTO) {
        User user = userMapper.mapToUserEntity(userDTO);
        user.setRole(Role.USER);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return userDTO;
    }

    @Override
    public UserDTO updateUser(long id, UserDTO userDTO) {
        User existingUser = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));

        existingUser.setUsername(userDTO.username());
        existingUser.setPassword(passwordEncoder.encode(userDTO.password()));
        existingUser.setRole(userDTO.role());

        User updatedUser = userRepository.save(existingUser);

        return userMapper.mapToUserDTO(updatedUser);
    }

    @Override
    public void deleteUser(long id) {
        userRepository.deleteById(id);
    }

}
