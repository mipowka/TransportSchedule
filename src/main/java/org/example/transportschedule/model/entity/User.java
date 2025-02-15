package org.example.transportschedule.model.entity;

import jakarta.persistence.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.example.transportschedule.model.enums.Role;

@Entity
@Table(name = "users")
@Data
@Schema(description = "Сущность пользователя, представляющая зарегистрированного пользователя в системе.")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Уникальный идентификатор пользователя", example = "1")
    private long id;

    @Schema(description = "Имя пользователя", example = "john_doe")
    private String username;

    @Schema(description = "Пароль пользователя", example = "password123", accessMode = Schema.AccessMode.WRITE_ONLY)
    private String password;

    @Enumerated(EnumType.STRING)
    @Schema(description = "Роль пользователя", example = "USER")
    private Role role;
}
