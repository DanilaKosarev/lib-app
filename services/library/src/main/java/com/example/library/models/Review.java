package com.example.library.models;

import jakarta.persistence.*;

@Entity
@Table(name = "review")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "person_id", referencedColumnName = "id")
    private Person reviewer;

    @ManyToOne
    @JoinColumn(name = "book_id", referencedColumnName = "id")
    private Book reviewedBook;

    @Column(name = "score")
    private int score;

    @Column(name = "comment")
    private String comment;

    public Review() {
    }

    public Review(int id, Person reviewer, Book reviewedBook, int score, String comment) {
        this.id = id;
        this.reviewer = reviewer;
        this.reviewedBook = reviewedBook;
        this.score = score;
        this.comment = comment;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Person getReviewer() {
        return reviewer;
    }

    public void setReviewer(Person reviewer) {
        this.reviewer = reviewer;
    }

    public Book getReviewedBook() {
        return reviewedBook;
    }

    public void setReviewedBook(Book reviewedBook) {
        this.reviewedBook = reviewedBook;
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
