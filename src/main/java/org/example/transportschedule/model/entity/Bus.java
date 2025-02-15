package org.example.transportschedule.model.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name = "buses")
@Getter
@Setter
@Schema(description = "Сущность, представляющая автобус и его маршрут")
public class Bus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Уникальный идентификатор поезда", example = "1")
    private long id;

    @Schema(description = "Город отправления", example = "Москва")
    private String cityFrom;

    @Schema(description = "Город прибытия", example = "Казань")
    private String cityTo;

    @Schema(description = "Цена билета", example = "500.5")
    private double price;

    @Column(nullable = false)
    @Schema(description = "Дата и время отправления", example = "2025-02-15T23:30:00")
    private LocalDateTime dateOfDeparture;

    @Column(nullable = false)
    @Schema(description = "Дата и время прибытия", example = "2025-02-15T23:30:00")
    private LocalDateTime dateOfArrival;
}
