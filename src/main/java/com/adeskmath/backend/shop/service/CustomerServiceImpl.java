package com.adeskmath.backend.shop.service;

import com.adeskmath.backend.shop.entity.Customer;
import com.adeskmath.backend.shop.repo.CustomerRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
@Transactional
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
    public List<Customer> findLeastActive(Integer leastActiveNumber) {
        return repository.findLeastActive(leastActiveNumber);
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
    public List<Customer> findByLastName(String text) {
        return repository.findByLastNameContainsIgnoreCase(text);
    }

    @Override
    public List<CustomerRepository.CustomerStatJSON> findByPurchasingPeriod(Date startDate, Date endDate) {
        return repository.customerByDate(startDate, endDate);
    }


    /*@Override
    public List<String> getStat() {
        return repository.getStat();
    }*/

//    @Override
//    public Map<Customer, Product> testStat() {
//        return repository.testStat();
//    }

}
