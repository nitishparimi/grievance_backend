package com.example.demo.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.DaoServices.*;
import com.example.demo.Dto.UserDto;
import com.example.demo.Entities.ApiResponse;
import com.example.demo.Entities.UserEntity;
import com.example.demo.jjwtAuth.CustomUserDetailsService;
import com.example.demo.jjwtAuth.JwtUtil;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/users")
public class MainController {

    @Autowired
    private UserService userserv;
    
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;


    @Value("${user.change-password.url}")
    private String change_password;

    @GetMapping("/greeter")
    public ApiResponse<String> greeter() {
        return new ApiResponse<>("success", "Welcome to website", null);
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserEntity>> register(@Valid @RequestBody UserDto u) {
        UserEntity us = userserv.save(u);
        if (us != null) {
            return ResponseEntity.ok(new ApiResponse<>("success", "User Registered", us));
        }
        return ResponseEntity.badRequest().body(new ApiResponse<>("error", "Registration failed", null));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Object>> loginuser(@RequestBody UserDto u) {
    	System.out.println(u.getId());
        String s = userserv.UserLogin(u);
        if (s.equals("Success")) {
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(u.getId());
            String token = jwtUtil.generateToken(userDetails);  // Generate JWT properly
            return ResponseEntity.ok(new ApiResponse<>("success", "User Logged in", token));
        }
        return ResponseEntity.badRequest().body(new ApiResponse<>("error", s, null));
    }
    
    

    @GetMapping("/verifyuser")
    public ResponseEntity<ApiResponse<Object>> verifyUser(@RequestParam("token") String token) {
        String verificationStatus = userserv.verifyUser(token);
        if (verificationStatus.equals("Verified")) {
            return ResponseEntity.ok(new ApiResponse<>("success", "Email Verified", null));
        }
        return ResponseEntity.badRequest().body(new ApiResponse<>("error", verificationStatus, null));
    }

    @PostMapping("/verificationresend")
    public ResponseEntity<ApiResponse<Object>> resendverification(@RequestParam("email") String email) {
        String s = userserv.resendVerification(email);
        if (s.equals("Sent")) {
            return ResponseEntity.ok(new ApiResponse<>("success", "Verification Email Sent", null));
        }
        return ResponseEntity.badRequest().body(new ApiResponse<>("error", s, null));
    }

    @PostMapping("/sendResetToken")
    public ResponseEntity<ApiResponse<Object>> sendResetToken(@RequestParam("id") String id) {
        String s = userserv.generateResetToken(id);
        if (s.equals("Sent")) {
            return ResponseEntity.ok(new ApiResponse<>("success", "Reset Token Sent", null));
        }
        return ResponseEntity.badRequest().body(new ApiResponse<>("error", s, null));
    }

    @GetMapping("/verifyResetToken")
    public ResponseEntity<String> verifyResetToken(@RequestParam("resettoken") String token) {
        UserEntity u = userserv.verifyResetToken(token);
        if (u != null) {
            String htmlForm = "<html>"
                    + "<body>"
                    + "<h3>Enter New Password</h3>"
                    + "<form action=\"" + change_password + u.getBusinessId() + "\" method=\"POST\">"
                    + "New Password: <input type=\"password\" name=\"password\" />"
                    + "<button type=\"submit\">Change Password</button>"
                    + "</form>"
                    + "</body>"
                    + "</html>";

            return ResponseEntity.ok().header("Content-Type", "text/html").body(htmlForm);
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid user ID or token");
    }

    @PostMapping("/password-change")
    public ResponseEntity<ApiResponse<Object>> changePassword(
            @RequestParam("id") String id,
            @RequestParam String password) {

        UserEntity u = userserv.changePass(id, password);
        if (u != null) {
            return ResponseEntity.ok(new ApiResponse<>("success", "Password Changed Successfully", u));
        }

        return ResponseEntity.badRequest().body(new ApiResponse<>("error", "Invalid user ID", null));
    }
    
    

}


