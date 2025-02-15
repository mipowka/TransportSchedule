package org.example.transportschedule.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.transportschedule.exception.UserNotFoundException;
import org.example.transportschedule.mapper.user.UserMapper;
import org.example.transportschedule.model.dto.UserDTO;
import org.example.transportschedule.model.entity.User;
import org.example.transportschedule.model.enums.Role;
import org.example.transportschedule.repository.user.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Сервис для работы с пользователями.
 * Предоставляет методы для получения, создания, обновления и удаления пользователей.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    /**
     * Получает пользователя по его идентификатору.
     *
     * @param id Идентификатор пользователя.
     * @return {@link UserDTO} - данные пользователя.
     * @throws UserNotFoundException если пользователь с указанным идентификатором не найден.
     */
    @Override
    public UserDTO getUserById(long id) {
        log.info("Получение пользователя по ID: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Пользователь с ID {} не найден", id);
                    return new UserNotFoundException(id);
                });
        log.info("Пользователь с ID {} успешно получен", id);
        return userMapper.mapToUserDTO(user);
    }

    /**
     * Получает список всех пользователей.
     *
     * @return Список {@link UserDTO} - данные всех пользователей.
     */
    @Override
    public List<UserDTO> getAllUsers() {
        log.info("Получение списка всех пользователей");
        List<User> users = userRepository.findAll();
        log.info("Найдено {} пользователей", users.size());
        return users.stream()
                .map(userMapper::mapToUserDTO)
                .toList();
    }

    /**
     * Создает нового пользователя.
     *
     * @param userDTO Данные нового пользователя.
     * @return {@link UserDTO} - данные созданного пользователя.
     */
    @Override
    public UserDTO createUser(UserDTO userDTO) {
        log.info("Создание нового пользователя: {}", userDTO.username());
        User user = userMapper.mapToUserEntity(userDTO);
        user.setRole(Role.USER);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        log.info("Пользователь {} успешно создан", userDTO.username());
        return userDTO;
    }

    /**
     * Обновляет данные пользователя по его идентификатору.
     *
     * @param id      Идентификатор пользователя.
     * @param userDTO Новые данные пользователя.
     * @return {@link UserDTO} - обновленные данные пользователя.
     * @throws UserNotFoundException если пользователь с указанным идентификатором не найден.
     */
    @Override
    public UserDTO updateUser(long id, UserDTO userDTO) {
        log.info("Обновление пользователя с ID: {}", id);
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Пользователь с ID {} не найден", id);
                    return new UserNotFoundException(id);
                });

        existingUser.setUsername(userDTO.username());
        existingUser.setPassword(passwordEncoder.encode(userDTO.password()));
        existingUser.setRole(userDTO.role());

        User updatedUser = userRepository.save(existingUser);
        log.info("Пользователь с ID {} успешно обновлен", id);
        return userMapper.mapToUserDTO(updatedUser);
    }

    /**
     * Удаляет пользователя по его идентификатору.
     *
     * @param id Идентификатор пользователя.
     */
    @Override
    public void deleteUser(long id) {
        log.info("Удаление пользователя с ID: {}", id);
        if (!userRepository.existsById(id)) {
            log.error("Пользователь с ID {} не найден", id);
            throw new UserNotFoundException(id);
        }
        userRepository.deleteById(id);
        log.info("Пользователь с ID {} успешно удален", id);
    }
}