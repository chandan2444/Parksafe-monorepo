package com.example.parkingslotservice.event;
import java.time.LocalDateTime;
public record SlotReleaseRequestedEvent(String ticketNumber, Long slotId, LocalDateTime requestedAt) {}
