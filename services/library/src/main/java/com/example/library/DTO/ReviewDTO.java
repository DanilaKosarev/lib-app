package com.example.library.DTO;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public class ReviewDTO {

    private int id;

    private int reviewedBookId;

    private int reviewerId;

    @Min(value = 1, message = "Score should be 1 or greater")
    @Max(value = 5, message = "Score should be 5 or less")
    private int score;

    @Size(max = 255, message = "Comment should be under 300 characters")
    private String comment;

    public ReviewDTO() {
    }

    public ReviewDTO(int id, int reviewedBookId, int reviewerId, int score, String comment) {
        this.id = id;
        this.reviewedBookId = reviewedBookId;
        this.reviewerId = reviewerId;
        this.score = score;
        this.comment = comment;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getReviewedBookId() {
        return reviewedBookId;
    }

    public void setReviewedBookId(int reviewedBookId) {
        this.reviewedBookId = reviewedBookId;
    }

    public int getReviewerId() {
        return reviewerId;
    }

    public void setReviewerId(int reviewerId) {
        this.reviewerId = reviewerId;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
