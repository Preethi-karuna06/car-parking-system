package com.smartparking.service;

import com.smartparking.entity.ParkingArea;
import com.smartparking.entity.User;
import com.smartparking.enums.ParkingStatus;
import com.smartparking.repository.ParkingAreaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ParkingAreaService {

    @Autowired
    private ParkingAreaRepository repo;

    public ParkingArea add(String name, String address, String city, String state,
                            String pincode, Double lat, Double lng, Integer totalSlots,
                            BigDecimal pricePerHour, String vehicleTypes, String description,
                            String facilities, String imageFileName, String imageUrl, User owner) {
        ParkingArea p = new ParkingArea();
        p.setName(name);
        p.setAddress(address);
        p.setCity(city);
        p.setState(state);
        p.setPincode(pincode);
        p.setLatitude(lat);
        p.setLongitude(lng);
        p.setTotalSlots(totalSlots);
        p.setAvailableSlots(totalSlots);
        p.setPricePerHour(pricePerHour);
        p.setSupportedVehicleTypes(vehicleTypes);
        p.setDescription(description);
        p.setFacilities(facilities);
        p.setImageFileName(imageFileName);
        p.setImageUrl(imageUrl);
        p.setOwner(owner);
        p.setStatus(ParkingStatus.PENDING_APPROVAL);
        return repo.save(p);
    }

    public List<ParkingArea> getByOwner(User owner) { return repo.findByOwner(owner); }

    public List<ParkingArea> searchByCity(String city) {
        return repo.findByCityIgnoreCaseAndStatus(city, ParkingStatus.ACTIVE);
    }

    public List<ParkingArea> getAllActive() {
        return repo.findByStatusAndAvailableSlotsGreaterThan(ParkingStatus.ACTIVE, 0);
    }

    public List<ParkingArea> getAll() { return repo.findAll(); }

    public List<ParkingArea> getPending() {
        return repo.findByStatus(ParkingStatus.PENDING_APPROVAL);
    }

    public ParkingArea findById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Parking area not found: " + id));
    }

    public ParkingArea approve(Long id) {
        ParkingArea p = findById(id);
        p.setStatus(ParkingStatus.ACTIVE);
        return repo.save(p);
    }

    public ParkingArea suspend(Long id) {
        ParkingArea p = findById(id);
        p.setStatus(ParkingStatus.SUSPENDED);
        return repo.save(p);
    }

    public ParkingArea toggleStatus(Long id, User owner) {
        ParkingArea p = findById(id);
        if (!p.getOwner().getId().equals(owner.getId()))
            throw new RuntimeException("Not your parking area");
        p.setStatus(p.getStatus() == ParkingStatus.ACTIVE ? ParkingStatus.INACTIVE : ParkingStatus.ACTIVE);
        return repo.save(p);
    }

    public void save(ParkingArea p) { repo.save(p); }
}
