package com.example.parkingslotservice.event;
import com.example.parkingslotservice.entity.VehicleType;
import java.time.LocalDateTime;
public record SlotAllocationRequestedEvent(
        String ticketNumber,
        String vehicleNumber,
        VehicleType vehicleType,
        Long parkingLotId,
        LocalDateTime entryTime
) {
}
