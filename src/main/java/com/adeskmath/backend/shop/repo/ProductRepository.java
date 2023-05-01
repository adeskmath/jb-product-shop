package com.adeskmath.backend.shop.repo;

import com.adeskmath.backend.shop.entity.Customer;
import com.adeskmath.backend.shop.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT prod FROM Product prod" +
            " JOIN Purchasing p ON p.product = prod" +
            " JOIN Customer c ON c = p.customer" +
            " WHERE p.purchasingDate BETWEEN :startDate AND :endDate" +
            " AND p.customer = :customer")
    List<Product> findByPurchasingPeriodAndCustomer(@Param("startDate") Date startDate,
                                                    @Param("endDate") Date endDate,
                                                    @Param("customer") Customer customer);
}
