package com.example.library.DTO;

import java.util.List;

public class PersonDTO {

    private int id;

    private String name;

    private int age;

    private String email;

    private List<ReviewDTO> writtenReviews;

    private List<BookDTO> books;

    public PersonDTO() {
    }

    public PersonDTO(int id, String name, int age, String email, List<ReviewDTO> writtenReviews, List<BookDTO> books) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.email = email;
        this.writtenReviews = writtenReviews;
        this.books = books;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<ReviewDTO> getWrittenReviews() {
        return writtenReviews;
    }

    public void setWrittenReviews(List<ReviewDTO> writtenReviews) {
        this.writtenReviews = writtenReviews;
    }

    public List<BookDTO> getBooks() {
        return books;
    }

    public void setBooks(List<BookDTO> books) {
        this.books = books;
    }
}
