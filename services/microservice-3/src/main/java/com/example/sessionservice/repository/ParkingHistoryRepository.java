package com.example.sessionservice.repository;
import com.example.sessionservice.entity.ParkingHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface ParkingHistoryRepository extends JpaRepository<ParkingHistory,Long> {
    List<ParkingHistory> findByTicketNumberOrderByActionTimeAsc(String ticketNumber);
}
