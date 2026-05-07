package com.athiramk.ecommercesite.user.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.athiramk.ecommercesite.common.exception.DuplicateResourceException;
import com.athiramk.ecommercesite.common.exception.ResourceNotFoundException;
import com.athiramk.ecommercesite.user.dto.AddressRequest;
import com.athiramk.ecommercesite.user.dto.AddressResponse;
import com.athiramk.ecommercesite.user.dto.RegisterRequest;
import com.athiramk.ecommercesite.user.dto.UserResponse;
import com.athiramk.ecommercesite.user.enums.Status;
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
		Optional<User> optional= userRepository.findByEmail(request.getEmail());
		if (optional.isPresent()) {
			throw new DuplicateResourceException("user", "email", request.getEmail());
		}
		User user = new User();
		user.setName(request.getName());
		user.setEmail(request.getEmail());
		user.setPassword(request.getPassword());
		Optional<Role> validRole = roleRepository.findByName(request.getRole());
		if (validRole.isEmpty()) {
			throw new ResourceNotFoundException("role", "name", request.getRole());
		}
		Role role = validRole.get();
		Set<Role> roles = new HashSet<>();
		roles.add(role);
		
		user.setRoles(roles);
		user.setStatus(Status.ACTIVE);
		
		User savedUser = userRepository.save(user);
		
		UserResponse userResponse = new UserResponse();
		userResponse.setId(savedUser.getId());
		userResponse.setName(savedUser.getName());
		userResponse.setEmail(savedUser.getEmail());
		List<String> roleNames = savedUser.getRoles()
				.stream()
				.map(role1 -> role1.getName())
				.collect(Collectors.toList());
		userResponse.setRole(roleNames);
		
		return userResponse;
		
	}
	
	//public UserResponse login(LoginRequest request) {}
	
	//public UserResponse findUserById(Long id) {}
	
	//public UserResponse suspendUser(Long id) { }

}
