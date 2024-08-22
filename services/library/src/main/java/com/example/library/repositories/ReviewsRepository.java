package com.example.library.repositories;

import com.example.library.models.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReviewsRepository extends JpaRepository<Review, Integer> {

    Optional<Review> findByReviewerIdAndReviewedBookId(int reviewerId, int reviewedBookId);

}
