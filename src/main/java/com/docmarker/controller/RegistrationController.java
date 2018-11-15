package com.docmarker.controller;

import java.util.Calendar;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.docmarker.dto.UserDto;
import com.docmarker.model.VerificationToken;
import com.docmarker.model.security.User;
import com.docmarker.registration.OnRegistrationCompleteEvent;
import com.docmarker.service.IUserService;
import com.docmarker.web.util.GenericResponse;

@RestController
@RequestMapping("/secured")
public class RegistrationController {

	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	@Autowired
	private IUserService userService;
	@Autowired
	private JavaMailSender mailSender;
	@Autowired
	private Environment env;

	@Autowired
	private ApplicationEventPublisher eventPublisher;

	@RequestMapping(value = "/registration", method = RequestMethod.POST)
	public GenericResponse registerUserAccount(@Valid @RequestBody final UserDto accountDto,
			final HttpServletRequest request) {
		LOGGER.debug("Registering user account with information: {}", accountDto);

		final User registered = userService.registerUser(accountDto);
		eventPublisher
				.publishEvent(new OnRegistrationCompleteEvent(registered, request.getLocale(), getAppUrl(request)));
		return new GenericResponse("success");
	}

	private String getAppUrl(HttpServletRequest request) {
		return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
	}

	@RequestMapping(value = "/regitrationConfirm", method = RequestMethod.GET)
	public String confirmRegistration(@RequestParam("token") String token) {

		VerificationToken verificationToken = userService.getVerificationToken(token);
		if (verificationToken == null) {
			return "Token not found";
		}

		User user = verificationToken.getUser();
		Calendar cal = Calendar.getInstance();
		if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
			return "Token Expired";
		}

		user.setEnabled(true);
		userService.saveRegisteredUser(user);
		return "Token added succesfully";
	}

	@RequestMapping(value = "/user/resendRegistrationToken", method = RequestMethod.GET)
	public GenericResponse resendRegistrationToken(final HttpServletRequest request,
			@RequestParam("token") final String existingToken) {
		final VerificationToken newToken = userService.generateNewVerificationToken(existingToken);
		final User user = userService.getUser(newToken.getToken());
		mailSender.send(constructResendVerificationTokenEmail(getAppUrl(request), request.getLocale(), newToken, user));
		return new GenericResponse("Resend Token Generated.");
	}

	private SimpleMailMessage constructResendVerificationTokenEmail(final String contextPath, final Locale locale,
			final VerificationToken newToken, final User user) {
		final String confirmationUrl = contextPath + "/registrationConfirm.html?token=" + newToken.getToken();
		final String message ="Resend Token Generated.";
		return constructEmail("Resend Registration Token", message + " \r\n" + confirmationUrl, user);
	}

	private SimpleMailMessage constructEmail(String subject, String body, User user) {
		final SimpleMailMessage email = new SimpleMailMessage();
		email.setSubject(subject);
		email.setText(body);
		email.setTo(user.getEmail());
		email.setFrom(env.getProperty("support.email"));
		return email;
	}

}
