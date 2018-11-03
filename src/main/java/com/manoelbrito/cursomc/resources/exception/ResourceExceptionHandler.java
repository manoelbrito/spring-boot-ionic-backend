package com.manoelbrito.cursomc.resources.exception;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.manoelbrito.cursomc.services.exception.DataIntegrityException;
import com.manoelbrito.cursomc.services.exception.ObjectNotFoundException;

@ControllerAdvice
public class ResourceExceptionHandler {

	@ExceptionHandler(ObjectNotFoundException.class)
	public ResponseEntity<StrandardError> objectNotFound(ObjectNotFoundException e, HttpServletRequest request){
		
		
	StrandardError err=new StrandardError(HttpStatus.NOT_FOUND.value(), e.getMessage(), System.currentTimeMillis());
	
	return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
	}
	
	
	@ExceptionHandler(DataIntegrityException.class)
	public ResponseEntity<StrandardError> dataInterity(DataIntegrityException e, HttpServletRequest request){
		
		
	StrandardError err=new StrandardError(HttpStatus.BAD_REQUEST.value(), e.getMessage(), System.currentTimeMillis());
	
	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
	}
	
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<StrandardError> validation(MethodArgumentNotValidException e, HttpServletRequest request){
		
		
	ValidationErro err=new ValidationErro(HttpStatus.BAD_REQUEST.value(), "Erro de validação", System.currentTimeMillis());
	
	for(FieldError x: e.getBindingResult().getFieldErrors()) {
		err.addError(x.getField(), x.getDefaultMessage());
	}
	
	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
	}
}
