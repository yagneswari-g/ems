package com.example.ems.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class NotificationService {
    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);
    private final Optional<JavaMailSender> sender;
    private final String from;

    public NotificationService(Optional<JavaMailSender> sender, @Value("${app.mail.from}") String from) {
        this.sender = sender; this.from = from;
    }

    public void send(String to, String subject, String body) {
        if (to == null || to.isBlank() || sender.isEmpty()) {
            log.info("Email skipped (SMTP not configured): {} - {}", to, subject); return;
        }
        try {
            MimeMessage message = sender.get().createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");
            helper.setFrom(from); helper.setTo(to); helper.setSubject(subject); helper.setText(body, false);
            sender.get().send(message);
        } catch (MessagingException | RuntimeException ex) {
            log.warn("Email notification failed for {}: {}", to, ex.getMessage());
        }
    }
}
