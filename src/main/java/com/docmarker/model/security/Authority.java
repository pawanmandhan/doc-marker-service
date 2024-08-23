package com.docmarker.model.security;

import org.springframework.security.core.GrantedAuthority;

import java.util.HashSet;
import java.util.Set;
import jakarta.persistence.*;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "AUTHORITY", uniqueConstraints = {@UniqueConstraint(columnNames = {"NAME"})})
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class Authority implements GrantedAuthority {

   private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME")
    private String name;
    
    @ManyToMany(mappedBy = "authorities")
    private Set<User> users = new HashSet<>();

    @Override
    public String getAuthority() {
        return getName();
    }
}
