package com.adeskmath.backend.shop.service;

import com.adeskmath.backend.shop.entity.Customer;
import com.adeskmath.backend.shop.entity.Product;

import java.util.Date;
import java.util.List;

public interface ProductService {
    Product add(Product product);

    List<Product> findPurchasingPeriodAndCustomer(Date startDate, Date endDate, Customer customer);
}
