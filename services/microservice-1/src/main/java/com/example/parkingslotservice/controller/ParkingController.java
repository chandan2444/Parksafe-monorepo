package com.example.parkingslotservice.controller;

import com.example.parkingslotservice.entity.*;
import com.example.parkingslotservice.repository.ParkingSlotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/parking/slots")
@RequiredArgsConstructor
public class ParkingController {
    private final ParkingSlotRepository slotRepository;

    @GetMapping
    public List<ParkingSlot> getAllSlots() { return slotRepository.findAll(); }

    @GetMapping("/available")
    public List<ParkingSlot> getAvailableSlots() {
        return slotRepository.findAll().stream()
                .filter(s -> s.getStatus() == SlotStatus.AVAILABLE).toList();
    }
}
