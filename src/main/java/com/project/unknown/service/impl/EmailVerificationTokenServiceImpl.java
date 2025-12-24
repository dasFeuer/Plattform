package com.project.unknown.service.impl;


import com.project.unknown.domain.entities.emailVerifyEntity.EmailVerificationToken;
import com.project.unknown.domain.entities.userEntity.User;
import com.project.unknown.repository.EmailVerificationTokenRepository;
import com.project.unknown.repository.UserRepository;
import com.project.unknown.service.EmailService;
import com.project.unknown.service.EmailVerificationTokenService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmailVerificationTokenServiceImpl implements EmailVerificationTokenService {

     private final UserRepository userRepository;
     private final EmailVerificationTokenRepository emailVerificationTokenRepository;
     private final EmailService emailService;


     @Transactional
     @Override
     public void sendVerificationEmail(User user) {
          emailVerificationTokenRepository.deleteByUserId(user.getId());
          emailVerificationTokenRepository.flush();

          String token = UUID.randomUUID().toString();
          EmailVerificationToken emailVerificationToken = new EmailVerificationToken(token, user);
          emailVerificationTokenRepository.save(emailVerificationToken);

          String verificationToken = "http://localhost:8080/api/verify-email?token=" + token;
          emailService.sendVerificationEmail(user.getEmail(), verificationToken);
     }

     @Transactional
     @Override
     public void verifyEmail(String token) {
          EmailVerificationToken emailVerificationToken = emailVerificationTokenRepository.findByToken(token)
                  .orElseThrow(() -> new RuntimeException("Token nicht gefunden!"));

          if(emailVerificationToken.isExpired()){
               throw new RuntimeException("Token ist abgelaufen");
          }
          if(emailVerificationToken.isUsed()){
               throw new RuntimeException("Token ist schon genutzt und ungültig!");
          }

          User user = emailVerificationToken.getUser();
          user.setVerified(true);
          user.setVerifiedAt(LocalDateTime.now());
          userRepository.save(user);

          emailVerificationToken.setUsed(true);
          emailVerificationTokenRepository.save(emailVerificationToken);
     }
}
