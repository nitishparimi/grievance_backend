package com.example.demo.DaoServices;

import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.example.demo.Entities.UserEntity;
import com.example.demo.Entities.GrievanceEntity;


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
	    String message = "Click the link to reset your Password for the account " + user.getBusinessId() 
	    				+ ": " + verificationUrl + 
	    				"\n This link will be expired within 10 Min i.e " + 
	    				user.getResetTokenGeneratedAt().plusMinutes(10).format(formatter);

	    SimpleMailMessage email = new SimpleMailMessage();
	    email.setTo(user.getMail());
	    email.setSubject(subject);
	    email.setText(message);

	    mailSender.send(email);
	}

	public void sendGrievanceSubmissionEmail(UserEntity submitter, GrievanceEntity grievance) {
		String subject = "Grievance Submitted";
		String message = "Dear " + submitter.getName() + ",\n\n" +
				"Your grievance has been submitted successfully." +
				"\nDepartment: " + grievance.getDepartment() +
				"\nIssue: " + grievance.getIssue() +
				"\nStatus: " + grievance.getStatus() +
				"\n\nWe will keep you updated.";

		SimpleMailMessage email = new SimpleMailMessage();
		email.setTo(submitter.getMail());
		email.setSubject(subject);
		email.setText(message);

		mailSender.send(email);
	}

	public void sendGrievanceStatusUpdateEmail(UserEntity submitter, GrievanceEntity grievance) {
		String subject = "Grievance Status Updated";
		String message = "Dear " + submitter.getName() + ",\n\n" +
				"Your grievance status has been updated." +
				"\nDepartment: " + grievance.getDepartment() +
				"\nIssue: " + grievance.getIssue() +
				"\nCurrent Status: " + grievance.getStatus() +
				(grievance.getResult() != null ? "\nResult: " + grievance.getResult() : "") +
				"\n\nThank you.";

		SimpleMailMessage email = new SimpleMailMessage();
		email.setTo(submitter.getMail());
		email.setSubject(subject);
		email.setText(message);

		mailSender.send(email);
	}

}
