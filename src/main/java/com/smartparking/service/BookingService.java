package com.smartparking.service;

import com.smartparking.entity.*;
import com.smartparking.enums.BookingStatus;
import com.smartparking.enums.PaymentStatus;
import com.smartparking.repository.BookingRepository;
import com.smartparking.repository.ParkingAreaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepo;

    @Autowired
    private ParkingAreaRepository parkingRepo;

    @Transactional
    public Booking create(User user, ParkingArea area, LocalDateTime entry, LocalDateTime exit,
                          String vehicleNumber, String vehicleType) {
        if (area.getAvailableSlots() <= 0)
            throw new RuntimeException("No slots available");

        long active = bookingRepo.countByUserAndStatusIn(user,
                List.of(BookingStatus.PENDING, BookingStatus.CONFIRMED, BookingStatus.ACTIVE));
        if (active >= 3)
            throw new RuntimeException("You already have 3 active bookings");

        long mins = Duration.between(entry, exit).toMinutes();
        if (mins <= 0)
            throw new RuntimeException("Exit time must be after entry time");

        BigDecimal hours = BigDecimal.valueOf(mins).divide(BigDecimal.valueOf(60), 2, RoundingMode.CEILING);
        BigDecimal estimated = area.getPricePerHour().multiply(hours).setScale(2, RoundingMode.CEILING);
        String slot = "S-" + (area.getTotalSlots() - area.getAvailableSlots() + 1);

        area.setAvailableSlots(area.getAvailableSlots() - 1);
        parkingRepo.save(area);

        Booking b = new Booking();
        b.setBookingCode("BK-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))
                + "-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase());
        b.setUser(user);
        b.setParkingArea(area);
        b.setPlannedEntryTime(entry);
        b.setPlannedExitTime(exit);
        b.setVehicleNumber(vehicleNumber != null ? vehicleNumber : user.getVehicleNumber());
        b.setVehicleType(vehicleType != null ? vehicleType : user.getVehicleType());
        b.setSlotNumber(slot);
        b.setEstimatedAmount(estimated);
        b.setStatus(BookingStatus.PENDING);
        b.setPaymentStatus(PaymentStatus.PENDING);
        return bookingRepo.save(b);
    }

    @Transactional
    public Booking cancel(Long id, User user) {
        Booking b = findById(id);
        if (!b.getUser().getId().equals(user.getId()))
            throw new RuntimeException("Not your booking");
        if (!List.of(BookingStatus.PENDING, BookingStatus.CONFIRMED).contains(b.getStatus()))
            throw new RuntimeException("Cannot cancel in status: " + b.getStatus());
        b.setStatus(BookingStatus.CANCELLED);
        b.getParkingArea().setAvailableSlots(b.getParkingArea().getAvailableSlots() + 1);
        parkingRepo.save(b.getParkingArea());
        return bookingRepo.save(b);
    }

    @Transactional
    public Booking confirm(Long id) {
        Booking b = findById(id);
        b.setStatus(BookingStatus.CONFIRMED);
        return bookingRepo.save(b);
    }

    @Transactional
    public Booking reject(Long id, String reason) {
        Booking b = findById(id);
        b.setStatus(BookingStatus.REJECTED);
        b.setRejectionReason(reason);
        b.getParkingArea().setAvailableSlots(b.getParkingArea().getAvailableSlots() + 1);
        parkingRepo.save(b.getParkingArea());
        return bookingRepo.save(b);
    }

    @Transactional
    public Booking recordEntry(Long id) {
        Booking b = findById(id);
        b.setStatus(BookingStatus.ACTIVE);
        b.setActualEntryTime(LocalDateTime.now());
        return bookingRepo.save(b);
    }

    @Transactional
    public Booking recordExit(Long id) {
        Booking b = findById(id);
        LocalDateTime exit = LocalDateTime.now();
        b.setActualExitTime(exit);
        b.setStatus(BookingStatus.COMPLETED);
        long mins = Duration.between(b.getActualEntryTime(), exit).toMinutes();
        BigDecimal hours = BigDecimal.valueOf(mins).divide(BigDecimal.valueOf(60), 2, RoundingMode.CEILING);
        b.setFinalAmount(b.getParkingArea().getPricePerHour().multiply(hours).setScale(2, RoundingMode.CEILING));
        b.getParkingArea().setAvailableSlots(b.getParkingArea().getAvailableSlots() + 1);
        parkingRepo.save(b.getParkingArea());
        return bookingRepo.save(b);
    }

    @Transactional
    public Booking markPaid(Long id) {
        Booking b = findById(id);
        b.setPaymentStatus(PaymentStatus.PAID);
        b.setTransactionId("TXN-" + UUID.randomUUID().toString().substring(0, 12).toUpperCase());
        return bookingRepo.save(b);
    }

    public List<Booking> getUserBookings(User user) {
        return bookingRepo.findByUserOrderByCreatedAtDesc(user);
    }

    public List<Booking> getParkingBookings(ParkingArea area) {
        return bookingRepo.findByParkingAreaOrderByCreatedAtDesc(area);
    }

    public List<Booking> getAll() { return bookingRepo.findAll(); }

    public Booking findById(Long id) {
        return bookingRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found: " + id));
    }

    public BigDecimal getOwnerRevenue(Long ownerId) {
        return bookingRepo.calculateOwnerRevenue(ownerId);
    }

    public BigDecimal getTotalRevenue() {
        return bookingRepo.calculateTotalRevenue();
    }
}
