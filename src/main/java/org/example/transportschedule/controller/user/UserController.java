package org.example.transportschedule.controller.user;

import lombok.RequiredArgsConstructor;
import org.example.transportschedule.model.dto.UserDTO;
import org.example.transportschedule.service.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * Получить пользователя по ID.
     *
     * @param id Идентификатор пользователя.
     * @return Ответ с пользователем.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable long id) {
        UserDTO userDTO = userService.getUserById(id);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    /**
     * Получить всех пользователей.
     *
     * @return Ответ со списком пользователей.
     */
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    /**
     * Создать нового пользователя.
     *
     * @param userDTO Объект с данными для создания пользователя.
     * @return Ответ с созданным пользователем.
     */
    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) {
        UserDTO createdUser = userService.createUser(userDTO);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    /**
     * Обновить пользователя по ID.
     *
     * @param id Идентификатор пользователя для обновления.
     * @param userDTO Обновленные данные пользователя.
     * @return Ответ с обновленным пользователем.
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable long id, @RequestBody UserDTO userDTO) {
        UserDTO updatedUser = userService.updateUser(id, userDTO);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    /**
     * Удалить пользователя по ID.
     *
     * @param id Идентификатор пользователя для удаления.
     * @return Ответ с успешным удалением.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
