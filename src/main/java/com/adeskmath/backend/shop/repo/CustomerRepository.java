package com.adeskmath.backend.shop.repo;

import com.adeskmath.backend.shop.entity.Customer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
//    @Query("SELECT c FROM Customer c" +
//            " JOIN Purchasing p ON p.customer = c" +
//            " WHERE p.purchasingDate BETWEEN :startDate AND :endDate")
//    List<Customer> findByPurchasingPeriod(@Param("startDate") Date startDate,
//                                          @Param("endDate") Date endDate);
    List<Customer> findByLastNameContainsIgnoreCase(String text);

    @Query("SELECT c FROM Customer c " +
            "INNER JOIN Purchasing p ON c = p.customer " +
            "INNER JOIN Product prod ON p.product = prod " +
            "GROUP BY c " +
            "HAVING SUM(prod.price) between :minValue and :maxValue")
    List<Customer> findByExpenseRange(@Param("minValue") BigDecimal minValue,
                                      @Param("maxValue") BigDecimal maxValue);

    @Query("SELECT c FROM Customer c " +
            "INNER JOIN c.purchases pur " +
            "WHERE pur.product.name ILIKE %:productName% " +
            "GROUP BY c " +
            "HAVING COUNT(c) >= :minTimes")
    List<Customer> findByProductMinTimes(@Param("productName") String productName,
                                         @Param("minTimes") Integer minTimes);

    @Query("SELECT c FROM Customer c " +
            "INNER JOIN Purchasing p ON c = p.customer " +
            "GROUP BY c " +
            "ORDER BY COUNT(p) DESC " +
            "LIMIT :leastActiveNumber")
    List<Customer> findLeastActive(@Param("leastActiveNumber") Integer leastActiveNumber);

    @Query(value = "select concat(c.last_name, ' ', c.name) as customerName, prod.name as productName, " +
            "sum(prod.price) as expenses from shop.customer c " +
            "inner join shop.purchasing pur on c.id = pur.customer_id " +
            "inner join shop.product prod on prod.id = pur.product_id " +
            "where pur.purchasing_date between :dateFrom and :dateTo " +
            "group by c.id, prod.name", nativeQuery = true)
    List<CustomerStatJSON> customerByDate(@Param("dateFrom") Date dateFrom, @Param("dateTo") Date dateTo);

    // interface-based projection (Spring Data JPA doc)
    interface CustomerStatJSON {
        @JsonIgnore
        String getCustomerName();
        String getProductName();
        BigDecimal getExpenses();
    }
}
