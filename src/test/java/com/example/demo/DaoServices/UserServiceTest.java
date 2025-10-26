package com.example.demo.DaoServices;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.UUID;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.demo.Dto.UserDto;
import com.example.demo.Entities.UserEntity;
import com.example.demo.Repositories.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
	
	@Mock
	UserRepository userRepo;
	
	@Mock
    PasswordEncoder passwordEncoder;
	
	@Mock
	EmailService emailservice;
	
	@InjectMocks
	UserService userservice;
	
	@Test
	public void useraddingTest() {
		UserDto user = new UserDto();
		
		user.setId("2100031401");
		user.setName("nitish");
		user.setMail("test@gmail.com");
		user.setPassword("test@123");
		user.setCourse("Btech");
		user.setDepartment("cse");
		UserEntity useradd = new UserEntity();
		
		useradd.setName(user.getName());
        useradd.setMail(user.getMail());
        useradd.setBusinessId(user.getId());
        useradd.setPassword(passwordEncoder.encode(user.getPassword()));
        useradd.setVerified(false);
        useradd.setCourse(user.getCourse());
        useradd.setDepartment(user.getDepartment());
        useradd.setVerificationToken(UUID.randomUUID().toString());
        useradd.setTokenGeneratedAt(LocalDateTime.now());
        
        when(userRepo.save(any(UserEntity.class))).thenReturn(useradd);
        UserEntity savedUser = userservice.save(user);
        assertEquals(user.getId(), savedUser.getBusinessId());
	}
	
	@Test
	public void userloginTest() {
		UserDto userdto = new UserDto();
	    userdto.setId("2100031401");
	    userdto.setPassword("thisispassword");
	    UserEntity userentity = new UserEntity();
	    userentity.setBusinessId("2100031401");
	    userentity.setPassword("encodedpassword");
	    userentity.setVerified(true);
	    userentity.setActiveSession(false);
	    when(userRepo.findByBusinessId(userdto.getId())).thenReturn(userentity);
	    when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
	    String result = userservice.UserLogin(userdto);
	    assertEquals("Success", result);
	}
	
	@Test
	public void userloginNotFoundTest() {
		UserDto userdto = new UserDto();
	    userdto.setId("2100031401");
	    userdto.setPassword("thisispassword");
	    UserEntity userentity = null;
	    when(userRepo.findByBusinessId(userdto.getId())).thenReturn(userentity);
	    String result = userservice.UserLogin(userdto);
	    assertEquals("No user Found", result);
	}
	
	@Test
	public void userAlreadyloginTest() {
		UserDto userdto = new UserDto();
	    userdto.setId("2100031401");
	    userdto.setPassword("thisispassword");
	    UserEntity userentity = new UserEntity();
	    userentity.setBusinessId("2100031401");
	    userentity.setPassword("encodedpassword");
	    userentity.setVerified(true);
	    userentity.setActiveSession(true);
	    when(userRepo.findByBusinessId(userdto.getId())).thenReturn(userentity);
	    String result = userservice.UserLogin(userdto);
	    assertEquals("Already LoggedIn", result);
	}
	
	@Test
	public void userloginEmailVerifiedOrNotTest() {
		UserDto userdto = new UserDto();
	    userdto.setId("2100031401");
	    userdto.setPassword("thisispassword");
	    UserEntity userentity = new UserEntity();
	    userentity.setBusinessId("2100031401");
	    userentity.setPassword("encodedpassword");
	    userentity.setVerified(false);
	    userentity.setActiveSession(false);
	    when(userRepo.findByBusinessId(userdto.getId())).thenReturn(userentity);
	    String result = userservice.UserLogin(userdto);
	    assertEquals("Verify Your email To Login", result);
	}
	
	@Test
	public void userloginPasswordNotMatchTest() {
		UserDto userdto = new UserDto();
	    userdto.setId("2100031401");
	    userdto.setPassword("thisispassword");
	    UserEntity userentity = new UserEntity();
	    userentity.setBusinessId("2100031401");
	    userentity.setPassword("encodedpassword");
	    userentity.setVerified(true);
	    userentity.setActiveSession(false);
	    when(userRepo.findByBusinessId(userdto.getId())).thenReturn(userentity);
	    when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);
	    String result = userservice.UserLogin(userdto);
	    assertEquals("Password Invalid", result);
	}
	
	@Test
	public void findUserTest() {
		String id = "testid";
		UserEntity userentity = new UserEntity();
		userentity.setBusinessId("testid");
		when(userRepo.findByBusinessId(id)).thenReturn(userentity);
		UserEntity founduser = userservice.findUser(id);
		assertEquals(id, founduser.getBusinessId());
	}
	
	@Test
	public void verifyUserTest() {
		String token = "verificationtojen";
		UserEntity user = new UserEntity();
		user.setTokenGeneratedAt(LocalDateTime.now());
		user.setVerificationToken(token);	 
		when(userRepo.findByVerificationToken(token)).thenReturn(user);
		String result = userservice.verifyUser(token);
		assertEquals("Verified", result);
	}
	
	@Test
	public void verifyUserNotFoundTest() {
		String token = "verificationtojen";
		UserEntity user = null;	 
		when(userRepo.findByVerificationToken(token)).thenReturn(user);
		String result = userservice.verifyUser(token);
		assertEquals("No User Found", result);
	}
	
	@Test
	public void verifyUserTokenExperiedTest() {
		String token = "verificationtojen";
		UserEntity user = new UserEntity();
		user.setTokenGeneratedAt(LocalDateTime.now().minusHours(2));
		user.setVerificationToken(token);	 
		when(userRepo.findByVerificationToken(token)).thenReturn(user);
		String result = userservice.verifyUser(token);
		assertEquals("Token Experied", result);
	}
	
	@Test
	public void addingresendVerifyTokenTest() {
		UserEntity user = new UserEntity();
		String email = "test@email.com";
		when(userRepo.findByMail(email)).thenReturn(user);
		String res = userservice.resendVerification(email);
		assertEquals("Sent", res);
	}
	
	@Test
	public void addingresendVerifyTokenForNullTest() {
		UserEntity user = null;
		String email = "test@email.com";
		when(userRepo.findByMail(email)).thenReturn(user);
		String res = userservice.resendVerification(email);
		assertEquals("No user found", res);
	}
	
	@Test
	public void addingresendVerifyTokenForVerifiedUserTest() {
		UserEntity user = new UserEntity();
		user.setVerified(true);;
		String email = "test@email.com";
		when(userRepo.findByMail(email)).thenReturn(user);
		String res = userservice.resendVerification(email);
		assertEquals("Verified User", res);
	}
	
	@Test
	public void addingPasswordResetTokenTest() {
		String id = "test_id";
		UserEntity user = new UserEntity();
		when(userRepo.findByBusinessId(id)).thenReturn(user);
		String res = userservice.generateResetToken(id);
		assertEquals("Sent", res);
	}
	
	@Test
	public void addingPasswordResetTokenForNullTest() {
		String id = "test_id";
		UserEntity user = null;
		when(userRepo.findByBusinessId(id)).thenReturn(user);
		String res = userservice.generateResetToken(id);
		assertEquals("No User Found for this Id/ Check the Id you Mentioned", res);
	}
	
	@Test
	public void verifyRestTokenTest() {
		String token = "resetToken";
		UserEntity user = new UserEntity();
		user.setBusinessId("test_id");
		user.setResetToken(token);
		user.setResetTokenGeneratedAt(LocalDateTime.now());
		when(userRepo.findByResetToken(token)).thenReturn(user);
		UserEntity resultantuser = userservice.verifyResetToken(token);
		assertEquals(user.getBusinessId(), resultantuser.getBusinessId());
	}
	
	@Test
	public void verifyRestTokenForNullTest() {
		String token = "resetToken";
		UserEntity user = null;
		when(userRepo.findByResetToken(token)).thenReturn(user);
		UserEntity resultantuser = userservice.verifyResetToken(token);
		assertEquals(null, resultantuser);
	}
	
	@Test
	public void verifyRestTokenExpiryTest() {
		String token = "resetToken";
		UserEntity user = new UserEntity();
		user.setBusinessId("test_id");
		user.setResetToken(token);
		user.setResetTokenGeneratedAt(LocalDateTime.now().minusMinutes(20));
		when(userRepo.findByResetToken(token)).thenReturn(user);
		UserEntity resultantuser = userservice.verifyResetToken(token);
		assertEquals(null, resultantuser);
	}
	
	@Test
	public void changePasswordTest() {
		String id = "tset_id";
		String newpassword = "newpass";
		UserEntity user = new UserEntity();
		when(userRepo.findByBusinessId(id)).thenReturn(user);
		UserEntity resultuser = userservice.changePass(id,newpassword);
		assertEquals(user, resultuser);
	}
	
	@Test
	public void settingActiveSessionTest() {
		String id = "test_id";
		UserEntity user = new UserEntity();
		user.setActiveSession(false);
		when(userRepo.findByBusinessId(id)).thenReturn(user);
		boolean res = userservice.settingActiveSession(id);
		assertEquals(false, res);
	}
	
	@Test
	public void settingActiveSessionForNullTest() {
		String id = "test_id";
		UserEntity user = null;
		when(userRepo.findByBusinessId(id)).thenReturn(user);
		boolean res = userservice.settingActiveSession(id);
		assertEquals(false, res);
	}
	
	@Test
	public void settingActiveSessionForActiveUserTest() {
		String id = "test_id";
		UserEntity user = new UserEntity();
		user.setActiveSession(true);
		when(userRepo.findByBusinessId(id)).thenReturn(user);
		boolean res = userservice.settingActiveSession(id);
		assertEquals(true, res);
	}
	
	@Test
	public void findByuserIdTest() {
		String id = "test_id";
		UserEntity user = new UserEntity();
		when(userRepo.findByBusinessId(id)).thenReturn(user);
		UserEntity resultantUser = userservice.findByuserId(id);
		assertEquals(user, resultantUser);
	}
	
}
