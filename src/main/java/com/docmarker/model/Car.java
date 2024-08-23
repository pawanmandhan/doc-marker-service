package com.docmarker.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import java.io.Serializable;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "CAR")
@Getter
@Setter
public class Car implements Serializable {

  private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", updatable = false, nullable = false)
    private Long id = null;

    @Column(name = "REGISTRATION_NUMBER")
    private String registrationNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    private Company company;
}
