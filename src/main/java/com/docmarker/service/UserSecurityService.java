package com.docmarker.service;

import com.docmarker.web.util.GenericResponse;

public interface UserSecurityService {

	GenericResponse validatePasswordResetToken(long id, String token);

}
