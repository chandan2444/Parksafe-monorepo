package com.example.parkingslotservice.service;

import com.example.parkingslotservice.entity.ParkingSlot;
import com.example.parkingslotservice.entity.SlotStatus;
import com.example.parkingslotservice.event.SlotAllocatedEvent;
import com.example.parkingslotservice.event.SlotAllocationFailedEvent;
import com.example.parkingslotservice.event.SlotAllocationRequestedEvent;
import com.example.parkingslotservice.event.SlotReleasedEvent;
import com.example.parkingslotservice.event.SlotReleaseRequestedEvent;
import com.example.parkingslotservice.repository.ParkingSlotRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class ParkingService {

    private final ParkingSlotRepository slotRepository;
    private final ParkingEventPublisher publisher;

    @Transactional
    public void allocate(SlotAllocationRequestedEvent event) {

        var existing =
                slotRepository.findByAssignedTicketNumber(
                        event.ticketNumber());

        if (existing.isPresent()) {

            ParkingSlot assigned = existing.get();

            publisher.slotAllocated(
                    new SlotAllocatedEvent(
                            event.ticketNumber(),
                            event.vehicleNumber(),
                            assigned.getId(),
                            assigned.getSlotNumber(),
                            LocalDateTime.now()
                    )
            );

            log.info(
                    "Duplicate allocation request ignored for ticket {}",
                    event.ticketNumber()
            );

            return;
        }

        var slot =
                slotRepository
                        .findFirstByParkingLotIdAndVehicleTypeAndStatusOrderById(
                                event.parkingLotId(),
                                event.vehicleType(),
                                SlotStatus.AVAILABLE
                        );

        if (slot.isEmpty()) {

            publisher.allocationFailed(
                    new SlotAllocationFailedEvent(
                            event.ticketNumber(),
                            event.vehicleNumber(),
                            "NO_SLOTS_AVAILABLE",
                            LocalDateTime.now()
                    )
            );

            log.warn(
                    "No available slot found for ticket {}",
                     event.ticketNumber()

            );

            return;
        }

        ParkingSlot selected = slot.get();

        selected.setStatus(SlotStatus.OCCUPIED);
        selected.setAssignedTicketNumber(
                event.ticketNumber()
        );

        slotRepository.save(selected);

        log.info(
                "Allocated slot {} to ticket {}",
                selected.getSlotNumber(),
                event.ticketNumber()
        );

        publisher.slotAllocated(
                new SlotAllocatedEvent(
                        event.ticketNumber(),
                        event.vehicleNumber(),
                        selected.getId(),
                        selected.getSlotNumber(),
                        LocalDateTime.now()
                )
        );
    }

    @Transactional
    public void release(SlotReleaseRequestedEvent event) {

        if (event.slotId() == null) {

            log.warn(
                    "Cannot release slot because slotId is null for ticket {}",
                    event.ticketNumber()
            );

            return;
        }

        ParkingSlot slot =
                slotRepository.findById(event.slotId())
                        .orElseThrow(
                                () -> new IllegalArgumentException(
                                        "Slot not found: " + event.slotId()
                                )
                        );

        if (slot.getStatus() == SlotStatus.AVAILABLE) {

            log.info(
                    "Slot {} already available",
                    slot.getSlotNumber()
            );

            return;
        }

        if (!event.ticketNumber()
                .equals(slot.getAssignedTicketNumber())) {

            log.warn(
                    "Ticket {} does not own slot {}",
                    event.ticketNumber(),
                    slot.getSlotNumber()
            );

            return;
        }

        slot.setStatus(SlotStatus.AVAILABLE);
        slot.setAssignedTicketNumber(null);

        slotRepository.save(slot);

        publisher.slotReleased(
                new SlotReleasedEvent(
                        event.ticketNumber(),
                        slot.getId(),
                        slot.getSlotNumber(),
                        LocalDateTime.now()
                )
        );

        log.info(
                "Released slot {} for ticket {}",
                slot.getSlotNumber(),
                event.ticketNumber()
        );
    }
}
