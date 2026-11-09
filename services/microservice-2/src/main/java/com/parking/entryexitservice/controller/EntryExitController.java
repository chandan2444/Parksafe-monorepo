package com.parking.entryexitservice.controller;

import com.parking.entryexitservice.entity.*;
import com.parking.entryexitservice.service.EntryExitService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/parking")
@RequiredArgsConstructor
public class EntryExitController {
    private final EntryExitService service;

    @PostMapping("/entry")
    public ResponseEntity<ParkingTicket> entry(@Valid @RequestBody EntryRequest request) {
        return ResponseEntity.accepted().body(
                service.vehicleEntry(
                        request.vehicleNumber(),
                        request.vehicleType(),
                        request.ownerName(),
                        request.parkingLotId()
                )
        );
    }

    @PostMapping("/exit/{ticketNumber}")
    public ResponseEntity<ParkingTicket> exit(@PathVariable String ticketNumber) {
        return ResponseEntity.accepted().body(service.requestExit(ticketNumber));
    }

    @GetMapping("/tickets/{ticketNumber}")
    public ParkingTicket getTicket(@PathVariable String ticketNumber) {
        return service.getTicket(ticketNumber);
    }

    public record EntryRequest(
            @NotBlank String vehicleNumber,
            @NotNull VehicleType vehicleType,
            String ownerName,
            @NotNull Long parkingLotId
    ) {}
}
