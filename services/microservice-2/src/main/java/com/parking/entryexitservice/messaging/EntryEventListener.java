package com.parking.entryexitservice.messaging;
import com.parking.entryexitservice.entity.ParkingTicket;
import com.parking.entryexitservice.entity.TicketStatus;
import com.parking.entryexitservice.event.SlotAllocatedEvent;
import com.parking.entryexitservice.event.SlotAllocationFailedEvent;
import com.parking.entryexitservice.event.ParkingSessionCompletedEvent;
import com.parking.entryexitservice.repository.ParkingTicketRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
@Component
@RequiredArgsConstructor
@Slf4j
public class EntryEventListener {
    private final ParkingTicketRepository ticketRepository;
    // Receive successful allocation
    @RabbitListener(queues = "entry.slot-allocated")
    @Transactional
    public void onSlotAllocated(SlotAllocatedEvent event) {
        log.info("Received slot allocated event for ticket {}",
                event.ticketNumber());
        ticketRepository
                .findByTicketNumber(event.ticketNumber())
                .ifPresent(ticket -> {
                    if (ticket.getStatus() ==
                            TicketStatus.ALLOCATION_PENDING) {
                        ticket.setSlotId(event.slotId());
                        ticket.setSlotNumber(event.slotNumber());
                        ticket.setStatus(TicketStatus.ACTIVE);
                        ticketRepository.save(ticket);
                        log.info("Ticket {} is ACTIVE with slot {}",
                                event.ticketNumber(),
                                event.slotNumber());
                    }
                });
    }
    // Receive allocation failure
    @RabbitListener(queues = "entry.slot-allocation-failed")
    @Transactional
    public void onAllocationFailed(
            SlotAllocationFailedEvent event) {
        log.warn("Received allocation failure for ticket {}",
                event.ticketNumber());
        ticketRepository
                .findByTicketNumber(event.ticketNumber())
                .ifPresent(ticket -> {
                    ticket.setStatus(TicketStatus.NO_SLOT);
                    ticketRepository.save(ticket);
                    log.warn("No slot available for ticket {}",
                            event.ticketNumber());
                });
    }
    // Receive session completion
    @RabbitListener(queues = "entry.session-completed")
    @Transactional
    public void onSessionCompleted(
            ParkingSessionCompletedEvent event) {
        log.info("Received session completed event for ticket {}",
                event.ticketNumber());
        ticketRepository
                .findByTicketNumber(event.ticketNumber())
                .ifPresent(ticket -> {
                    ticket.setStatus(TicketStatus.COMPLETED);
                    ticket.setExitTime(event.exitTime());
                    ticketRepository.save(ticket);
                    log.info("Ticket {} completed",
                            event.ticketNumber());
                });
    }
}