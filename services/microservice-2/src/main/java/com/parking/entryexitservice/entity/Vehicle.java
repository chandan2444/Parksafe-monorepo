package com.parking.entryexitservice.entity;
import jakarta.persistence.*;
import lombok.*;
@Entity
@Table(name="vehicles", uniqueConstraints=@UniqueConstraint(columnNames="vehicleNumber"))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Vehicle {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(nullable=false, unique=true) private String vehicleNumber;

    @Enumerated(EnumType.STRING) @Column(nullable=false) private VehicleType vehicleType;
    private String ownerName;
    @Column(name = "parking_lot_id")
    private Long parkingLotId;
}
