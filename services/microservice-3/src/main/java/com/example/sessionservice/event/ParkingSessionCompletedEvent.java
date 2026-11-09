package com.example.sessionservice.event;
import java.time.LocalDateTime;
public record ParkingSessionCompletedEvent(String ticketNumber, String vehicleNumber,
        Long slotId, LocalDateTime entryTime, LocalDateTime exitTime) {}
