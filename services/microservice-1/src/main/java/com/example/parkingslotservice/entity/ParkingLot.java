package com.example.parkingslotservice.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "parking_lots")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ParkingLot {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    private String location;
}
