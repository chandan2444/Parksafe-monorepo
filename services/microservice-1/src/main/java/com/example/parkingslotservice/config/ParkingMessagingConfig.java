package com.example.parkingslotservice.config;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class ParkingMessagingConfig {
    @Bean
    public TopicExchange parkingExchange() {
        return new TopicExchange("mall.parking.events");
    }
    @Bean
    public Queue slotAllocationRequestedQueue() {
        return new Queue("parking.slot-allocation.requested", true);
    }
    @Bean
    public Queue slotReleaseRequestedQueue() {
        return new Queue("parking.slot-release.requested", true);
    }
    @Bean
    public Binding allocationBinding() {
        return BindingBuilder
                .bind(slotAllocationRequestedQueue())
                .to(parkingExchange())
                .with("slot.allocation.requested");
    }
    @Bean
    public Binding releaseBinding() {
        return BindingBuilder
                .bind(slotReleaseRequestedQueue())
                .to(parkingExchange())
                .with("slot.release.requested");
    }
}