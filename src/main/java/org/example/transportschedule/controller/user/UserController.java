package org.example.transportschedule.controller.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "User Management", description = "API для управления пользователями")
public class UserController {

    private final UserService userService;

    @Operation(
            summary = "Получение пользователя по ID",
            description = "Возвращает пользователя на основе его уникального ID")
    @ApiResponse(
            responseCode = "200",
            description = "Успешный запрос"
    )
    @ApiResponse(
            responseCode = "404",
            description = "Пользователь не найден"
    )
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(
            @Parameter(description = "ID пользователя", required = true) @PathVariable long id) {
        UserDTO userDTO = userService.getUserById(id);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @Operation(
            summary = "Получение всех пользователей",
            description = "Возвращает список всех зарегистрированных пользователей")
    @ApiResponse(
            responseCode = "200",
            description = "Успешный запрос"
    )
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @Operation(
            summary = "Создание нового пользователя",
            description = "Создает нового пользователя на основе переданных данных")
    @ApiResponse(
            responseCode = "201",
            description = "Пользователь успешно создан"
    )
    @ApiResponse(
            responseCode = "400",
            description = "Неверные данные"
    )
    @PostMapping
    public ResponseEntity<UserDTO> createUser(
            @Parameter(description = "Данные нового пользователя", required = true) @RequestBody UserDTO userDTO) {
        UserDTO createdUser = userService.createUser(userDTO);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Обновление пользователя",
            description = "Обновляет информацию о существующем пользователе")
    @ApiResponse(
            responseCode = "200",
            description = "Пользователь успешно обновлен"
    )
    @ApiResponse(
            responseCode = "404",
            description = "Пользователь не найден"
    )
    @ApiResponse(
            responseCode = "400",
            description = "Неверные данные"
    )
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(
            @Parameter(description = "ID пользователя для обновления", required = true) @PathVariable long id,
            @Parameter(description = "Обновленные данные пользователя", required = true) @RequestBody UserDTO userDTO) {
        UserDTO updatedUser = userService.updateUser(id, userDTO);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @Operation(
            summary = "Удаление пользователя",
            description = "Удаляет пользователя на основе его ID")
    @ApiResponse(
            responseCode = "204",
            description = "Пользователь успешно удален"
    )
    @ApiResponse(
            responseCode = "404",
            description = "Пользователь не найден"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "ID удаляемого пользователя", required = true) @PathVariable long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
