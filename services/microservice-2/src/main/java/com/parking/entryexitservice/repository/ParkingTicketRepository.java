package com.parking.entryexitservice.repository;
import com.parking.entryexitservice.entity.ParkingTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface ParkingTicketRepository extends JpaRepository<ParkingTicket,Long> {
    Optional<ParkingTicket> findByTicketNumber(String ticketNumber);
}
