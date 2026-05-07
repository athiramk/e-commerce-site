package com.athiramk.ecommercesite;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing 
public class ECommerceSiteApplication {


	public static void main(String[] args) {
        SpringApplication.run(ECommerceSiteApplication.class, args);
    }
}