package com.docmarker.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import com.docmarker.model.security.User;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class PasswordResetToken {

//	private static final int EXPIRATION = 60 * 24;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String token;

	@OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
	@JoinColumn(nullable = false, name = "user_id")
	private User user;

	private Date expiryDate;
	public PasswordResetToken() {}
	
	public PasswordResetToken(String token, User user) {
		this.user = user;
		this.token = token;
	}
}
