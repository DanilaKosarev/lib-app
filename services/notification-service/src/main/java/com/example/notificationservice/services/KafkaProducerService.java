package com.example.notificationservice.services;

import com.example.notificationservice.DTO.BookExpiredDTO;
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

    public void sendBookExpiredNotification(BookExpiredDTO bookExpiredDTO){
        kafkaTemplate.send("book_expiration_topic", bookExpiredDTO);
    }
}
