package com.adeskmath.backend.shop.service;

import com.adeskmath.backend.shop.entity.Customer;
import com.adeskmath.backend.shop.repo.CustomerRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository repository;

    public CustomerServiceImpl(CustomerRepository repository) {
        this.repository = repository;
    }

    @Override
    public Customer add(Customer customer) {
        return repository.save(customer);
    }

    @Override
    public List<Customer> findByLowestRank(Integer lowestRank) {
        return repository.findByLowestRank(lowestRank);
    }

    @Override
    public List<Customer> findByProductMinTimes(String productName, Integer minTimes) {
        return repository.findByProductMinTimes(productName, minTimes);
    }

    @Override
    public List<Customer> findByExpenseRange(BigDecimal minValue, BigDecimal maxValue) {
        return repository.findByExpenseRange(minValue, maxValue);
    }

    @Override
    public List<Customer> findByLastName(String lastName) {
        return repository.findByLastName(lastName);
    }

    @Override
    public List<String> getStat() {
        return repository.getStat();
    }

}
