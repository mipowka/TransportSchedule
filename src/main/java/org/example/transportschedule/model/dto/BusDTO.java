package org.example.transportschedule.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public record BusDTO(
        @NotBlank(message = "Город отправления не должен быть пустым")
        @Pattern(regexp = "^[a-zA-Zа-яА-Я\\s-]+$", message = "Город отправления должен содержать только буквы")
        String cityFrom,

        @NotBlank(message = "Город прибытия не должен быть пустым")
        @Pattern(regexp = "^[a-zA-Zа-яА-Я\\s-]+$", message = "Город прибытия должен содержать только буквы")
        String cityTo,

        @Min(value = 0, message = "Цена должна быть неотрицательной")
        double price,

        @NotNull(message = "Дата и время отправления не могут быть пустыми")
        @DateTimeFormat(pattern = "dd.MM.yyyy HH:mm")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy HH:mm")
        LocalDateTime dateOfDeparture,

        @NotNull(message = "Дата и время прибытия не могут быть пустыми")
        @DateTimeFormat(pattern = "dd.MM.yyyy HH:mm")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy HH:mm")
        LocalDateTime dateOfArrival
) {
}
