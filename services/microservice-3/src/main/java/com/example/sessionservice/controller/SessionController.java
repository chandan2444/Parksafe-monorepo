package com.example.sessionservice.controller;
import com.example.sessionservice.entity.*;
import com.example.sessionservice.service.ParkingSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController @CrossOrigin(origins = "*") @RequestMapping("/api/sessions") @RequiredArgsConstructor
public class SessionController {
    private final ParkingSessionService service;
    @GetMapping public List<ParkingSession> getAllSessions()
    {
        return service.getAll();
    }
    @GetMapping("/{ticketNumber}/history")
    public List<ParkingHistory> getHistory(@PathVariable String ticketNumber) {
        return service.getHistory(ticketNumber);
    }
}

