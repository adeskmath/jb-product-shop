package com.adeskmath.backend.shop.service;

import com.adeskmath.backend.shop.entity.Customer;
import com.adeskmath.backend.shop.repo.CustomerRepository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface CustomerService {
    Customer add(Customer customer);

    List<Customer> findLeastActive(Integer leastActiveNumber);

    List<Customer>findByProductMinTimes(String productName, Integer minTimes);

    List<Customer> findByExpenseRange(BigDecimal minValue, BigDecimal maxValue);

    List<Customer> findByLastName(String lastName);

    List<CustomerRepository.CustomerStatJSON> findByPurchasingPeriod(Date startDate, Date endDate);
}
