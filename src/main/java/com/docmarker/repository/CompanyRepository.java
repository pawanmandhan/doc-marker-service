package com.docmarker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.docmarker.model.Company;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

	public Company findByName(String name);

}
