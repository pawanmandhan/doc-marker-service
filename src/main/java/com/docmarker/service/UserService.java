package com.docmarker.service;

import java.util.HashSet;
import java.util.Set;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.docmarker.dto.UserDto;
import com.docmarker.exception.EmailAlreadyUsedException;
import com.docmarker.exception.LoginAlreadyUsedException;
import com.docmarker.model.VerificationToken;
import com.docmarker.model.security.Authority;
import com.docmarker.model.security.User;
import com.docmarker.repository.AuthorityRepository;
import com.docmarker.repository.UserRepository;
import com.docmarker.repository.VerificationTokenRepository;
import com.docmarker.service.util.AuthoritiesConstants;
import com.docmarker.service.util.RandomUtil;

@Service
public class UserService implements IUserService {

	private final Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private AuthorityRepository authorityRepository;
	@Autowired
	private VerificationTokenRepository tokenRepository;

	@Transactional
	@Override
	public User registerUser(UserDto userDTO) {
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
		newUser.setActivationKey(RandomUtil.generateActivationKey());
		Set<Authority> authorities = new HashSet<>();
		authorityRepository.findByName(AuthoritiesConstants.USER).ifPresent(authorities::add);
		newUser.setAuthorities(authorities);
		userRepository.save(newUser);
		log.debug("Created Information for User: {}", newUser);
		return newUser;
	}

	private boolean removeNonActivatedUser(User existingUser) {
		if (existingUser.isEnabled()) {
			return false;
		}
		userRepository.delete(existingUser);
		userRepository.flush();
		return true;
	}

	private boolean emailExist(String email) {
		User user = userRepository.findByEmail(email);
		if (user != null) {
			return true;
		}
		return false;
	}

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
}