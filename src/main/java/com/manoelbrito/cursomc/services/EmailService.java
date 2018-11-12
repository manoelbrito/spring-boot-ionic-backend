package com.manoelbrito.cursomc.services;

import org.springframework.mail.SimpleMailMessage;

import com.manoelbrito.cursomc.domain.Pedido;

public interface EmailService {
	void sendOrderConfirmationEmail(Pedido obj);
	void sendEmail(SimpleMailMessage msg);

}
