package com.example.sessionservice.service;
import com.example.sessionservice.config.RabbitConfig;
import com.example.sessionservice.event.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
@Component @RequiredArgsConstructor @Slf4j
public class SessionEventPublisher {
    private final RabbitTemplate rabbitTemplate;
    public void sessionCompleted(ParkingSessionCompletedEvent event) {
        log.info("Publishing ParkingSessionCompletedEvent for {}", event.ticketNumber());
        rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE, "session.completed", event);
    }
    public void releaseSlot(SlotReleaseRequestedEvent event) {
        log.info("Publishing SlotReleaseRequestedEvent for {}", event.ticketNumber());
        rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE, "slot.release.requested", event);
    }
}
