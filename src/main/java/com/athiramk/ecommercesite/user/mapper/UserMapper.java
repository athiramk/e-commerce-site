package com.athiramk.ecommercesite.user.mapper;

import java.util.List;
import java.util.stream.Collectors;

import com.athiramk.ecommercesite.user.dto.RegisterRequest;
import com.athiramk.ecommercesite.user.dto.UserResponse;
import com.athiramk.ecommercesite.user.enums.Status;
import com.athiramk.ecommercesite.user.model.User;

public class UserMapper {

    public static User toEntity(RegisterRequest request) {
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setStatus(Status.ACTIVE);
        return user;
    }

    public static UserResponse toResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        List<String> roleNames = user.getRoles()
                .stream()
                .map(role -> role.getName().toString())
                .collect(Collectors.toList());
        response.setRole(roleNames);
        return response;
    }
}
