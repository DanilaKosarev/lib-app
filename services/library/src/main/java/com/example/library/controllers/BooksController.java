package com.example.library.controllers;

import com.example.library.DTO.BookDTO;
import com.example.library.DTO.ReviewDTO;
import com.example.library.DTO.TinyPersonDTO;
import com.example.library.services.BooksService;
import com.example.library.services.PeopleService;
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
@Tag(name = "Books controller")
@RestController
@RequestMapping("/api/books")
public class BooksController {

    private final BooksService booksService;

    private final ReviewsService reviewsService;

    private final Mapper mapper;

    @Autowired
    public BooksController(BooksService booksService, ReviewsService reviewsService, Mapper mapper) {
        this.booksService = booksService;
        this.reviewsService = reviewsService;
        this.mapper = mapper;
    }

    @Operation(summary = "Method returns a list of all books")
    @GetMapping
    public List<BookDTO> findAll() {
        return booksService.findAllBooks().stream().map(mapper::convertBookToBookDTO).collect(Collectors.toList());
    }

    @Operation(summary = "Method returns a book with the provided id if it exists")
    @GetMapping("/{id}")
    public BookDTO findById(@PathVariable int id) {
        return mapper.convertBookToBookDTO(booksService.findBookById(id));
    }

    @Operation(summary = "Method returns a list of all reviews for a book with the provided id")
    @GetMapping("/{id}/reviews")
    public List<ReviewDTO> findReviewsByBookId(@PathVariable int id) {
        return reviewsService.findReviewsByBookId(id).stream().map(mapper::convertReviewToReviewDTO).collect(Collectors.toList());
    }

    @Operation(summary = "Method allows to create a new book")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createBook(@RequestBody @Valid BookDTO bookDTO) {
        booksService.saveNewBook(mapper.convertBookDtoToBook(bookDTO));
    }

    @Operation(summary = "Method allows to update an existing book")
    @PatchMapping("/{id}")
    public void updateBook(@PathVariable int id, @RequestBody @Valid BookDTO bookDTO) {
        booksService.updateExistingBook(mapper.convertBookDtoToBook(bookDTO), id);
    }

    @Operation(summary = "Method allows to delete an existing book")
    @DeleteMapping("/{id}")
    public void deleteBook(@PathVariable int id) {
        booksService.deleteBookById(id);
    }

    @Operation(summary = "Method allows to claim a free book")
    @PatchMapping("/{id}/claim")
    public void takeBook(@PathVariable("id") int bookId) {
        booksService.takeBook(bookId);
    }

    @Operation(summary = "Method allows to release a claimed book")
    @PatchMapping("/{id}/release")
    public void releaseBook(@PathVariable("id") int bookId) {
        booksService.releaseBook(bookId);
    }
}
