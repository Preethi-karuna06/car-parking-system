package com.smartparking.service;

import com.smartparking.entity.ParkingArea;
import com.smartparking.entity.Review;
import com.smartparking.entity.User;
import com.smartparking.repository.ParkingAreaRepository;
import com.smartparking.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepo;

    @Autowired
    private ParkingAreaRepository parkingRepo;

    public Review add(User user, Long parkingAreaId, Integer rating, String comment) {
        ParkingArea area = parkingRepo.findById(parkingAreaId)
                .orElseThrow(() -> new RuntimeException("Parking area not found"));

        if (reviewRepo.existsByUserIdAndParkingAreaId(user.getId(), parkingAreaId))
            throw new RuntimeException("You have already reviewed this parking area");

        if (rating < 1 || rating > 5)
            throw new RuntimeException("Rating must be between 1 and 5");

        Review review = new Review();
        review.setUser(user);
        review.setParkingArea(area);
        review.setRating(rating);
        review.setComment(comment);
        return reviewRepo.save(review);
    }

    public List<Review> getByParking(Long parkingAreaId) {
        return reviewRepo.findByParkingAreaIdOrderByCreatedAtDesc(parkingAreaId);
    }

    public Double getAvgRating(Long parkingAreaId) {
        return reviewRepo.getAverageRating(parkingAreaId);
    }

    public Long getCount(Long parkingAreaId) {
        return reviewRepo.getReviewCount(parkingAreaId);
    }

    public boolean hasReviewed(Long userId, Long parkingAreaId) {
        return reviewRepo.existsByUserIdAndParkingAreaId(userId, parkingAreaId);
    }
}
