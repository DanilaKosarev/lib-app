package com.example.library.util;

import com.example.library.DTO.*;
import com.example.library.models.Book;
import com.example.library.models.Person;
import com.example.library.models.Review;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Mapper {

    private final ModelMapper modelMapper;

    @Autowired
    public Mapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Review convertReviewDtoToReview(ReviewDTO reviewDTO) {
        return modelMapper.map(reviewDTO, Review.class);
    }

    public ReviewDTO convertReviewToReviewDTO(Review review) {
        return modelMapper.map(review, ReviewDTO.class);
    }

    public Person convertRegistrationDtoToPerson(RegistrationDTO registrationDTO){
        return modelMapper.map(registrationDTO, Person.class);
    }

    public RegistrationGreetingDTO convertPersonToRegistrationGreetingDTO(Person person){
        return modelMapper.map(person, RegistrationGreetingDTO.class);
    }

    public PersonDTO convertPersonToPersonDTO(Person person) {
        return modelMapper.map(person, PersonDTO.class);
    }

    public Person convertPersonDtoToPerson(UpdatePersonDTO updatePersonDTO) {
        return modelMapper.map(updatePersonDTO, Person.class);
    }

    public BookDTO convertBookToBookDTO(Book book) {
        return modelMapper.map(book, BookDTO.class);
    }

    public Book convertBookDtoToBook(BookDTO bookDTO) {
        return modelMapper.map(bookDTO, Book.class);
    }

    public TinyPersonDTO convertPersonToTinyPersonDTO(Person person) { return modelMapper.map(person, TinyPersonDTO.class);}
}
