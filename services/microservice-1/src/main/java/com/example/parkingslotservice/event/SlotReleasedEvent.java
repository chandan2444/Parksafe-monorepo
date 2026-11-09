package com.example.parkingslotservice.event;
import java.time.LocalDateTime;
public record SlotReleasedEvent(String ticketNumber, Long slotId, String slotNumber,
        LocalDateTime releasedAt) {}
