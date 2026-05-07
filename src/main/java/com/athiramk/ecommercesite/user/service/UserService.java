package com.athiramk.ecommercesite.user.service;


import org.springframework.stereotype.Service;

import com.athiramk.ecommercesite.common.exception.DuplicateResourceException;
import com.athiramk.ecommercesite.common.exception.ResourceNotFoundException;

import com.athiramk.ecommercesite.user.dto.RegisterRequest;
import com.athiramk.ecommercesite.user.dto.UserResponse;

import com.athiramk.ecommercesite.user.mapper.UserMapper;
import com.athiramk.ecommercesite.user.model.Role;
import com.athiramk.ecommercesite.user.model.User;
import com.athiramk.ecommercesite.user.repository.RoleRepository;
import com.athiramk.ecommercesite.user.repository.UserRepository;


@Service
public class UserService {
	
	UserRepository userRepository;
	RoleRepository roleRepository;
	
	
	public UserService(UserRepository userRepository, RoleRepository roleRepository) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
	}

	public UserResponse register(RegisterRequest request) {
		//step 1: check if email exists
		if (userRepository.findByEmail(request.getEmail()).isPresent()) {
			throw new DuplicateResourceException("user", "email", request.getEmail());
		}
		
		//step 2: build entity
		User user = UserMapper.toEntity(request);
		
		
		// step 3: fetch and assign role
	    Role role = roleRepository.findByName(request.getRole())
	            .orElseThrow(() -> new ResourceNotFoundException("role", "name", request.getRole()));
	    user.getRoles().add(role);
		
	    // step 4: save and return
	    return UserMapper.toResponse(userRepository.save(user));
		
	}
	
	//public UserResponse login(LoginRequest request) {}
	
	//public UserResponse findUserById(Long id) {}
	
	//public UserResponse suspendUser(Long id) { }

}
