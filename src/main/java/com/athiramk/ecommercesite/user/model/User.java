package com.athiramk.ecommercesite.user.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Version;


enum Status {
	ACTIVE, SUSPENDED, DELETED
}

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "users", 
uniqueConstraints = {
		@UniqueConstraint(name = "uq_users_email", columnNames = "email")
}, 
indexes = {
		@Index(name = "idx_users_email", columnList = "email")
})
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "full_name", nullable = false)
	private String name;
	
	@Column(nullable = false)
	private String email;
	
	@Column(nullable = false)
	private String password;
	
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private Status status = Status.ACTIVE;
	
	@Column(nullable = false)
	@Version
	private Integer version = 0;
	
	@CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;
	
	@LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
			name = "user_roles",
			joinColumns = @JoinColumn(name = "user_id"),
			inverseJoinColumns = @JoinColumn(name = "role_id")
			
			)
	private Set<Role> roles = new HashSet<Role>();
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
	private List<Address> addresses = new ArrayList<>();
		
}
