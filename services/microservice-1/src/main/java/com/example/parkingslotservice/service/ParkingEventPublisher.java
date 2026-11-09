package com.example.parkingslotservice.service;

import com.example.parkingslotservice.config.RabbitConfig;
import com.example.parkingslotservice.event.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ParkingEventPublisher {
    private final RabbitTemplate rabbitTemplate;

    public void slotAllocated(SlotAllocatedEvent event) {
        log.info("Publishing SlotAllocatedEvent for ticket {}", event.ticketNumber());
        rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE, "slot.allocated", event);
    }

    public void allocationFailed(SlotAllocationFailedEvent event) {
        log.warn("Publishing SlotAllocationFailedEvent for ticket {}", event.ticketNumber());
        rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE, "slot.allocation.failed", event);
    }

    public void slotReleased(SlotReleasedEvent event) {
        log.info("Publishing SlotReleasedEvent for ticket {}", event.ticketNumber());
        rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE, "slot.released", event);
    }
}
