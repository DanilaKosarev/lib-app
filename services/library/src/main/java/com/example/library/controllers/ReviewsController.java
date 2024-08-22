package com.example.library.controllers;

import com.example.library.DTO.ReviewDTO;
import com.example.library.services.ReviewsService;
import com.example.library.util.Mapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@SecurityRequirement(name = "Authorization")
@Tag(name = "Reviews controller")
@RestController
@RequestMapping("/api/reviews")
public class ReviewsController {

    private final Mapper mapper;

    private final ReviewsService reviewsService;

    @Autowired
    public ReviewsController(Mapper mapper, ReviewsService reviewsService) {
        this.mapper = mapper;
        this.reviewsService = reviewsService;
    }

    @Operation(summary = "Method returns a list of all reviews")
    @GetMapping
    public List<ReviewDTO> findAll() {
        return reviewsService.findAllReviews().stream().map(mapper::convertReviewToReviewDTO).collect(Collectors.toList());
    }

    @Operation(summary = "Method returns a review with the provided id if it exists")
    @GetMapping("/{id}")
    public ReviewDTO findById(@PathVariable int id) {
        return mapper.convertReviewToReviewDTO(reviewsService.findReviewById(id));
    }

    @Operation(summary = "Method allows to create a new review")
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public void createReview(@RequestBody @Valid ReviewDTO reviewDTO) {
        reviewsService.saveNewReview(mapper.convertReviewDtoToReview(reviewDTO));
    }

    @Operation(summary = "Method allows to update an existing review")
    @PatchMapping("/{id}")
    public void updateReview(@PathVariable int id, @RequestBody @Valid ReviewDTO reviewDTO) {
        reviewsService.updateExistingReview(mapper.convertReviewDtoToReview(reviewDTO), id);
    }

    @Operation(summary = "Method allows to delete an existing review")
    @DeleteMapping("/{id}")
    public void deleteReview(@PathVariable int id) {
        reviewsService.deleteReviewById(id);
    }
}
