package com.example.demo.DaoServices;

import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.example.demo.Entities.UserEntity;


@Service
public class EmailService {
	
	
	@Autowired
	private JavaMailSender mailSender;
	
	
	@Value("${password.reset-token.url}")
    private String reset_token;
	
	@Value("${user.verification-token.url}")
	private String verification_token;

	public void sendVerificationEmail(UserEntity user) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
	    String subject = "Please verify your email";
	    String verificationUrl = verification_token + user.getVerificationToken();
	    String message = "Click the link to verify your email: " + verificationUrl + 
	    		"\n This link will be expired with an hour i.e " + 
	    		user.getTokenGeneratedAt().plusHours(1).format(formatter);

	    SimpleMailMessage email = new SimpleMailMessage();
	    email.setTo(user.getMail());
	    email.setSubject(subject);
	    email.setText(message);

	    mailSender.send(email);
	}
	
	public void sendResetTokenEmail(UserEntity user) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
	    String subject = "Please reset your password";
	    String verificationUrl = reset_token + user.getResetToken();
	    String message = "Click the link to reset your Password for the account " + user.getId() 
	    				+ ": " + verificationUrl + 
	    				"\n This link will be expired within 10 Min i.e " + 
	    				user.getResetTokenGeneratedAt().plusMinutes(10).format(formatter);

	    SimpleMailMessage email = new SimpleMailMessage();
	    email.setTo(user.getMail());
	    email.setSubject(subject);
	    email.setText(message);

	    mailSender.send(email);
	}

}
