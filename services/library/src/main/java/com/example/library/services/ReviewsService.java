package com.example.library.services;

import com.example.library.exceptions.InappropriateUserException;
import com.example.library.exceptions.NoSuchReviewException;
import com.example.library.exceptions.ReviewIsAlreadyExistsException;
import com.example.library.models.Review;
import com.example.library.repositories.ReviewsRepository;
import com.example.library.security.PersonDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ReviewsService {

    private final ReviewsRepository reviewsRepository;

    private final BooksService booksService;

    private final PeopleService peopleService;

    @Autowired
    public ReviewsService(ReviewsRepository reviewsRepository, BooksService booksService, PeopleService peopleService) {
        this.reviewsRepository = reviewsRepository;
        this.booksService = booksService;
        this.peopleService = peopleService;
    }

    public List<Review> findAllReviews(){
        return reviewsRepository.findAll();
    }

    public Review findReviewById(int id){
        return reviewsRepository.findById(id).orElseThrow(() -> new NoSuchReviewException("Review with id " + id + " does not exist"));
    }

    public List<Review> findReviewsByBookId(int id){
        return booksService.findBookById(id).getReceivedReviews();
    }

    public List<Review> findReviewsByReviewerId(int id){
        return peopleService.findPersonById(id).getWrittenReviews();
    }

    @Transactional
    public void saveNewReview(Review newReview){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();

        newReview.setReviewer(personDetails.getPerson());
        newReview.setReviewedBook(booksService.findBookById(newReview.getReviewedBook().getId()));

        if(reviewsRepository.findByReviewerIdAndReviewedBookId(newReview.getReviewer().getId(), newReview.getReviewedBook().getId()).isPresent()) {
            throw new ReviewIsAlreadyExistsException("Review from this user for this book already exists");
        }
        newReview.setId(0);
        reviewsRepository.save(newReview);
    }

    @Transactional
    public void updateExistingReview(Review updatedReview, int id){
        Review reviewToUpdate = reviewsRepository.findById(id).orElseThrow(() -> new NoSuchReviewException("Review with id " + id + " does not exist"));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();

        if(reviewToUpdate.getReviewer().getId() != personDetails.getPerson().getId())
            throw new InappropriateUserException("Request from inappropriate user");

        reviewToUpdate.setScore(updatedReview.getScore());
        reviewToUpdate.setComment(updatedReview.getComment());
    }

    @Transactional
    public void deleteReviewById(int id){
        Review reviewToDelete = reviewsRepository.findById(id).orElseThrow(() -> new NoSuchReviewException("Review with id " + id + " does not exist"));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();

        if(reviewToDelete.getReviewer().getId() != personDetails.getPerson().getId())
            throw new InappropriateUserException("Request from inappropriate user");

        reviewsRepository.deleteById(id);
    }
}
