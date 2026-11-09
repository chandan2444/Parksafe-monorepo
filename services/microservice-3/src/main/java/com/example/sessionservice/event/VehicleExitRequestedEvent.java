package com.example.sessionservice.event;
import java.time.LocalDateTime;
public record VehicleExitRequestedEvent(String ticketNumber, String vehicleNumber,
        Long slotId, LocalDateTime exitTime) {}
