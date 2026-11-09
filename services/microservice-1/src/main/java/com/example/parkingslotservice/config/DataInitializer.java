package com.example.parkingslotservice.config;

import com.example.parkingslotservice.entity.*;
import com.example.parkingslotservice.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataInitializer {
    @Bean
    CommandLineRunner seedParking(ParkingSlotRepository slots, ParkingLotRepository lots) {
        return args -> {
            if (lots.count() == 0) {
                ParkingLot lot = lots.save(ParkingLot.builder()
                        .name("Mall Main Parking").location("Basement Level B1").build());
                for (int i = 1; i <= 10; i++) {
                    slots.save(ParkingSlot.builder().slotNumber(String.format("C-%03d", i))
                            .vehicleType(VehicleType.CAR).status(SlotStatus.AVAILABLE)
                            .parkingLotId(lot.getId()).build());
                }
                for (int i = 1; i <= 10; i++) {
                    slots.save(ParkingSlot.builder().slotNumber(String.format("B-%03d", i))
                            .vehicleType(VehicleType.BIKE).status(SlotStatus.AVAILABLE)
                            .parkingLotId(lot.getId()).build());
                }
                log.info("Inserted 10 car slots and 10 bike slots");
            }
        };
    }
}
