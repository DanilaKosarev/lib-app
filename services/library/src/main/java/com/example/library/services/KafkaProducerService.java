package com.example.library.services;

import com.example.library.DTO.RegistrationGreetingDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    public KafkaProducerService(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendRegistrationGreeting(RegistrationGreetingDTO registrationGreetingDTO){
        kafkaTemplate.send("registration_greeting_topic", registrationGreetingDTO);
    }

}
