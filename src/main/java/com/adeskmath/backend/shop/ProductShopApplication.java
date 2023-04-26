package com.adeskmath.backend.shop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class ProductShopApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductShopApplication.class, args);
	}

}
