package com.adeskmath.backend.shop.repo;

import com.adeskmath.backend.shop.entity.Customer;
import com.adeskmath.backend.shop.entity.Purchasing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface PurchasingRepository extends JpaRepository<Purchasing, Long> {


    // temp, TODO: remove
    @Query("SELECT p FROM Purchasing p WHERE p.purchasingDate BETWEEN :startDate AND :endDate" +
            " AND p.customer = :customer")
    List<Purchasing> findByParamsAndCustomer(@Param("startDate") Date startDate,
                                             @Param("endDate") Date endDate,
                                             @Param("customer") Customer customer);

    @Query("SELECT p FROM Purchasing p WHERE p.purchasingDate BETWEEN :startDate AND :endDate")
    List<Purchasing> findByParams(@Param("startDate") Date startDate,
                                  @Param("endDate") Date endDate);

    @Query("SELECT SUM(prod.price) FROM Purchasing p JOIN Product prod on p.product = prod " +
            "WHERE p.customer = :customer AND p.purchasingDate BETWEEN :startDate AND :endDate")
    BigDecimal getTotalExpenses(@Param("startDate") Date startDate,
                                @Param("endDate") Date endDate,
                                @Param("customer") Customer customer);

    @Query("SELECT SUM(prod.price) FROM Purchasing p JOIN Product prod on p.product = prod " +
            "WHERE p.purchasingDate BETWEEN :startDate AND :endDate")
    BigDecimal getGrandTotalExpenses(@Param("startDate") Date startDate,
                                     @Param("endDate") Date endDate);

    @Query("SELECT SUM(prod.price) / COUNT(DISTINCT c)" +
            " FROM Purchasing p JOIN Product prod on p.product = prod" +
            " JOIN Customer c ON p.customer = c" +
            " WHERE p.purchasingDate BETWEEN :startDate AND :endDate")
    BigDecimal getAvgExpenses(@Param("startDate") Date startDate,
                               @Param("endDate") Date endDate);
}
