package com.smartparking.entity;

import com.smartparking.enums.ParkingStatus;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "parking_areas")
public class ParkingArea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String city;

    private String state;
    private String pincode;
    private Double latitude;
    private Double longitude;

    @Column(nullable = false)
    private Integer totalSlots;

    @Column(nullable = false)
    private Integer availableSlots;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal pricePerHour;

    private String supportedVehicleTypes;

    @Column(length = 1000)
    private String description;

    private String facilities;
    private String imageFileName;
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    private ParkingStatus status = ParkingStatus.PENDING_APPROVAL;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @OneToMany(mappedBy = "parkingArea", cascade = CascadeType.ALL)
    private List<Booking> bookings;

    @OneToMany(mappedBy = "parkingArea", cascade = CascadeType.ALL)
    private List<Review> reviews;

    @CreationTimestamp
    private LocalDateTime createdAt;

    public ParkingArea() {}

    // Returns the image to display
    public String getDisplayImage() {
        if (imageFileName != null && !imageFileName.isBlank())
            return "/uploads/" + imageFileName;
        if (imageUrl != null && !imageUrl.isBlank())
            return imageUrl;
        return "/images/default-parking.svg";
    }

    // ── Getters & Setters ─────────────────────────
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public String getPincode() { return pincode; }
    public void setPincode(String pincode) { this.pincode = pincode; }

    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }

    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }

    public Integer getTotalSlots() { return totalSlots; }
    public void setTotalSlots(Integer totalSlots) { this.totalSlots = totalSlots; }

    public Integer getAvailableSlots() { return availableSlots; }
    public void setAvailableSlots(Integer availableSlots) { this.availableSlots = availableSlots; }

    public BigDecimal getPricePerHour() { return pricePerHour; }
    public void setPricePerHour(BigDecimal pricePerHour) { this.pricePerHour = pricePerHour; }

    public String getSupportedVehicleTypes() { return supportedVehicleTypes; }
    public void setSupportedVehicleTypes(String supportedVehicleTypes) { this.supportedVehicleTypes = supportedVehicleTypes; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getFacilities() { return facilities; }
    public void setFacilities(String facilities) { this.facilities = facilities; }

    public String getImageFileName() { return imageFileName; }
    public void setImageFileName(String imageFileName) { this.imageFileName = imageFileName; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public ParkingStatus getStatus() { return status; }
    public void setStatus(ParkingStatus status) { this.status = status; }

    public User getOwner() { return owner; }
    public void setOwner(User owner) { this.owner = owner; }

    public List<Booking> getBookings() { return bookings; }
    public void setBookings(List<Booking> bookings) { this.bookings = bookings; }

    public List<Review> getReviews() { return reviews; }
    public void setReviews(List<Review> reviews) { this.reviews = reviews; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
