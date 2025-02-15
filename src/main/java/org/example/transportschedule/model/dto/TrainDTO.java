package org.example.transportschedule.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

public record TrainDTO(
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
        LocalDateTime dateOfArrival,


        @NotEmpty(message = "Список остановок не должен быть пустым")
        @Max(value = 10, message = "Максимальное количество остановок - 10")
        List<
                @NotBlank(message = "Название остановки не должно быть пустым")
                @Pattern(regexp = "^[a-zA-Zа-яА-Я\\s-]+$", message = "Название остановки должно содержать только буквы")
                        String
                > stopList
) {
}
