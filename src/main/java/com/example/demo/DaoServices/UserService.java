package com.example.demo.DaoServices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Entities.UserEntity;
import com.example.demo.Repositories.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.example.demo.Dto.*;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepo;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserEntity save(UserDto dto) {
        UserEntity user = new UserEntity();
        user.setName(dto.getName());
        user.setMail(dto.getMail());
        user.setId(dto.getId());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        return userRepo.save(user);
    }
}