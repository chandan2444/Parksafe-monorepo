package com.parking.entryexitservice.event;
import java.time.LocalDateTime;
public record ParkingSessionCompletedEvent(String ticketNumber, String vehicleNumber,
        Long slotId, LocalDateTime entryTime, LocalDateTime exitTime) {}
