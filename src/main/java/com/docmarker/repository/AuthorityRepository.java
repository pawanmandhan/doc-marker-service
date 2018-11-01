package com.docmarker.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.docmarker.model.security.Authority;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, Long> {
	Optional<Authority> findByName(@Param("name") String name);
}
