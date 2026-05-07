package com.athiramk.ecommercesite.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.athiramk.ecommercesite.user.enums.UserRole;
import com.athiramk.ecommercesite.user.model.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long>{

	Optional<Role> findByName(UserRole role);
	

}
