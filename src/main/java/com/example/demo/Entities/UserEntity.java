package com.example.demo.Entities;


import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name = "users")
public class UserEntity {
		
		public Long getS_no() {
			return s_no;
		}

		public void setS_no(Long s_no) {
			this.s_no = s_no;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getMail() {
			return mail;
		}

		public void setMail(String mail) {
			this.mail = mail;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		public String getRole() {
			return role;
		}

		public void setRole(String role) {
			this.role = role;
		}

		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY) 
		private Long s_no;
		
		private String name;
		
		@Column(unique = true, nullable = false)
		private String mail;
		
		private String password;
		
		@Column(unique = true, nullable = false)
		private String businessId;
		
		public String getBusinessId() {
			return businessId;
		}

		public void setBusinessId(String businessId) {
			this.businessId = businessId;
			assignRole();
		}

		private String role;
		
		private String course;
		
		private String department;
		
		private String profileUrl;
		
		private boolean activeSession = false;

		public boolean isActiveSession() {
		    return activeSession;
		}

		public void setActiveSession(boolean activeSession) {
		    this.activeSession = activeSession;
		}

		
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

		public String getProfileUrl() {
			return profileUrl;
		}

		public void setProfileUrl(String profileUrl) {
			this.profileUrl = profileUrl;
		}

		public boolean isVerified() {
			return isVerified;
		}

		public void setVerified(boolean isVerified) {
			this.isVerified = isVerified;
		}

		public String getVerificationToken() {
			return verificationToken;
		}

		public void setVerificationToken(String verificationToken) {
			this.verificationToken = verificationToken;
		}

		public LocalDateTime getTokenGeneratedAt() {
			return tokenGeneratedAt;
		}

		public void setTokenGeneratedAt(LocalDateTime tokenGeneratedAt) {
			this.tokenGeneratedAt = tokenGeneratedAt;
		}

		private boolean isVerified;
		
		private String verificationToken;
		
		 private LocalDateTime tokenGeneratedAt;
		
		 private String resetToken;
		 private LocalDateTime resetTokenGeneratedAt;
		 
		public String getResetToken() {
			return resetToken;
		}

		public void setResetToken(String resetToken) {
			this.resetToken = resetToken;
		}

		public LocalDateTime getResetTokenGeneratedAt() {
			return resetTokenGeneratedAt;
		}

		public void setResetTokenGeneratedAt(LocalDateTime resetTokenGeneratedAt) {
			this.resetTokenGeneratedAt = resetTokenGeneratedAt;
		}

		private void assignRole() {
	        if (businessId != null) {
	            if (businessId.matches("\\d{10}")) {     // exactly 10 digits
	                this.role = "STUDENT";
	            } else if (businessId.matches("\\d{4}")) {  // exactly 4 digits
	                this.role = "STAFF";
	            } else {
	                this.role = "UNKNOWN"; // Or throw exception if invalid
	            }
	        }
		
		}

		
		
}



