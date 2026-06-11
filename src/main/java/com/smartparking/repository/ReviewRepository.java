package com.smartparking.repository;

import com.smartparking.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByParkingAreaIdOrderByCreatedAtDesc(Long parkingAreaId);
    boolean existsByUserIdAndParkingAreaId(Long userId, Long parkingAreaId);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.parkingArea.id = :id")
    Double getAverageRating(@Param("id") Long parkingAreaId);

    @Query("SELECT COUNT(r) FROM Review r WHERE r.parkingArea.id = :id")
    Long getReviewCount(@Param("id") Long parkingAreaId);
}
