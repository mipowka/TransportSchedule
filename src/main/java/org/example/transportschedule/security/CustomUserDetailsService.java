package org.example.transportschedule.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.transportschedule.model.entity.User;
import org.example.transportschedule.repository.user.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Сервис для загрузки пользовательских данных на основе имени пользователя.
 * Реализует интерфейс UserDetailsService из Spring Security.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {


    private final UserRepository userRepository;

    /**
     * Префикс для ролей пользователя в системе.
     */
    private final static String ROLE_PREFIX = "ROLE_";

    /**
     * Метод загружает данные пользователя по имени пользователя.
     *
     * @param username имя пользователя для поиска
     * @return объект UserDetails, содержащий данные пользователя
     * @throws UsernameNotFoundException если пользователь не найден
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Запрос на загрузку пользователя с именем: {}", username);

        // Поиск пользователя в базе данных
        User user = userRepository.findByUsername(username);
        if (user == null) {
            log.error("Пользователь с именем {} не найден", username);
            throw new UsernameNotFoundException("Пользователь с именем " + username + " не найден");
        }

        log.info("Пользователь {} успешно найден", username);

        // Создание списка ролей пользователя
        List<GrantedAuthority> roles = new ArrayList<>();
        String role = ROLE_PREFIX + user.getRole();
        roles.add(new SimpleGrantedAuthority(role));

        log.info("Роль пользователя {}: {}", username, role);

        // Возвращаем объект CustomUserDetails с данными пользователя
        return new CustomUserDetails(
                user.getUsername(),
                user.getPassword(),
                roles
        );
    }
}