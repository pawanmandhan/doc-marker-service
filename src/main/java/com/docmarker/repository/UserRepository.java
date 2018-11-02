package com.docmarker.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.docmarker.model.security.User;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	@Query("SELECT DISTINCT user FROM User user " +
            "INNER JOIN FETCH user.authorities AS authorities " +
            "WHERE user.username = :username")
	Optional<User> findByUsername(@Param("username") String username);
	
	
	User findByEmail(String email);
	 
	Optional<User> findOneByEmailIgnoreCase(@Param("email") String email);
}