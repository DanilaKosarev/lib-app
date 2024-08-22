package com.example.library.controllers;

import com.example.library.DTO.AuthenticationDTO;
import com.example.library.DTO.RegistrationDTO;
import com.example.library.services.KafkaProducerService;
import com.example.library.models.Person;
import com.example.library.security.JWTUtil;
import com.example.library.services.PeopleService;
import com.example.library.util.Mapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@SecurityRequirement(name = "Authorization")
@Tag(name = "Authentication controller")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final PeopleService peopleService;

    private final JWTUtil jwtUtil;

    private final Mapper mapper;

    private final AuthenticationManager authenticationManager;

    private final KafkaProducerService kafkaProducerService;

    @Autowired
    public AuthController(PeopleService peopleService, JWTUtil jwtUtil, Mapper mapper, AuthenticationManager authenticationManager, KafkaProducerService kafkaProducerService) {
        this.peopleService = peopleService;
        this.jwtUtil = jwtUtil;
        this.mapper = mapper;
        this.authenticationManager = authenticationManager;
        this.kafkaProducerService = kafkaProducerService;
    }

    @Operation(summary = "Method allows to register a new user")
    @PostMapping("/registration")
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, String> performRegistration(@RequestBody @Valid RegistrationDTO registrationDTO){
        Person person = mapper.convertRegistrationDtoToPerson(registrationDTO);

        peopleService.registerNewPerson(person);

        kafkaProducerService.sendRegistrationGreeting(mapper.convertPersonToRegistrationGreetingDTO(person));

        return Map.of("jwt-token", jwtUtil.generateToken(person.getEmail()));
    }

    @Operation(summary = "Method allows to log in for an existing user")
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> performLogin(@RequestBody @Valid AuthenticationDTO authenticationDTO){
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                authenticationDTO.getEmail(),
                authenticationDTO.getPassword()
        );

        try{
            authenticationManager.authenticate(authToken);
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>(Map.of("message", "Incorrect credentials"), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(Map.of("jwt-token", jwtUtil.generateToken(authenticationDTO.getEmail())), HttpStatus.OK);
    }
}
