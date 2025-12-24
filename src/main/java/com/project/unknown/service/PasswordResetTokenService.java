package com.project.unknown.service;


public interface PasswordResetTokenService {

     void createPasswordResetToken(String email);
     void resetPassword(String token, String newPassword);
     boolean validateToken(String token);

}
