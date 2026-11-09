package com.example.parkingslotservice.repository;
import com.example.parkingslotservice.entity.ParkingLot;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ParkingLotRepository extends JpaRepository<ParkingLot, Long> {}
