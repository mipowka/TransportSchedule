package org.example.transportschedule.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name = "buses")
@Getter
@Setter
@ToString
public class Bus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String cityFrom;
    private String cityTo;

    private double price;

    @Column(nullable = false)
    private LocalDateTime dateOfDeparture;
    @Column(nullable = false)
    private LocalDateTime dateOfArrival;
}
