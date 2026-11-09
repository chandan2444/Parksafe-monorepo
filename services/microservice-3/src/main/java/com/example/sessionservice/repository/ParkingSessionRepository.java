package com.example.sessionservice.repository;
import com.example.sessionservice.entity.ParkingSession;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface ParkingSessionRepository extends JpaRepository<ParkingSession,Long> {
    Optional<ParkingSession> findByTicketNumber(String ticketNumber);
}
