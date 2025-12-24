package com.project.unknown.service.impl;

import com.project.unknown.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;
    @Override
    public void sendPasswordResetEmail(String to, String resetLink) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail); // Nutzt die E-Mail aus application.properties
        message.setTo(to);
        message.setSubject("Passwort zurücksetzen");
        message.setText("Hallo,\n\n" +
                "Sie haben angefordert, Ihr Passwort zurückzusetzen.\n\n" +
                "Bitte klicken Sie auf den folgenden Link:\n" +
                resetLink + "\n\n" +
                "Dieser Link ist 1 Stunde gültig.");

        mailSender.send(message);

    }

    @Override
    public void sendVerificationEmail(String to, String verificationLink) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(to);
        message.setSubject("Email Verifizieren");
        message.setText("Hallo,\n\n" +
                "vielen Dank für Ihre Registrierung!\n\n" +
                "Bitte bestätigen Sie Ihre E-Mail-Adresse, indem Sie auf den folgenden Link klicken:\n" +
                verificationLink + "\n\n" +
                "Dieser Link ist 24 Stunden gültig.\n\n" +
                "Mit freundlichen Grüßen");

        mailSender.send(message);
    }
}