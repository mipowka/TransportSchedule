package org.example.transportschedule.security;

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

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
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
                        .anyRequest().permitAll()
                )
                .httpBasic(Customizer.withDefaults()) // Использование HTTP Basic Auth
                .csrf(AbstractHttpConfigurer::disable); // Отключение CSRF

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(CustomUserDetailsService customUserDetailsService) throws Exception {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(customUserDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }
}
