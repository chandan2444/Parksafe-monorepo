package com.parking.entryexitservice.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String EXCHANGE = "mall.parking.events";

    public static final String SLOT_ALLOCATED_QUEUE =
            "entry.slot-allocated";

    public static final String SLOT_FAILED_QUEUE =
            "entry.slot-allocation-failed";

    public static final String SESSION_COMPLETED_QUEUE =
            "entry.session-completed";

    @Bean
    public TopicExchange parkingExchange() {
        return new TopicExchange(EXCHANGE, true, false);
    }

    @Bean
    public Queue slotAllocatedQueue() {
        return new Queue(SLOT_ALLOCATED_QUEUE, true);
    }

    @Bean
    public Queue slotFailedQueue() {
        return new Queue(SLOT_FAILED_QUEUE, true);
    }

    @Bean
    public Queue sessionCompletedQueue() {
        return new Queue(SESSION_COMPLETED_QUEUE, true);
    }

    @Bean
    public Binding slotAllocatedBinding() {
        return BindingBuilder
                .bind(slotAllocatedQueue())
                .to(parkingExchange())
                .with("slot.allocated");
    }

    @Bean
    public Binding slotFailedBinding() {
        return BindingBuilder
                .bind(slotFailedQueue())
                .to(parkingExchange())
                .with("slot.allocation.failed");
    }

    @Bean
    public Binding sessionCompletedBinding() {
        return BindingBuilder
                .bind(sessionCompletedQueue())
                .to(parkingExchange())
                .with("session.completed");
    }

    @Bean
    public JacksonJsonMessageConverter jacksonJsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(
            ConnectionFactory connectionFactory,
            JacksonJsonMessageConverter converter) {

        RabbitTemplate template =
                new RabbitTemplate(connectionFactory);

        template.setMessageConverter(converter);
        return template;
    }
}