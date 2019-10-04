package com.docmarker.controller;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.docmarker.dto.PasswordDto;
import com.docmarker.dto.UserDto;
import com.docmarker.model.security.User;
import com.docmarker.service.UserRegistrationService;
import com.docmarker.web.util.GenericResponse;

@RestController
@RequestMapping("/user/")
public class RegistrationController {

	private final Logger LOGGER = LoggerFactory.getLogger(getClass());
	@Autowired
	private UserRegistrationService userService;
	
	@PostMapping(value = "registration")
	public GenericResponse registerUserAccount(@Valid @RequestBody final UserDto accountDto, final HttpServletRequest request) {
		LOGGER.debug("Registering user account with information: {}", accountDto);
		return userService.registerUser(accountDto,request);
	}

	@GetMapping(value = "registrationConfirm")
	public GenericResponse confirmRegistration(@RequestParam("token") String token) {
		return userService.confirmRegistration(token);
	}

	@PostMapping(value = "resetPassword")
	public GenericResponse resetPassword(@RequestParam("email") String userEmail, final HttpServletRequest request) {
		return userService.resetPassword(request, userEmail);
	}

//	@GetMapping(value = "changePassword")
//	public GenericResponse showChangePasswordPage(@RequestParam("id") long id,@RequestParam("token") String token, Locale locale) {
//		return securityUserService.validatePasswordResetToken(id, token);
//	}

	//flow needs improvement
	@PostMapping(value = "savePassword")
	public GenericResponse savePassword(@Valid PasswordDto passwordDto, @RequestParam("token") String token, Locale locale) {
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return userService.changeUserPassword(user, passwordDto, token);
	}
	@GetMapping(value = "resendRegistrationToken")
	public GenericResponse resendRegistrationToken(@RequestParam("token") final String existingToken, final HttpServletRequest request) {
		return userService.resendRegistrationToken(existingToken, request);
	}

	

}
