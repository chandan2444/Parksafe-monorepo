package com.parking.entryexitservice.service;

import com.parking.entryexitservice.entity.*;
import com.parking.entryexitservice.event.*;
import com.parking.entryexitservice.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.UUID;

@Service @RequiredArgsConstructor @Slf4j
public class EntryExitService {
    private final VehicleRepository vehicleRepository;
    private final ParkingTicketRepository ticketRepository;
    private final EntryEventPublisher publisher;

    @Transactional
    public ParkingTicket vehicleEntry(
            String vehicleNumber,
            VehicleType vehicleType,
            String ownerName,
            Long parkingLotId) {
        Vehicle vehicle = vehicleRepository.findByVehicleNumber(vehicleNumber)
                .orElseGet(() -> vehicleRepository.save(Vehicle.builder()
                        .vehicleNumber(vehicleNumber).vehicleType(vehicleType).ownerName(ownerName).build()));

        String ticketNumber = "MALL-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        LocalDateTime now = LocalDateTime.now();
        ParkingTicket ticket = ticketRepository.save(ParkingTicket.builder()
                .ticketNumber(ticketNumber).vehicleNumber(vehicle.getVehicleNumber())
                .vehicleType(vehicleType).status(TicketStatus.ALLOCATION_PENDING)
                .entryTime(now).build());

        publisher.requestSlot(
                new SlotAllocationRequestedEvent(
                        ticketNumber,
                        vehicleNumber,
                        vehicleType,
                        parkingLotId,
                        now
                )
        );
        log.info("Vehicle {} entered; ticket {} created", vehicleNumber, ticketNumber);
        return ticket;
    }

    @Transactional
    public ParkingTicket requestExit(String ticketNumber) {
        ParkingTicket ticket = ticketRepository.findByTicketNumber(ticketNumber)
                .orElseThrow(() -> new IllegalArgumentException("Ticket not found: " + ticketNumber));
        if (ticket.getStatus() != TicketStatus.ACTIVE) {
            throw new IllegalStateException("Only ACTIVE tickets can exit. Current status: " + ticket.getStatus());
        }
        ticket.setStatus(TicketStatus.EXIT_PENDING);
        ticket.setExitTime(LocalDateTime.now());
        ticketRepository.save(ticket);
        publisher.requestExit(new VehicleExitRequestedEvent(ticketNumber, ticket.getVehicleNumber(),
                ticket.getSlotId(), ticket.getExitTime()));
        log.info("Exit requested for ticket {}", ticketNumber);
        return ticket;
    }

    public ParkingTicket getTicket(String ticketNumber) {
        return ticketRepository.findByTicketNumber(ticketNumber)
                .orElseThrow(() -> new IllegalArgumentException("Ticket not found: " + ticketNumber));
    }
}
