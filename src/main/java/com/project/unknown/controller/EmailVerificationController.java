package com.project.unknown.controller;

import com.project.unknown.domain.EmailVerificationRequest;
import com.project.unknown.domain.dtos.emailVerificationDto.EmailVerificationRequestDto;
import com.project.unknown.domain.entities.userEntity.User;
import com.project.unknown.mapper.EmailVerificationTokenMapper;
import com.project.unknown.repository.UserRepository;
import com.project.unknown.service.EmailVerificationTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class EmailVerificationController {

    private final EmailVerificationTokenService emailVerificationTokenService;
    private final EmailVerificationTokenMapper emailVerificationTokenMapper;
    private final UserRepository userRepository;

    @GetMapping("/verify-email")
    public ResponseEntity<?> verifyEmail(@RequestParam String token){
        try {
            emailVerificationTokenService.verifyEmail(token);
            return ResponseEntity.ok("E-Mail erfolgreich verifiziert");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PostMapping("/resend-verification")
    public ResponseEntity<?> resendVerification(@RequestBody EmailVerificationRequestDto emailVerificationRequestDto){
        try {
            EmailVerificationRequest verificationToken = emailVerificationTokenMapper.toVerificationEmail(emailVerificationRequestDto);

            User user = userRepository.findByEmail(verificationToken.getEmail()).orElseThrow(() -> new RuntimeException("Benutzer nicht gefunden"));

            if(user.isVerified()){
                return ResponseEntity.badRequest().body("E-Mail ist bereits verifiziert");
            }

            emailVerificationTokenService.sendVerificationEmail(user);

            return ResponseEntity.ok("Verifizierungs-E-Mail wurde erneut gesendet");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
