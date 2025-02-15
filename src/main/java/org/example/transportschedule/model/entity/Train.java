package org.example.transportschedule.model.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "trains")
@Data
@Schema(description = "Сущность, представляющая поезд и его маршрут")
public class Train {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Уникальный идентификатор поезда", example = "1")
    private long id;

    @Schema(description = "Город отправления", example = "Москва")
    private String cityFrom;

    @Schema(description = "Город прибытия", example = "Санкт-Петербург")
    private String cityTo;

    @Schema(description = "Цена билета", example = "1600.0")
    private double price;

    @Column(nullable = false)
    @Schema(description = "Дата и время отправления", example = "2025-02-15T15:30:00")
    private LocalDateTime dateOfDeparture;

    @Column(nullable = false)
    @Schema(description = "Дата и время прибытия", example = "2025-02-15T23:30:00")
    private LocalDateTime dateOfArrival;

    @ElementCollection
    @Schema(description = "Список промежуточных остановок", example = "[\"Владимир\", \"Тверь\"]")
    private List<String> stopList = new ArrayList<>();
}