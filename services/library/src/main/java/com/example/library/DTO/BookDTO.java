package com.example.library.DTO;

import jakarta.validation.constraints.*;

public class BookDTO {

    private int id;

    @NotBlank(message = "Title should not be null or empty")
    @Size(max = 100, message = "Title should be less than 100 characters")
    private String title;

    @NotBlank(message = "Author name should not be null or empty")
    @Size(max = 100, message = "Author name should be less than 100 characters")
    private String author;

    @Min(value = 1501, message = "Year of productions should be greater than 1500")
    private int yearOfProduction;

    public BookDTO() {
    }

    public BookDTO(int id, String title, String author, int yearOfProduction) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.yearOfProduction = yearOfProduction;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getYearOfProduction() {
        return yearOfProduction;
    }

    public void setYearOfProduction(int yearOfProduction) {
        this.yearOfProduction = yearOfProduction;
    }

}
