package com.adeskmath.backend.shop.dto;

import com.adeskmath.backend.shop.entity.Customer;

public class CustomerMapper {
    public static DtoCustomer map(Customer customer) {
        DtoCustomer dtoCustomer = new DtoCustomer();
        dtoCustomer.setName(customer.getName());
        dtoCustomer.setLastName(customer.getLastName());
        return dtoCustomer;
    }
}
