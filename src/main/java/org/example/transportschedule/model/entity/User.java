package org.example.transportschedule.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.example.transportschedule.model.enums.Role;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String username;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;

}
