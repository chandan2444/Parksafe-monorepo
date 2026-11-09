package com.parking.entryexitservice.event;
import com.parking.entryexitservice.entity.VehicleType;
import java.time.LocalDateTime;
public record SlotAllocationRequestedEvent(
        String ticketNumber,
        String vehicleNumber,
        VehicleType vehicleType,
        Long parkingLotId,
        LocalDateTime entryTime
) {}
