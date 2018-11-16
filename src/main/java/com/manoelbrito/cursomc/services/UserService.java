package com.manoelbrito.cursomc.services;

import org.springframework.security.core.context.SecurityContextHolder;

import com.manoelbrito.cursomc.security.UserSS;

public class UserService {
	
	public static UserSS autenticated() {
		try {
		return (UserSS)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}catch(Exception e) {
		return null;
	}
	}
}
