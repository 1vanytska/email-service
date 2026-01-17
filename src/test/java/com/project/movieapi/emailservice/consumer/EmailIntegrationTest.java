package com.project.movieapi.emailservice.consumer;

import com.project.movieapi.emailservice.dto.EmailRequestDto;
import com.project.movieapi.emailservice.model.EmailLog;
import com.project.movieapi.emailservice.repository.EmailLogRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class EmailIntegrationTest {

    @Autowired
    private EmailConsumer emailConsumer;

    @MockitoBean
    private JavaMailSender javaMailSender;

    @MockitoBean
    private EmailLogRepository repository;

    @Test
    void shouldProcessMessageAndSendEmailSuccessfully() {
        EmailRequestDto message = new EmailRequestDto("user@test.com", "Subject", "Body");

        when(repository.save(any(EmailLog.class))).thenAnswer(i -> i.getArguments()[0]);

        emailConsumer.consumeMessage(message);

        verify(javaMailSender, times(1)).send(any(SimpleMailMessage.class));

        verify(repository, atLeast(1)).save(argThat(log ->
                "SENT".equals(log.getStatus()) && log.getRecipient().equals("user@test.com")
        ));
    }

    @Test
    void shouldHandleFailedSending() {
        EmailRequestDto message = new EmailRequestDto("fail@test.com", "Subject", "Body");

        when(repository.save(any(EmailLog.class))).thenAnswer(i -> i.getArguments()[0]);

        doThrow(new MailSendException("SMTP Error")).when(javaMailSender).send(any(SimpleMailMessage.class));

        emailConsumer.consumeMessage(message);

        verify(repository, atLeast(1)).save(argThat(log ->
                "FAILED".equals(log.getStatus()) && log.getErrorMessage().contains("SMTP Error")
        ));
    }
}