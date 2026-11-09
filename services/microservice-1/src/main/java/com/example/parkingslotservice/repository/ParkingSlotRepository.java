package com.example.parkingslotservice.repository;

import com.example.parkingslotservice.entity.ParkingSlot;
import com.example.parkingslotservice.entity.SlotStatus;
import com.example.parkingslotservice.entity.VehicleType;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.Optional;

public interface ParkingSlotRepository extends JpaRepository<ParkingSlot, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<ParkingSlot>
    findFirstByParkingLotIdAndVehicleTypeAndStatusOrderById(
            Long parkingLotId,
            VehicleType vehicleType,
            SlotStatus status
    );

    Optional<ParkingSlot> findByAssignedTicketNumber(String ticketNumber);

    long countByStatus(SlotStatus status);
}
