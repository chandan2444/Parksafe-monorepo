package com.parking.entryexitservice.entity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
@Entity
@Table(name="parking_tickets", uniqueConstraints=@UniqueConstraint(columnNames="ticketNumber"))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ParkingTicket {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(nullable=false, unique=true) private String ticketNumber;
    @Column(nullable=false) private String vehicleNumber;
    @Enumerated(EnumType.STRING) @Column(nullable=false) private VehicleType vehicleType;
    @Enumerated(EnumType.STRING) @Column(nullable=false) private TicketStatus status;
    private Long parkingLotId;
    private Long slotId;
    private String slotNumber;
    private LocalDateTime entryTime;
    private LocalDateTime exitTime;
}
