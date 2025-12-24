package com.project.unknown.service;


import com.project.unknown.domain.entities.userEntity.User;

public interface EmailVerificationTokenService {

     void sendVerificationEmail(User user);
     void verifyEmail(String token);

}
