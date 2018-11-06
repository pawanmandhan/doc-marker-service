package com.docmarker.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.docmarker.dto.UserDto;
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
	private ApplicationEventPublisher eventPublisher;

	public RegistrationController() {
		super();
	}

	// Registration

	@RequestMapping(value = "/registration", method = RequestMethod.POST)
	public GenericResponse registerUserAccount(@Valid @RequestBody final UserDto accountDto, final HttpServletRequest request) {
		LOGGER.debug("Registering user account with information: {}", accountDto);

		final User registered = userService.registerUser(accountDto);
		eventPublisher
				.publishEvent(new OnRegistrationCompleteEvent(registered, request.getLocale(), getAppUrl(request)));
		return new GenericResponse("success");
	}

	private String getAppUrl(HttpServletRequest request) {
		return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
	}

}