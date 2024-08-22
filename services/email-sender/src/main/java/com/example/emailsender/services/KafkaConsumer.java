package com.example.emailsender.services;

import com.example.emailsender.DTO.BookExpiredDTO;
import com.example.emailsender.DTO.RegistrationGreetingDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer {

    private final MailSenderService mailSender;

    @Autowired
    public KafkaConsumer(MailSenderService mailSender) {
        this.mailSender = mailSender;
    }

    @KafkaListener(topics = "registration_greeting_topic", groupId = "group-1")
    public void listenerToRegistrationsGreetings(RegistrationGreetingDTO registrationGreetingDTO){

        mailSender.send(
                registrationGreetingDTO.getEmail(),
                "Registration greeting",
                "Hello, " + registrationGreetingDTO.getName() + "! Thank you for your registration."
        );

    }

    @KafkaListener(topics = "book_expiration_topic", groupId = "group-1")
    public void listenerToBookExpiredNotification(BookExpiredDTO bookExpiredDTO){

        mailSender.send(
                bookExpiredDTO.getOwnerEmail(),
                "Expiration notification",
                "Your book " + bookExpiredDTO.getTitle() + " is expired. Please return the book to the library."
        );

    }
}
