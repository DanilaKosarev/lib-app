package com.example.library.services;

import com.example.library.exceptions.InappropriateUserException;
import com.example.library.exceptions.NoSuchPersonException;
import com.example.library.models.Book;
import com.example.library.models.Person;
import com.example.library.repositories.PeopleRepository;
import com.example.library.security.PersonDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class PeopleService {

    private final PeopleRepository peopleRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public PeopleService(PeopleRepository peopleRepository, PasswordEncoder passwordEncoder) {
        this.peopleRepository = peopleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Person> findAllPeople(){
        return peopleRepository.findAll();
    }

    public Person findPersonById(int id){
        return peopleRepository.findById(id).orElseThrow(() -> new NoSuchPersonException("Person with id " + id + " does not exist"));
    }

    public boolean isEmailOccupied(String email){
        return peopleRepository.findByEmail(email).isPresent();
    }

    @Transactional
    public void registerNewPerson(Person person){
        person.setRole("ROLE_USER");
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        peopleRepository.save(person);
    }

    @Transactional
    public void updateExistingPerson(Person updatedPerson, int id){
        Person personToUpdate = peopleRepository.findById(id).orElseThrow(() -> new NoSuchPersonException("Person with id " + id + " does not exist"));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();

        if(id != personDetails.getPerson().getId())
            throw new InappropriateUserException("Request from inappropriate user");

        personToUpdate.setName(updatedPerson.getName());
        personToUpdate.setAge(updatedPerson.getAge());
    }

    @Transactional
    public void promoteUserToAdmin(int id){
        Person userToPromote = peopleRepository.findById(id).orElseThrow(() -> new NoSuchPersonException("Person with id " + id + " does not exist"));
        userToPromote.setRole("ROLE_ADMIN");
    }

    @Transactional
    public void demoteAdminToUser(int id){
        Person adminToDemote = peopleRepository.findById(id).orElseThrow(() -> new NoSuchPersonException("Person with id " + id + " does not exist"));
        adminToDemote.setRole("ROLE_USER");
    }

    @Transactional
    public void deletePersonById(int id){
        Person personToDelete = peopleRepository.findById(id).orElseThrow(() -> new NoSuchPersonException("Person with id " + id + " does not exist"));

        for(Book book: personToDelete.getBooks()){
            book.setTakenDate(null);
        }

        peopleRepository.deleteById(id);
    }
}
