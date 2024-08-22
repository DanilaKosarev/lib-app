package com.example.library.controllers;

import com.example.library.DTO.*;
import com.example.library.services.BooksService;
import com.example.library.services.PeopleService;
import com.example.library.services.ReviewsService;
import com.example.library.util.Mapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@SecurityRequirement(name = "Authorization")
@Tag(name = "People controller")
@RestController
@RequestMapping("/api/people")
public class PeopleController {

    private final PeopleService peopleService;

    private final ReviewsService reviewsService;

    private final BooksService booksService;

    private final Mapper mapper;

    @Autowired
    public PeopleController(PeopleService peopleService, ReviewsService reviewsService, BooksService booksService, Mapper mapper) {
        this.peopleService = peopleService;
        this.reviewsService = reviewsService;
        this.booksService = booksService;
        this.mapper = mapper;
    }

    @Operation(summary = "Method returns a list of all people")
    @GetMapping
    public List<TinyPersonDTO> findAll() {
        return peopleService.findAllPeople().stream().map(mapper::convertPersonToTinyPersonDTO).collect(Collectors.toList());
    }

    @Operation(summary = "Method returns a person with provided id if it exists")
    @GetMapping("/{id}")
    public PersonDTO findById(@PathVariable int id) {
        return mapper.convertPersonToPersonDTO(peopleService.findPersonById(id));
    }

    @Operation(summary = "Method returns a list of all reviews written by a person with the provided id")
    @GetMapping("/{id}/reviews")
    public List<ReviewDTO> findReviewsByPersonId(@PathVariable int id) {
        return reviewsService.findReviewsByReviewerId(id).stream().map(mapper::convertReviewToReviewDTO).collect(Collectors.toList());
    }

    @Operation(summary = "Method returns a list of all books currently taken by a person with the provided id")
    @GetMapping("/{id}/books")
    public List<BookDTO> findBooksByPersonId(@PathVariable int id){
        return booksService.findAllBooksByOwnerId(id).stream().map(mapper::convertBookToBookDTO).collect(Collectors.toList());
    }

    @Operation(summary = "Method allows to update an existing person")
    @PatchMapping("/{id}")
    public void updatePerson(@PathVariable int id, @RequestBody @Valid UpdatePersonDTO updatePersonDTO) {
        peopleService.updateExistingPerson(mapper.convertPersonDtoToPerson(updatePersonDTO), id);
    }

    @Operation(summary = "Method allows to delete an existing person")
    @DeleteMapping("/{id}")
    public void deletePerson(@PathVariable int id) {
        peopleService.deletePersonById(id);
    }

    @Operation(summary = "Method allows to promote an existing person from user to admin")
    @PatchMapping("/{id}/promote")
    public void promoteToAdmin(@PathVariable int id){
        peopleService.promoteUserToAdmin(id);
    }

    @Operation(summary = "Method allows to demote an existing person from admin to user")
    @PatchMapping("/{id}/demote")
    public void demoteToUser(@PathVariable int id){
        peopleService.demoteAdminToUser(id);
    }
}
