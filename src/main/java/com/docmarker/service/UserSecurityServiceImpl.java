package com.docmarker.service;

import java.util.Arrays;
import java.util.Calendar;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.docmarker.model.PasswordResetToken;
import com.docmarker.model.security.User;
import com.docmarker.repository.PasswordResetTokenRepository;
import com.docmarker.web.util.GenericResponse;

@Service
@Transactional
public class UserSecurityServiceImpl implements UserSecurityService {

	@Autowired
	private PasswordResetTokenRepository passwordTokenRepository;

	@Override
	public GenericResponse validatePasswordResetToken(long id, String token) {
		final PasswordResetToken passToken = passwordTokenRepository.findByToken(token);
		//current user and user linked with token are different.
		if ((passToken == null) || (passToken.getUser().getId() != id)) {
			return new GenericResponse("Invalid Token");
		}

		final Calendar cal = Calendar.getInstance();
		if ((passToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
			return new GenericResponse("Token Expired");
		}

		final User user = passToken.getUser();
		final Authentication auth = new UsernamePasswordAuthenticationToken(user, null,
				Arrays.asList(new SimpleGrantedAuthority("CHANGE_PASSWORD_PRIVILEGE")));
		SecurityContextHolder.getContext().setAuthentication(auth);
		return new GenericResponse("Token Validated successfully.");
	}

}