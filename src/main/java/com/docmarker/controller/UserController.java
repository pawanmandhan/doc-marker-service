package com.docmarker.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.docmarker.dto.UserDto;
import com.docmarker.model.security.User;
import com.docmarker.service.IUserService;



public class UserController {
	
//	@Autowired
//	private IUserService service;
//
//	@PostMapping(value = "/register")
//	public User registerUserAccount(@RequestBody @Valid UserDto accountDto) {
//		
//		return createUserAccount(accountDto);
//	}
//	
//	private User createUserAccount(UserDto accountDto) {
//	    User registered = null;
//	    
//	     registered = service.registerUser(accountDto);
//	        
//	    return registered;
//	}

}
