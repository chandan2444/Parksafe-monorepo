package com.example.parkingslotservice.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "parking_slots", uniqueConstraints = @UniqueConstraint(columnNames = "slotNumber"))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ParkingSlot {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String slotNumber;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VehicleType vehicleType;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SlotStatus status;
    private Long parkingLotId;
    // Ticket currently occupying this slot; also helps ignore duplicate RabbitMQ allocation messages.
    @Column(unique = true)
    private String assignedTicketNumber;
}
