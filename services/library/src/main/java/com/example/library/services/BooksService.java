package com.example.library.services;

import com.example.library.exceptions.BookIsAlreadyTakenException;
import com.example.library.exceptions.InappropriateUserException;
import com.example.library.exceptions.NoSuchBookException;
import com.example.library.models.Book;
import com.example.library.models.Person;
import com.example.library.repositories.BooksRepository;
import com.example.library.security.PersonDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class BooksService {

    private final BooksRepository booksRepository;

    private final PeopleService peopleService;

    @Autowired
    public BooksService(BooksRepository bookRepository, PeopleService peopleService) {
        this.booksRepository = bookRepository;
        this.peopleService = peopleService;
    }

    public List<Book> findAllBooks(){
        return booksRepository.findAll();
    }

    public Book findBookById(int id) {
        return booksRepository.findById(id).orElseThrow(() -> new NoSuchBookException("Book with id " + id + " does not exist"));
    }

    public List<Book> findAllBooksByOwnerId(int ownerId){
        return peopleService.findPersonById(ownerId).getBooks();
    }

    @Transactional
    public void saveNewBook(Book newBook){
        newBook.setId(0);
        booksRepository.save(newBook);
    }

    @Transactional
    public void updateExistingBook(Book updatedBook, int id){
        Book bookToUpdate = booksRepository.findById(id).orElseThrow(() -> new NoSuchBookException("Book with id " + id + " does not exist"));

        bookToUpdate.setTitle(updatedBook.getTitle());
        bookToUpdate.setAuthor(updatedBook.getAuthor());
        bookToUpdate.setYearOfProduction(updatedBook.getYearOfProduction());
    }

    @Transactional
    public void deleteBookById(int id){
        booksRepository.findById(id).orElseThrow(() -> new NoSuchBookException("Book with id " + id + " does not exist"));
        booksRepository.deleteById(id);
    }

    @Transactional
    public void takeBook(int id){
        Book bookToTake = booksRepository.findById(id).orElseThrow(() -> new NoSuchBookException("Book with id " + id + " does not exist"));

        if(bookToTake.getOwner() != null)
            throw new BookIsAlreadyTakenException("Book with id " + id + " is already taken");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();

        Person newOwner = peopleService.findPersonById(personDetails.getPerson().getId());

        bookToTake.setOwner(newOwner);
        bookToTake.setTakenDate(new Date());

        if(newOwner.getBooks().isEmpty())
            newOwner.setBooks(Collections.singletonList(bookToTake));
        else
            newOwner.getBooks().add(bookToTake);
    }

    @Transactional
    public void releaseBook(int bookId){
        Book bookToRelease = booksRepository.findById(bookId).orElseThrow(() -> new NoSuchBookException("Book with id " + bookId + " does not exist."));
        Person owner = bookToRelease.getOwner();

        if(owner != null) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();

            if(owner.getId() != personDetails.getPerson().getId())
                throw new InappropriateUserException("Request from inappropriate user");

            owner.getBooks().remove(bookToRelease);
            bookToRelease.setOwner(null);
            bookToRelease.setTakenDate(null);
        }
    }
}
