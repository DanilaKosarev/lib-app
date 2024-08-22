package com.example.notificationservice.services;

import com.example.notificationservice.DTO.BookExpiredDTO;
import com.example.notificationservice.models.Book;
import com.example.notificationservice.repositories.BooksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class NotificationsService {

    @Value("${days_to_expire}")
    private static int DAYS_TO_EXPIRE;

    private final BooksRepository booksRepository;

    private final KafkaProducerService kafkaProducerService;

    @Autowired
    public NotificationsService(BooksRepository booksRepository, KafkaProducerService kafkaProducerService) {
        this.booksRepository = booksRepository;
        this.kafkaProducerService = kafkaProducerService;
    }

    //at 10:00 every day
    @Scheduled(cron = "0 0 10 * * *")
    public void scanForExpirationAndSendNotification(){
        List<Book> booksToNotify = booksRepository.findByTakenDateBefore(Date.from(ZonedDateTime.now().minusDays(DAYS_TO_EXPIRE).toInstant()));

        for(Book book : booksToNotify){
            kafkaProducerService.sendBookExpiredNotification(new BookExpiredDTO(book.getTitle(), book.getOwner().getEmail()));
        }

    }
}
