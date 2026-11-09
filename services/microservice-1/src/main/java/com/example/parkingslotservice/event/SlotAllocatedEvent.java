package com.example.parkingslotservice.event;
import java.time.LocalDateTime;
public record SlotAllocatedEvent(String ticketNumber, String vehicleNumber, Long slotId,
        String slotNumber, LocalDateTime allocatedAt) {}
