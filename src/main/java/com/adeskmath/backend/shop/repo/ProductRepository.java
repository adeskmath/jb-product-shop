package com.adeskmath.backend.shop.repo;

import com.adeskmath.backend.shop.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
