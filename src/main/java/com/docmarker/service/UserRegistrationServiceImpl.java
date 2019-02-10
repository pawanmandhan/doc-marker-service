package com.docmarker.service;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.docmarker.dto.PasswordDto;
import com.docmarker.dto.UserDto;
import com.docmarker.exception.EmailAlreadyUsedException;
import com.docmarker.exception.LoginAlreadyUsedException;
import com.docmarker.model.PasswordResetToken;
import com.docmarker.model.VerificationToken;
import com.docmarker.model.security.Authority;
import com.docmarker.model.security.User;
import com.docmarker.registration.OnRegistrationCompleteEvent;
import com.docmarker.repository.AuthorityRepository;
import com.docmarker.repository.PasswordResetTokenRepository;
import com.docmarker.repository.UserRepository;
import com.docmarker.repository.VerificationTokenRepository;
import com.docmarker.service.util.AuthoritiesConstants;
import com.docmarker.web.util.GenericResponse;

@Service
public class UserRegistrationServiceImpl implements UserRegistrationService {

	private final Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private Environment env;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private AuthorityRepository authorityRepository;
	@Autowired
	private VerificationTokenRepository tokenRepository;
	@Autowired
	private PasswordResetTokenRepository passwordTokenRepository;
	@Autowired
	private ApplicationEventPublisher eventPublisher;
	@Autowired
	private UserSecurityService securityUserService;

	@Transactional
	@Override
	public GenericResponse registerUser(UserDto userDTO, final HttpServletRequest request) {
		userRepository.findByUsername(userDTO.getEmail().toLowerCase()).ifPresent(existingUser -> {
			boolean removed = removeNonActivatedUser(existingUser);
			if (!removed) {
				throw new LoginAlreadyUsedException();
			}
		});
		userRepository.findOneByEmailIgnoreCase(userDTO.getEmail()).ifPresent(existingUser -> {
			boolean removed = removeNonActivatedUser(existingUser);
			if (!removed) {
				throw new EmailAlreadyUsedException(
						"There is an account with that email address: " + userDTO.getEmail());
			}
		});

		User newUser = new User();
		String encryptedPassword = passwordEncoder.encode(userDTO.getPassword());
		newUser.setUsername(userDTO.getEmail().toLowerCase());
		newUser.setPassword(encryptedPassword);
		newUser.setFirstName(userDTO.getFirstName());
		newUser.setLastName(userDTO.getLastName());
		newUser.setEmail(userDTO.getEmail().toLowerCase());
		// new user is not active
		newUser.setEnabled(false);
		// new user gets registration key
		//newUser.setActivationKey(RandomUtil.generateActivationKey());
		Set<Authority> authorities = new HashSet<>();
		authorityRepository.findByName(AuthoritiesConstants.USER).ifPresent(authorities::add);
		newUser.setAuthorities(authorities);
		userRepository.save(newUser);
		log.debug("Created Information for User: {}", newUser);
		eventPublisher
		.publishEvent(new OnRegistrationCompleteEvent(newUser, request.getLocale(), getAppUrl(request)));

		return new GenericResponse("success");
	}

	private String getAppUrl(HttpServletRequest request) {
		return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
	}
	
	private boolean removeNonActivatedUser(User existingUser) {
		if (existingUser.isEnabled()) {
			return false;
		}
		userRepository.delete(existingUser);
		userRepository.flush();
		return true;
	}

//	private boolean emailExist(String email) {
//		User user = userRepository.findByEmail(email);
//		if (user != null) {
//			return true;
//		}
//		return false;
//	}

	@Override
	public void createVerificationToken(final User user, final String token) {
		final VerificationToken myToken = new VerificationToken(token, user);
		tokenRepository.save(myToken);
	}

	@Override
	public VerificationToken getVerificationToken(final String verificationToken) {
		return tokenRepository.findByToken(verificationToken);
	}

	@Override
	public void saveRegisteredUser(final User user) {
		userRepository.save(user);
	}

	@Override
	public GenericResponse changeUserPassword(User user, PasswordDto passwordDto, String token) {
		securityUserService.validatePasswordResetToken(user.getId(), token);
		user.setPassword(passwordEncoder.encode(passwordDto.getNewPassword()));
		userRepository.save(user);
		return new GenericResponse("message.resetPasswordSuc");
	}

	@Override
	public User findUserByEmail(final String email) {
		return userRepository.findByEmail(email);
	}

	@Override
	public void createPasswordResetTokenForUser(final User user, final String token) {
		final PasswordResetToken myToken = new PasswordResetToken(token, user);
		passwordTokenRepository.save(myToken);
	}

	@Override
	public GenericResponse confirmRegistration(String token) {
		VerificationToken verificationToken = getVerificationToken(token);
		if (verificationToken == null) {
			return new GenericResponse("Token not found.");
		}

		User user = verificationToken.getUser();
		Calendar cal = Calendar.getInstance();
		if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
			return new GenericResponse("Token Expired");
		}
		
		if (user.isEnabled()) {
			return new GenericResponse("User already confirmed mail.");
		}

		user.setEnabled(true);
		saveRegisteredUser(user);
		return new GenericResponse("U have successfully verified your mail.");
	}

	@Override
	public GenericResponse resetPassword(HttpServletRequest request, String userEmail) {
		User user = findUserByEmail(userEmail);
		// if (user == null) {
		// throw new UsernameNotFoundException();
		// }
		String token = UUID.randomUUID().toString();
		createPasswordResetTokenForUser(user, token);
		// mailSender.send(constructResetTokenEmail(getAppUrl(request),
		// request.getLocale(), token, user));
		return new GenericResponse("message.resetPasswordEmail");
	}
	
	private SimpleMailMessage constructEmail(String subject, String body, User user) {
		final SimpleMailMessage email = new SimpleMailMessage();
		email.setSubject(subject);
		email.setText(body);
		email.setTo(user.getEmail());
		email.setFrom(env.getProperty("support.email"));
		return email;
	} 
	private SimpleMailMessage constructResetTokenEmail(final String contextPath, final Locale locale,
			final String token, final User user) {
		final String url = contextPath + "/user/changePassword?id=" + user.getId() + "&token=" + token;
		final String message = "";
		// messages.getMessage("message.resetPassword", null, locale);
		return constructEmail("Reset Password", message + " \r\n" + url, user);
	}

}