package com.adeskmath.backend.shop.repo;

import com.adeskmath.backend.shop.entity.Purchasing;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchasingRepository extends JpaRepository<Purchasing, Long> {
}
