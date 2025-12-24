package com.project.unknown.service.impl;

import com.project.unknown.domain.entities.passwordResetTokenEntity.PasswordResetToken;
import com.project.unknown.domain.entities.userEntity.User;
import com.project.unknown.repository.PasswordResetTokenRepository;
import com.project.unknown.repository.UserRepository;
import com.project.unknown.service.EmailService;
import com.project.unknown.service.PasswordResetTokenService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordResetTokenServiceImpl implements PasswordResetTokenService {


     private final UserRepository userRepository;
     private final PasswordResetTokenRepository tokenRepository;
     private final EmailService emailService;
     private final PasswordEncoder passwordEncoder;

     @Transactional
     @Override
     public void createPasswordResetToken(String email) {
          User user = userRepository.findByEmail(email)
                  .orElseThrow(() -> new RuntimeException("Benutzer nicht gefunden"));

          // ZUERST löschen!
          tokenRepository.deleteByUserId(user.getId());
          tokenRepository.flush(); // <- Wichtig: Sofort ausführen!

          // DANN neuen Token erstellen
          String token = UUID.randomUUID().toString();
          PasswordResetToken passwordResetToken = new PasswordResetToken(token, user);
          tokenRepository.save(passwordResetToken);

          // Email senden
          String resetLink = "http://localhost:8080/api/reset-password?token=" + token;
          emailService.sendPasswordResetEmail(user.getEmail(), resetLink);
     }

     @Override
     public void resetPassword(String token, String newPassword) {
          PasswordResetToken resetToken = tokenRepository.findByToken(token)
                  .orElseThrow(() -> new RuntimeException("Ungültiger Token"));

          if (resetToken.isExpired()) {
               throw new RuntimeException("Token ist abgelaufen");
          }

          if (resetToken.isUsed()) {
               throw new RuntimeException("Token wurde bereits verwendet");
          }
          // Neue Passwort setzen
          User user = resetToken.getUser();
          user.setPassword(passwordEncoder.encode(newPassword));
          userRepository.save(user);

          // Verwendet markieren
          resetToken.setUsed(true);
          tokenRepository.save(resetToken);

     }

     @Override
     public boolean validateToken(String token) {
         return tokenRepository.findByToken(token)
                  .map(t -> !t.isUsed() && !t.isExpired())
                  .orElse(false);
     }
}
