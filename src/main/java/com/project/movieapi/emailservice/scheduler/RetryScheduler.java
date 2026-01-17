package com.project.movieapi.emailservice.scheduler;

import com.project.movieapi.emailservice.model.EmailLog;
import com.project.movieapi.emailservice.repository.EmailLogRepository;
import com.project.movieapi.emailservice.service.EmailSenderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class RetryScheduler {

    private final EmailLogRepository repository;
    private final EmailSenderService emailService;

    @Scheduled(fixedRate = 300000)
    public void retryFailedEmails() {
        log.info("Starting retry job...");
        List<EmailLog> failedEmails = repository.findByStatus("FAILED");

        for (EmailLog email : failedEmails) {
            log.info("Retrying email id: {}", email.getId());
            emailService.trySend(email);
        }
    }
}