package com.athiramk.ecommercesite.user.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * CREATE TABLE addresses (
    id              BIGSERIAL       NOT NULL,
    user_id         BIGINT          NOT NULL,
    address_line1   VARCHAR(255)    NOT NULL,
    address_line2   VARCHAR(255),
    city            VARCHAR(100)    NOT NULL,
    state           VARCHAR(100)    NOT NULL,
    postal_code     VARCHAR(20)     NOT NULL,
    country         VARCHAR(100)    NOT NULL    DEFAULT 'Australia',
    is_default      BOOLEAN         NOT NULL    DEFAULT FALSE,

    CONSTRAINT pk_addresses PRIMARY KEY (id),

    CONSTRAINT fk_addresses_user
        FOREIGN KEY (user_id) REFERENCES users (id)
        ON DELETE CASCADE
);

CREATE INDEX idx_addresses_user_id ON addresses (user_id);
 */

@Entity
@Table(name = "addresses", indexes = {
		@Index(name = "idx_addresses_user_id", columnList = "user_id")
})
public class Address {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	//@Column(name = "user_id", nullable = false)
	//private Long userId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;
	
	@Column(name = "address_line1", nullable = false)
	private String addressLine1;
	
	@Column(name = "address_line2")
	private String addressLine2;
	
	@Column(nullable = false)
	private String city;
	
	@Column(nullable = false)
	private String state;
	
	@Column(name = "postal_code", nullable = false)
	private String postalCode;
	
	@Column(nullable = false)
	private String country;
	
	@Column(name ="is_default", nullable = false)
	private boolean isDefault = false;

}
