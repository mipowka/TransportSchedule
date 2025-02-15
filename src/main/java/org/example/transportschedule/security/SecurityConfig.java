package org.example.transportschedule.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Конфигурация безопасности приложения.
 * Определяет правила доступа к эндпоинтам и настройки аутентификации/авторизации.
 */
@Configuration
public class SecurityConfig {

    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    /**
     * Создает цепочку фильтров безопасности для конфигурации доступа к эндпоинтам.
     *
     * @param http объект для настройки безопасности HTTP
     * @return настроенная цепочка фильтров безопасности
     * @throws Exception если произошла ошибка при конфигурации
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        logger.info("Настройка правил безопасности...");

        return http
                .authorizeHttpRequests(auth -> {
                    logger.info("Настройка правил авторизации...");
                    auth
                            // Доступ к расписанию поездов только для аутентифицированных пользователей
                            .requestMatchers(HttpMethod.GET, "/api/trains/**").authenticated()
                            // Доступ к расписанию автобусов для всех
                            .requestMatchers(HttpMethod.GET, "/api/buses/**").permitAll()
                            // Доступ к пользовательским эндпоинтам для всех
                            .requestMatchers("/api/users/**").permitAll()
                            // Добавление, редактирование и удаление транспортов только для ADMIN
                            .requestMatchers(HttpMethod.POST, "/api/trains").hasRole("ADMIN")
                            .requestMatchers(HttpMethod.PUT, "/api/trains/**").hasRole("ADMIN")
                            .requestMatchers(HttpMethod.DELETE, "/api/trains/**").hasRole("ADMIN")
                            .requestMatchers(HttpMethod.POST, "/api/buses").hasRole("ADMIN")
                            .requestMatchers(HttpMethod.PUT, "/api/buses/**").hasRole("ADMIN")
                            .requestMatchers(HttpMethod.DELETE, "/api/buses/**").hasRole("ADMIN")
                            // Все остальные запросы разрешены
                            .anyRequest().permitAll();
                })
                .httpBasic(Customizer.withDefaults()) // Использование HTTP Basic Auth
                .csrf(AbstractHttpConfigurer::disable) // Отключение CSRF
                .build();
    }

    /**
     * Создает кодировщик паролей BCrypt для безопасного хеширования паролей.
     *
     * @return объект PasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        logger.info("Инициализация кодировщика паролей BCrypt...");
        return new BCryptPasswordEncoder();
    }

    /**
     * Создает провайдер аутентификации, который использует сервис пользователей
     * и кодировщик паролей для проверки учетных данных.
     *
     * @param customUserDetailsService сервис пользователей
     * @return объект AuthenticationProvider
     * @throws Exception если произошла ошибка при настройке провайдера
     */
    @Bean
    public AuthenticationProvider authenticationProvider(CustomUserDetailsService customUserDetailsService) throws Exception {
        logger.info("Настройка провайдера аутентификации...");

        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(customUserDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());

        logger.info("Провайдер аутентификации настроен.");
        return daoAuthenticationProvider;
    }
}