package com.docmarker.service;

import javax.servlet.http.HttpServletRequest;

import com.docmarker.dto.PasswordDto;
import com.docmarker.dto.UserDto;
import com.docmarker.exception.EmailAlreadyUsedException;
import com.docmarker.model.VerificationToken;
import com.docmarker.model.security.User;
import com.docmarker.web.util.GenericResponse;

public interface UserRegistrationService {
	GenericResponse registerUser(UserDto accountDto, final HttpServletRequest request) throws EmailAlreadyUsedException;

	void createVerificationToken(User user, String token);

	VerificationToken getVerificationToken(String verificationToken);

	void saveRegisteredUser(User user);

	User findUserByEmail(String userEmail);

	void createPasswordResetTokenForUser(final User user, final String token);

	GenericResponse confirmRegistration(String token);

	GenericResponse resetPassword(HttpServletRequest request, String userEmail);

	GenericResponse changeUserPassword(User user, PasswordDto passwordDto, String token);

}