package com.parking.entryexitservice;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@SpringBootApplication
public class
EntryExitServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(EntryExitServiceApplication.class, args);
    }
}
