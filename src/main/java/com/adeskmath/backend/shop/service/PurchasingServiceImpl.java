package com.adeskmath.backend.shop.service;

import com.adeskmath.backend.shop.entity.Purchasing;
import com.adeskmath.backend.shop.repo.PurchasingRepository;
import org.springframework.stereotype.Service;

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
}
