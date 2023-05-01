package com.adeskmath.backend.shop.service;

import com.adeskmath.backend.shop.entity.Customer;
import com.adeskmath.backend.shop.entity.Purchasing;
import com.adeskmath.backend.shop.repo.PurchasingRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class PurchasingServiceImpl implements PurchasingService {
    private final PurchasingRepository repository;

    public PurchasingServiceImpl(PurchasingRepository repository) {
        this.repository = repository;
    }

    @Override
    public Purchasing add(Purchasing purchasing) {
        return repository.save(purchasing);
    }

    @Override
    public List<Purchasing> findAllByParams(Date startDate, Date endDate) {
        return repository.findByParams(startDate, endDate);
    }

    @Override
    public BigDecimal getTotalExpenses(Date startDate, Date endDate,Customer customer) {
        return repository.getTotalExpenses(startDate, endDate, customer);
    }

    @Override
    public BigDecimal getGrandTotalExpenses(Date startDate, Date endDate) {
        return repository.getGrandTotalExpenses(startDate, endDate);
    }

    @Override
    public BigDecimal getAvgExpenses(Date startDate, Date endDate) {
        return repository.getAvgExpenses(startDate, endDate);
    }


    @Override
    public List<Purchasing> findByParamsAndCustomer(Date startDate, Date endDate, Customer customer) {
        return repository.findByParamsAndCustomer(startDate, endDate, customer);
    }
}
