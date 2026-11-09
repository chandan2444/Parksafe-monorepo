package com.example.sessionservice.entity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
@Entity
@Table(name="parking_sessions", uniqueConstraints=@UniqueConstraint(columnNames="ticketNumber"))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ParkingSession {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(nullable=false, unique=true) private String ticketNumber;
    @Column(nullable=false) private String vehicleNumber;
    private Long slotId;
    private String slotNumber;
    private LocalDateTime entryTime;
    private LocalDateTime exitTime;
    @Enumerated(EnumType.STRING) @Column(nullable=false) private SessionStatus status;
}
