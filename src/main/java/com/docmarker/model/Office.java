package com.docmarker.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import java.io.Serializable;

import jakarta.persistence.*;


import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "OFFICE")
@Getter
@Setter
public class Office implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID", updatable = false, nullable = false)
	private Long id = null;

	@Column(name = "NAME", nullable = false)
	private String name;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "ADDRESS_ID")
	private Address address;

	@ManyToOne(fetch = FetchType.LAZY)
	@JsonBackReference
	private Department department;
}