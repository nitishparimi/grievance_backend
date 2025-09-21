package com.example.demo.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.Entities.UserEntity;
import java.util.List;



@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long>{

	UserEntity findByMail(String mail);
	UserEntity findByBusinessId(String id);
	UserEntity findByVerificationToken(String verificationToken);
	UserEntity findByResetToken(String resetToken);

	List<UserEntity> findByRoleAndDepartment(String role, String department);
}
