package com.adeskmath.backend.shop.service;

import com.adeskmath.backend.shop.entity.Customer;
import com.adeskmath.backend.shop.entity.Purchasing;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface PurchasingService {
    Purchasing add(Purchasing purchasing);
    List<Purchasing> findAllByParams(Date startDate, Date endDate);

    BigDecimal getTotalExpenses(Date startDate, Date endDate, Customer customer);

    BigDecimal getGrandTotalExpenses(Date startDate, Date endDate);

    BigDecimal getAvgExpenses(Date startDate, Date endDate);

    List<Purchasing> findByParamsAndCustomer(Date startDate, Date endDate, Customer customer);
}
