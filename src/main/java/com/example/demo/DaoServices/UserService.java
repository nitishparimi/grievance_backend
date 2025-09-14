package com.example.demo.DaoServices;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.Entities.UserEntity;
import com.example.demo.Repositories.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.example.demo.Dto.*;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepo;
    
    @Autowired
    private EmailService emailservice;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserEntity save(UserDto dto) {
        UserEntity user = new UserEntity();
        
        user.setName(dto.getName());
        user.setMail(dto.getMail());
        user.setId(dto.getId());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setVerified(false);
        user.setCourse(dto.getCourse());
        user.setDepartment(dto.getDepartment());
        user.setVerificationToken(UUID.randomUUID().toString());
        user.setTokenGeneratedAt(LocalDateTime.now());
        userRepo.save(user);
        emailservice.sendVerificationEmail(user);
        
        return user;
        
    }
    
	public String UserLogin(UserDto u) {
    	UserEntity us = userRepo.findById(u.getId());
    	
    	if(us == null) return "No user Found";
    	if(us.isActiveSession()) return "Already LoggedIn";
    	if(!us.isVerified()) return "Verify Your email To Login";
    	if(passwordEncoder.matches(u.getPassword(), us.getPassword())) {
    		us.setActiveSession(true);
    		userRepo.save(us);
    		return "Success";
    	}
    		
    	return "Password Invalid";
    }
	
	
	@Transactional
	public String verifyUser(String token) {
	    UserEntity user = userRepo.findByVerificationToken(token);
	    
	    if (user == null) return "No User Found"; 
	    if(isTokenExpired(user)) return "Token Experied";
	    
	    user.setVerified(true);
	    user.setVerificationToken(null);
	    return "Verified";
	}
	
	public String resendVerification(String email) {
		UserEntity user = userRepo.findByMail(email);
		
		if(user == null) return "No user found";
		if(user.isVerified()) return "Verified User";
		
		user.setVerificationToken(UUID.randomUUID().toString());
		user.setTokenGeneratedAt(LocalDateTime.now());
		userRepo.save(user);
		emailservice.sendVerificationEmail(user);
	    return "Sent";
		
	}
	
	public boolean isTokenExpired(UserEntity user) {
	    return user.getTokenGeneratedAt().plusHours(1).isBefore(LocalDateTime.now());
	}
	
	public String generateResetToken(String id) {
		UserEntity u = userRepo.findById(id);
		if(u == null) return "No User Found for this Id/ Check the Id you Mentioned";
		u.setResetToken(UUID.randomUUID().toString());
        u.setResetTokenGeneratedAt(LocalDateTime.now());
        userRepo.save(u);
		emailservice.sendResetTokenEmail(u);
		return "Sent";
	}
	
	public UserEntity verifyResetToken(String token) {
		UserEntity u = userRepo.findByResetToken(token);
		if(u == null || isResetTokenExpired(u)) return null;
		
		return u;
		
	}
	
	public boolean isResetTokenExpired(UserEntity user) {
	    return user.getResetTokenGeneratedAt().plusMinutes(10).isBefore(LocalDateTime.now());
	}
	
	public UserEntity changePass(String id, String newpassword) {
		UserEntity u = userRepo.findById(id);
		u.setPassword(passwordEncoder.encode(newpassword));
		u.setResetToken(null);
		u.setResetTokenGeneratedAt(null);
		userRepo.save(u);
		return u;
	}

	public boolean findById(String id) {
		// TODO Auto-generated method stub
		UserEntity u = userRepo.findById(id);
		if(u != null && u.isActiveSession()) {
			u.setActiveSession(false);
	        userRepo.save(u);
	        return true;
		}
		return false;
	}


}