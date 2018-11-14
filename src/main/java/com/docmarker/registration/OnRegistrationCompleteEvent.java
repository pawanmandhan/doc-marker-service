package com.docmarker.registration;

import java.util.Locale;

import org.springframework.context.ApplicationEvent;

import com.docmarker.model.security.User;

import lombok.Getter;

@SuppressWarnings("serial")
@Getter
public class OnRegistrationCompleteEvent extends ApplicationEvent {

	private final String appUrl;
	private final Locale locale;
	private final User user;

	public OnRegistrationCompleteEvent(final User user, final Locale locale, final String appUrl) {
		super(user);
		this.user = user;
		this.locale = locale;
		this.appUrl = appUrl;
	}

}