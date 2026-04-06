package com.smartspend.service;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service @RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;
    @Value("${spring.mail.username}") private String from;
    @Value("${app.base-url}") private String baseUrl;

    public void sendVerification(String to, String token) {
        sendEmail(to, "Verify your SmartSpend account",
            "Click the link to verify your email: " + baseUrl + "/verify?token=" + token);
    }

    public void sendPasswordReset(String to, String token) {
        sendEmail(to, "Reset your SmartSpend password",
            "Click the link to reset your password: " + baseUrl + "/reset-password?token=" + token);
    }

    public void sendBudgetAlert(String to, String category, double percentage) {
        sendEmail(to, "SmartSpend Budget Alert",
            "You have used " + String.format("%.0f", percentage) + "% of your " + category + " budget this month.");
    }

    private void sendEmail(String to, String subject, String text) {
        try {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setFrom(from); msg.setTo(to); msg.setSubject(subject); msg.setText(text);
            mailSender.send(msg);
        } catch (Exception e) { System.err.println("Email error: " + e.getMessage()); }
    }
}
