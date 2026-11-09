package com.parking.entryexitservice.event;
import java.time.LocalDateTime;
public record SlotAllocationFailedEvent(String ticketNumber, String vehicleNumber,
        String reason, LocalDateTime failedAt) {}
