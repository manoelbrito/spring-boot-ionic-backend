package com.manoelbrito.cursomc.services;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.manoelbrito.cursomc.domain.Cliente;
import com.manoelbrito.cursomc.repositories.ClienteRepository;
import com.manoelbrito.cursomc.services.exception.ObjectNotFoundException;

@Service
public class AuthService {
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private BCryptPasswordEncoder pe;
	
	@Autowired
	private EmailService emailService;
	
	private Random rand=new Random();
	
	public void sendNewPassword(String email) {
		
	
	Cliente cliente=clienteRepository.findByEmail(email);
	if(cliente==null) {
		throw new ObjectNotFoundException("Email não encontrado");
	}
	
		String newPass=newPassword();
		cliente.setSenha(pe.encode(newPass));
		emailService.sendNewPasswordEmail(cliente,newPass);
		
	}

	private String newPassword() {
		char[] vet=new char[10];
		for(int i=0;i<10;i++) {
			vet[i]=randomChar();
		}
		return new String(vet);
	}

	private char randomChar() {
		
		int opt=rand.nextInt(3);
		
		if(opt==0) {//gera um digito
			
			return (char) (rand.nextInt(10)+48);
		}
		
		else if(opt==1) {//gera letra maiúscula
			
			return (char) (rand.nextInt(26)+65);
			
		}
		
		else{//gera letra minúscula
			
			return (char) (rand.nextInt(26)+97);
			
		}
		
	}

}
