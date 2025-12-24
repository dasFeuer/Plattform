package com.project.unknown.controller;

import com.project.unknown.domain.PasswordResetRequest;
import com.project.unknown.domain.dtos.passwordResetTokenDto.PasswordResetConfirmDto;
import com.project.unknown.domain.dtos.passwordResetTokenDto.PasswordResetRequestDto;
import com.project.unknown.mapper.PasswordResetTokenMapper;
import com.project.unknown.service.PasswordResetTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PasswordResetController {

    private final PasswordResetTokenService passwordResetTokenService;
    private final PasswordResetTokenMapper tokenMapper;

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody PasswordResetRequestDto passwordResetRequestDto){
        try {
            PasswordResetRequest resetPassword = tokenMapper.toResetPassword(passwordResetRequestDto);
            passwordResetTokenService.createPasswordResetToken(resetPassword.getEmail());
            return ResponseEntity.ok("E-Mail zum Zurücksetzen wurde gesendet");
        } catch (Exception e) {
            return ResponseEntity.ok("Falls die E-Mail existiert, wurde eine Nachricht gesendet");
        }
    }

    @GetMapping("/reset-password")
    public ResponseEntity<?> validateResetToken(@RequestParam String token) {
        boolean isValid = passwordResetTokenService.validateToken(token);

        if(isValid){
            return ResponseEntity.ok("Token ist gültig");
        }

        return ResponseEntity.badRequest().body("Token ist ungültig oder abgelaufen");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody PasswordResetConfirmDto resetConfirmDto) {
        try {
            passwordResetTokenService.resetPassword(resetConfirmDto.getToken(), resetConfirmDto.getNewPassword());
            return ResponseEntity.ok("Passwort erfolgreich zurückgesetzt");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
