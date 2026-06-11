package com.smartparking.repository;

import com.smartparking.entity.ParkingArea;
import com.smartparking.entity.User;
import com.smartparking.enums.ParkingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ParkingAreaRepository extends JpaRepository<ParkingArea, Long> {
    List<ParkingArea> findByOwner(User owner);
    List<ParkingArea> findByStatus(ParkingStatus status);
    List<ParkingArea> findByCityIgnoreCaseAndStatus(String city, ParkingStatus status);
    List<ParkingArea> findByStatusAndAvailableSlotsGreaterThan(ParkingStatus status, int slots);
}
