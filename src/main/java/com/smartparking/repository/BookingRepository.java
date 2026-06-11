package com.smartparking.repository;

import com.smartparking.entity.Booking;
import com.smartparking.entity.ParkingArea;
import com.smartparking.entity.User;
import com.smartparking.enums.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUserOrderByCreatedAtDesc(User user);
    List<Booking> findByParkingAreaOrderByCreatedAtDesc(ParkingArea area);
    long countByUserAndStatusIn(User user, List<BookingStatus> statuses);

    @Query("SELECT COALESCE(SUM(b.finalAmount),0) FROM Booking b WHERE b.parkingArea.owner.id=:ownerId AND b.status='COMPLETED' AND b.paymentStatus='PAID'")
    BigDecimal calculateOwnerRevenue(@Param("ownerId") Long ownerId);

    @Query("SELECT COALESCE(SUM(b.finalAmount),0) FROM Booking b WHERE b.status='COMPLETED' AND b.paymentStatus='PAID'")
    BigDecimal calculateTotalRevenue();
}
