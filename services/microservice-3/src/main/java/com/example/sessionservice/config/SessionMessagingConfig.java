package com.example.sessionservice.config;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.*;
@Configuration
public class SessionMessagingConfig {
    @Bean TopicExchange sessionExchange() { return new TopicExchange(RabbitConfig.EXCHANGE, true, false); }
    @Bean Queue sessionSlotAllocatedQueue() { return QueueBuilder.durable("session.slot-allocated").build(); }
    @Bean Binding sessionSlotAllocatedBinding(Queue sessionSlotAllocatedQueue, TopicExchange sessionExchange) {
        return BindingBuilder.bind(sessionSlotAllocatedQueue).to(sessionExchange).with("slot.allocated");
    }
    @Bean Queue vehicleExitQueue() { return QueueBuilder.durable("session.vehicle-exit-requested").build(); }
    @Bean Binding vehicleExitBinding(Queue vehicleExitQueue, TopicExchange sessionExchange) {
        return BindingBuilder.bind(vehicleExitQueue).to(sessionExchange).with("vehicle.exit.requested");
    }
}
