package com.docmarker.service;

import com.docmarker.dto.UserDto;
import com.docmarker.exception.EmailAlreadyUsedException;
import com.docmarker.model.VerificationToken;
import com.docmarker.model.security.User;

public interface IUserService {
    User registerUser(UserDto accountDto) throws EmailAlreadyUsedException;
    void createVerificationToken(User user, String token);
    VerificationToken getVerificationToken(String verificationToken);
    void saveRegisteredUser(User user);
    VerificationToken generateNewVerificationToken(String token);
    User getUser(String verificationToken);
}