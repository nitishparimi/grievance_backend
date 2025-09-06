package com.example.demo.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.DaoServices.*;
import com.example.demo.Dto.UserDto;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/users")
public class MainController {
	
	@Autowired
	UserService userserv;
	
	
	@GetMapping("/greeter")
	public String greeter() {
		return "Welcome to website";
	}
	
	
	@PostMapping("/register")
	public String register(@Valid @RequestBody UserDto u) {
		
		userserv.save(u);
		return "Success";
	}
}

