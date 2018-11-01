package com.docmarker.service;

import com.docmarker.dto.UserDto;
import com.docmarker.exception.EmailAlreadyUsedException;
import com.docmarker.model.security.User;

public interface IUserService {
    User registerUser(UserDto accountDto) throws EmailAlreadyUsedException;
}