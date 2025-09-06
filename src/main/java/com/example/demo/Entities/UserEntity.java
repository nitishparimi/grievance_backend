package com.example.demo.Entities;


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
		private String id;
		
		private String role;
		
		
		private void assignRole() {
	        if (id != null) {
	            if (id.matches("\\d{10}")) {     // exactly 10 digits
	                this.role = "STUDENT";
	            } else if (id.matches("\\d{4}")) {  // exactly 4 digits
	                this.role = "STAFF";
	            } else {
	                this.role = "UNKNOWN"; // Or throw exception if invalid
	            }
	        }
		
		}

		public void setId(String id) {
			this.id = id;
			assignRole();
		}
		
}



