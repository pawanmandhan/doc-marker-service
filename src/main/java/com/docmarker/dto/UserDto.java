package com.docmarker.dto;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.docmarker.validation.PasswordMatches;
import com.docmarker.validation.ValidEmail;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@PasswordMatches
@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
public class UserDto {
    @NotNull
    @NotEmpty
    private String firstName;
     
    @NotNull
    @NotEmpty
    private String lastName;
     
    @NotNull
    @NotEmpty
    private String password;
    private String matchingPassword;
    
    @ValidEmail
    @NotNull
    @NotEmpty
    private String email;
     
   
}
