package com.example.library.services;

import com.example.library.exceptions.BookIsAlreadyTakenException;
import com.example.library.exceptions.InappropriateUserException;
import com.example.library.exceptions.NoSuchBookException;
import com.example.library.exceptions.NoSuchPersonException;
import com.example.library.models.Book;
import com.example.library.models.Person;
import com.example.library.repositories.BooksRepository;
import com.example.library.security.PersonDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.ZonedDateTime;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BooksServiceTest {

    private BooksService underTest;

    @Mock
    private BooksRepository booksRepository;

    @Mock
    private PeopleService peopleService;

    @BeforeEach
    void setUp() {
        underTest = new BooksService(booksRepository, peopleService);
    }

    @Test
    void shouldCallFindAllInRepository() {
        // when
        underTest.findAllBooks();

        // then
        verify(booksRepository).findAll();
    }

    @Test
    void shouldFindBookByExistingBookId() {
        // given
        int existingBookId = 1;
        given(booksRepository.findById(existingBookId)).willReturn(Optional.of(mock(Book.class)));

        // when
        underTest.findBookById(existingBookId);

        // then
        ArgumentCaptor<Integer> idArgCaptor = ArgumentCaptor.forClass(Integer.class);

        verify(booksRepository).findById(idArgCaptor.capture());

        int capturedId = idArgCaptor.getValue();
        assertThat(capturedId).isEqualTo(existingBookId);
    }

    @Test
    void shouldThrowForNonExistingBookId() {
        // given
        int nonExistingBookId = -1;

        // when
        // then
        assertThatThrownBy(() -> underTest.findBookById(nonExistingBookId))
                .isInstanceOf(NoSuchBookException.class)
                .hasMessageContaining("Book with id " + nonExistingBookId + " does not exist");
    }

    @Test
    void shouldPassIdToPeopleService(){
        // given
        int existingOwnerId = 1;
        given(peopleService.findPersonById(existingOwnerId)).willReturn(mock(Person.class));

        // when
        underTest.findAllBooksByOwnerId(existingOwnerId);

        // then
        ArgumentCaptor<Integer> idArgCaptor = ArgumentCaptor.forClass(Integer.class);

        verify(peopleService).findPersonById(idArgCaptor.capture());

        int capturedId = idArgCaptor.getValue();
        assertThat(capturedId).isEqualTo(existingOwnerId);
    }

    @Test
    void shouldThrowForNonExistingOwnerId(){
        // given
        int nonExistingOwnerId = -1;
        given(peopleService.findPersonById(nonExistingOwnerId)).willThrow(NoSuchPersonException.class);

        // when
        // then
        assertThatThrownBy(() -> underTest.findAllBooksByOwnerId(nonExistingOwnerId))
                .isInstanceOf(NoSuchPersonException.class);
    }

    @Test
    void shouldSaveNewBook() {
        // given
        Book testBook = mock(Book.class);

        // when
        underTest.saveNewBook(testBook);

        // then
        ArgumentCaptor<Book> bookArgumentCaptor = ArgumentCaptor.forClass(Book.class);

        verify(booksRepository).save(bookArgumentCaptor.capture());

        Book capturedBook = bookArgumentCaptor.getValue();
        assertThat(capturedBook).isEqualTo(testBook);
    }

    @Test
    void shouldUpdateExistingBookWithRightArgs() {
        // given
        int existingBookId = 1;
        String title = "TestBook";
        String author = "TestAuthor";
        int yearOfProduction = 2024;
        Book testBook = new Book(existingBookId, title, author, yearOfProduction, null, null, null);
        Book testBookToUpdate = mock(Book.class);

        given(booksRepository.findById(existingBookId)).willReturn(Optional.of(testBookToUpdate));

        // when
        underTest.updateExistingBook(testBook, existingBookId);

        // then
        ArgumentCaptor<Integer> idArgCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<String> titleArgCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> authorArgCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Integer> yearArgCaptor = ArgumentCaptor.forClass(Integer.class);

        verify(booksRepository).findById(idArgCaptor.capture());
        verify(testBookToUpdate).setTitle(titleArgCaptor.capture());
        verify(testBookToUpdate).setAuthor(authorArgCaptor.capture());
        verify(testBookToUpdate).setYearOfProduction(yearArgCaptor.capture());

        assertThat(idArgCaptor.getValue()).isEqualTo(existingBookId);
        assertThat(titleArgCaptor.getValue()).isEqualTo(title);
        assertThat(authorArgCaptor.getValue()).isEqualTo(author);
        assertThat(yearArgCaptor.getValue()).isEqualTo(yearOfProduction);
    }

    @Test
    void shouldNotUpdateAndThrowForNonExistingBookId() {
        // given
        int nonExistingBookId = -1;
        Book textBook = new Book();
        given(booksRepository.findById(nonExistingBookId)).willThrow(NoSuchBookException.class);

        // when
        // then
        assertThatThrownBy(() -> underTest.updateExistingBook(textBook, nonExistingBookId))
                .isInstanceOf(NoSuchBookException.class);
    }

    @Test
    void shouldDeleteBookByExistingId() {
        // given
        int existingBookId = 1;
        given(booksRepository.findById(existingBookId)).willReturn(Optional.of(mock(Book.class)));

        // when
        underTest.deleteBookById(existingBookId);

        // then
        ArgumentCaptor<Integer> idArgCaptor = ArgumentCaptor.forClass(Integer.class);

        verify(booksRepository).deleteById(idArgCaptor.capture());

        assertThat(idArgCaptor.getValue()).isEqualTo(existingBookId);
    }

    @Test
    void shouldNotDeleteAndThrowForNonExistingBookId() {
        // given
        int nonExistingBookId = -1;
        given(booksRepository.findById(nonExistingBookId)).willThrow(NoSuchBookException.class);

        // when
        // then
        assertThatThrownBy(() -> underTest.deleteBookById(nonExistingBookId))
                .isInstanceOf(NoSuchBookException.class);

        verify(booksRepository, never()).deleteById(nonExistingBookId);
    }

    @Test
    void shouldTakeExistingFreeBookForUserWithNoBooks() {
        // given
        Book testBook = new Book(1, "TestBook", "TestAuthor", 2024, Collections.EMPTY_LIST, null, null);
        Person testPerson = new Person(1, "TestName", 24, "testEmail@mail.com", "testPassword", "ROLE_USER", Collections.EMPTY_LIST, Collections.EMPTY_LIST);

        Authentication auth = mock(Authentication.class);
        SecurityContext context = mock(SecurityContext.class);
        SecurityContextHolder.setContext(context);

        given(booksRepository.findById(testBook.getId())).willReturn(Optional.of(testBook));
        given(SecurityContextHolder.getContext().getAuthentication()).willReturn(auth);
        given(auth.getPrincipal()).willReturn(new PersonDetails(testPerson));
        given(peopleService.findPersonById(testBook.getId())).willReturn(testPerson);

        // when
        underTest.takeBook(testBook.getId());

        // then
        assertThat(testBook.getOwner()).isEqualTo(testPerson);
        assertThat(testBook.getTakenDate()).isCloseTo(new Date(), 100);

        assertThat(testPerson.getBooks().size()).isEqualTo(1);
        assertThat(testPerson.getBooks().contains(testBook)).isEqualTo(true);
    }

    @Test
    void shouldTakeExistingFreeBookForUserWithBooks() {
        // given
        Book testBook = new Book(1, "TestBook", "TestAuthor", 2024, Collections.EMPTY_LIST, null, null);
        Person testPerson = new Person(1, "TestName", 24, "testEmail@mail.com", "testPassword", "ROLE_USER", Collections.EMPTY_LIST, new ArrayList<>(Arrays.asList(mock(Book.class))));
        int countOfBooksBefore = testPerson.getBooks().size();

        Authentication auth = mock(Authentication.class);
        SecurityContext context = mock(SecurityContext.class);
        SecurityContextHolder.setContext(context);

        given(booksRepository.findById(testBook.getId())).willReturn(Optional.of(testBook));
        given(SecurityContextHolder.getContext().getAuthentication()).willReturn(auth);
        given(auth.getPrincipal()).willReturn(new PersonDetails(testPerson));
        given(peopleService.findPersonById(testBook.getId())).willReturn(testPerson);

        // when
        underTest.takeBook(testBook.getId());

        // then
        assertThat(testBook.getOwner()).isEqualTo(testPerson);
        assertThat(testBook.getTakenDate()).isCloseTo(new Date(), 1000);

        assertThat(testPerson.getBooks().size()).isEqualTo(countOfBooksBefore + 1);
        assertThat(testPerson.getBooks().contains(testBook)).isEqualTo(true);
    }

    @Test
    void shouldNotTakeExistingNotFreeBookAndThrow() {
        // given
        Date dateTaken = Date.from(ZonedDateTime.now().minusDays(5).toInstant());
        Book testBook = new Book(1, "TestBook", "TestAuthor", 2024, Collections.EMPTY_LIST, mock(Person.class), dateTaken);
        Person testPerson = new Person(1, "TestName", 24, "testEmail@mail.com", "testPassword", "ROLE_USER", Collections.EMPTY_LIST, Collections.EMPTY_LIST);
        int countOfBooksBefore = testPerson.getBooks().size();

        given(booksRepository.findById(testBook.getId())).willReturn(Optional.of(testBook));

        // when
        // then
        assertThatThrownBy(() -> underTest.takeBook(testBook.getId()))
                .isInstanceOf(BookIsAlreadyTakenException.class)
                .hasMessageContaining("Book with id " + testBook.getId() + " is already taken");

        verify(peopleService, never()).findPersonById(anyInt());


        assertThat(testBook.getOwner()).isNotEqualTo(testPerson);
        assertThat(testBook.getTakenDate()).isCloseTo(Date.from(ZonedDateTime.now().minusDays(5).toInstant()),1000);

        assertThat(testPerson.getBooks().size()).isEqualTo(countOfBooksBefore);
        assertThat(testPerson.getBooks().contains(testBook)).isEqualTo(false);
    }


    @Test
    void shouldNotTakeNonExistingBookAndThrow() {
        // given
        int nonExistingBookId = -1;

        // when
        // then
        assertThatThrownBy(() -> underTest.takeBook(nonExistingBookId))
                .isInstanceOf(NoSuchBookException.class)
                .hasMessageContaining("Book with id " + nonExistingBookId + " does not exist");

    }

    @Test
    void shouldReleaseExistingTakenBook() {
        // given
        Person testOwner = new Person(1, "TestName", 24, "testEmail@mail.com", "testPassword", "ROLE_USER", Collections.EMPTY_LIST, Collections.EMPTY_LIST);
        Date dateTaken = Date.from(ZonedDateTime.now().minusDays(5).toInstant());
        Book testBook = new Book(1, "TestBook", "TestAuthor", 2024, Collections.EMPTY_LIST, testOwner, dateTaken);
        testOwner.setBooks(new ArrayList<>(Arrays.asList(testBook)));
        int countOfBooksBefore = testOwner.getBooks().size();

        Authentication auth = mock(Authentication.class);
        SecurityContext context = mock(SecurityContext.class);
        SecurityContextHolder.setContext(context);

        given(booksRepository.findById(testBook.getId())).willReturn(Optional.of(testBook));
        given(SecurityContextHolder.getContext().getAuthentication()).willReturn(auth);
        given(auth.getPrincipal()).willReturn(new PersonDetails(testOwner));

        // when
        underTest.releaseBook(testBook.getId());

        // then
        assertThat(testOwner.getBooks().contains(testBook)).isEqualTo(false);
        assertThat(testOwner.getBooks().size()).isEqualTo(countOfBooksBefore - 1);

        assertThat(testBook.getOwner()).isEqualTo(null);
        assertThat(testBook.getTakenDate()).isEqualTo((Object)null);
    }

    @Test
    void shouldNotReleaseNonExistingBookAndThrow() {
        // given
        int nonExistingBookId = -1;

        // when
        // then
        assertThatThrownBy(() -> underTest.releaseBook(nonExistingBookId))
                .isInstanceOf(NoSuchBookException.class)
                .hasMessageContaining("Book with id " + nonExistingBookId + " does not exist");
    }

    @Test
    void shouldThrowForInappropriateUser() {
        // given
        Person inappropriateUser = new Person(-1, "TestName2", 24, "testEmail2@mail.com", "testPassword2", "ROLE_USER", Collections.EMPTY_LIST, Collections.EMPTY_LIST);
        Person testOwner = new Person(1, "TestName", 24, "testEmail@mail.com", "testPassword", "ROLE_USER", Collections.EMPTY_LIST, Collections.EMPTY_LIST);
        Date dateTaken = Date.from(ZonedDateTime.now().minusDays(5).toInstant());
        Book testBook = new Book(1, "TestBook", "TestAuthor", 2024, Collections.EMPTY_LIST, testOwner, dateTaken);
        testOwner.setBooks(new ArrayList<>(Arrays.asList(testBook)));
        int countOfBooksBefore = testOwner.getBooks().size();

        Authentication auth = mock(Authentication.class);
        SecurityContext context = mock(SecurityContext.class);
        SecurityContextHolder.setContext(context);

        given(booksRepository.findById(testBook.getId())).willReturn(Optional.of(testBook));
        given(SecurityContextHolder.getContext().getAuthentication()).willReturn(auth);
        given(auth.getPrincipal()).willReturn(new PersonDetails(inappropriateUser));

        // when
        // then
        assertThatThrownBy(() -> underTest.releaseBook(testBook.getId()))
                .isInstanceOf(InappropriateUserException.class)
                .hasMessageContaining("Request from inappropriate user");

        assertThat(testOwner.getBooks().size()).isEqualTo(countOfBooksBefore);
        assertThat(testOwner.getBooks().contains(testBook)).isEqualTo(true);
        assertThat(testBook.getTakenDate()).isEqualTo(dateTaken);
        assertThat(testBook.getOwner()).isEqualTo(testOwner);

    }

    @Test
    void shouldNotAffectExistingFreeBook() {
        // given
        Book testBook = new Book(1, "TestBook", "TestAuthor", 2024, Collections.EMPTY_LIST, null, null);
        given(booksRepository.findById(testBook.getId())).willReturn(Optional.of(testBook));

        // when
        underTest.releaseBook(testBook.getId());

        // then
        assertThat(testBook.getTakenDate()).isEqualTo((Object) null);
        assertThat(testBook.getOwner()).isEqualTo(null);
    }
}