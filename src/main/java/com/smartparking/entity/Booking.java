package com.smartparking.entity;

import com.smartparking.enums.BookingStatus;
import com.smartparking.enums.PaymentStatus;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String bookingCode;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "parking_area_id", nullable = false)
    private ParkingArea parkingArea;

    @Column(nullable = false)
    private LocalDateTime plannedEntryTime;

    @Column(nullable = false)
    private LocalDateTime plannedExitTime;

    private LocalDateTime actualEntryTime;
    private LocalDateTime actualExitTime;
    private String vehicleNumber;
    private String vehicleType;
    private String slotNumber;

    @Enumerated(EnumType.STRING)
    private BookingStatus status = BookingStatus.PENDING;

    @Column(precision = 10, scale = 2)
    private BigDecimal estimatedAmount;

    @Column(precision = 10, scale = 2)
    private BigDecimal finalAmount;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;

    private String paymentMethod;
    private String transactionId;
    private String rejectionReason;

    @CreationTimestamp
    private LocalDateTime createdAt;

    public Booking() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getBookingCode() { return bookingCode; }
    public void setBookingCode(String bookingCode) { this.bookingCode = bookingCode; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public ParkingArea getParkingArea() { return parkingArea; }
    public void setParkingArea(ParkingArea parkingArea) { this.parkingArea = parkingArea; }

    public LocalDateTime getPlannedEntryTime() { return plannedEntryTime; }
    public void setPlannedEntryTime(LocalDateTime plannedEntryTime) { this.plannedEntryTime = plannedEntryTime; }

    public LocalDateTime getPlannedExitTime() { return plannedExitTime; }
    public void setPlannedExitTime(LocalDateTime plannedExitTime) { this.plannedExitTime = plannedExitTime; }

    public LocalDateTime getActualEntryTime() { return actualEntryTime; }
    public void setActualEntryTime(LocalDateTime actualEntryTime) { this.actualEntryTime = actualEntryTime; }

    public LocalDateTime getActualExitTime() { return actualExitTime; }
    public void setActualExitTime(LocalDateTime actualExitTime) { this.actualExitTime = actualExitTime; }

    public String getVehicleNumber() { return vehicleNumber; }
    public void setVehicleNumber(String vehicleNumber) { this.vehicleNumber = vehicleNumber; }

    public String getVehicleType() { return vehicleType; }
    public void setVehicleType(String vehicleType) { this.vehicleType = vehicleType; }

    public String getSlotNumber() { return slotNumber; }
    public void setSlotNumber(String slotNumber) { this.slotNumber = slotNumber; }

    public BookingStatus getStatus() { return status; }
    public void setStatus(BookingStatus status) { this.status = status; }

    public BigDecimal getEstimatedAmount() { return estimatedAmount; }
    public void setEstimatedAmount(BigDecimal estimatedAmount) { this.estimatedAmount = estimatedAmount; }

    public BigDecimal getFinalAmount() { return finalAmount; }
    public void setFinalAmount(BigDecimal finalAmount) { this.finalAmount = finalAmount; }

    public PaymentStatus getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(PaymentStatus paymentStatus) { this.paymentStatus = paymentStatus; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }

    public String getRejectionReason() { return rejectionReason; }
    public void setRejectionReason(String rejectionReason) { this.rejectionReason = rejectionReason; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
