package com.adeskmath.backend.shop.service;

import com.adeskmath.backend.shop.entity.Product;
import com.adeskmath.backend.shop.repo.ProductRepository;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository repository;

    public ProductServiceImpl(ProductRepository repository) {
        this.repository = repository;
    }

    @Override
    public Product add(Product product) {
        return repository.save(product);
    }
}
