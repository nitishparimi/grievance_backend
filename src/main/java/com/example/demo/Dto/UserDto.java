package com.example.demo.Dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class UserDto {

    @NotBlank
    private String name;

    @Email
    @NotBlank
    private String mail;

    @NotBlank
    private String password;

    @NotBlank
    @Pattern(regexp="\\d{4}|\\d{10}")
    private String id;
    
    @NotBlank
    private String course;
    
    @NotBlank
    private String department;

    public String getCourse() {
		return course;
	}
	public void setCourse(String course) {
		this.course = course;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	// getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getMail() { return mail; }
    public void setMail(String mail) { this.mail = mail; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
}
