package com.example.sessionservice.entity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
@Entity
@Table(name="parking_history")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ParkingHistory {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    private String ticketNumber;
    private String vehicleNumber;
    private String action;
    private LocalDateTime actionTime;
    private String details;
}
