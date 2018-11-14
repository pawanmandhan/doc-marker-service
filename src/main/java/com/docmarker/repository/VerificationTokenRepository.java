package com.docmarker.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.docmarker.model.VerificationToken;
import com.docmarker.model.security.User;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {

	VerificationToken findByToken(String token);

	VerificationToken findByUser(User user);
}
