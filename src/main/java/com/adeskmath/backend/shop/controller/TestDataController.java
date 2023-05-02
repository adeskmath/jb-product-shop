package com.adeskmath.backend.shop.controller;

import com.adeskmath.backend.shop.entity.Customer;
import com.adeskmath.backend.shop.entity.Product;
import com.adeskmath.backend.shop.entity.Purchasing;
import com.adeskmath.backend.shop.service.CustomerService;
import com.adeskmath.backend.shop.service.ProductService;
import com.adeskmath.backend.shop.service.PurchasingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/testdata")
public class TestDataController {
    private final CustomerService customerService;
    private final ProductService productService;
    private final PurchasingService purchasingService;

    public TestDataController(CustomerService customerService, ProductService productService, PurchasingService purchasingService) {
        this.customerService = customerService;
        this.productService = productService;
        this.purchasingService = purchasingService;
    }

    @PostMapping("/init")
    public ResponseEntity<Boolean> init(){
        //customers
        List<Customer> customers = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Customer c = new Customer();
            c.setName("Имя " + i);
            c.setLastName("Фамилия " + i);
            customers.add(customerService.add(c));
        }

        //products
        List<Product> products = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Product p = new Product();
            p.setName("Продукт " + i);
            p.setPrice(BigDecimal.valueOf(i * 10 + 50));
            products.add(productService.add(p));
        }
        // purchasing
        LocalDate startDate = LocalDate.now();
        for (int i = 0; i < 10; i++) {
            Purchasing p = new Purchasing();
            p.setCustomer(customers.get(i));
            p.setProduct(products.get(i));
            p.setPurchasingDate(Date.valueOf(startDate.plusDays(i)));
            purchasingService.add(p);
        }

        return ResponseEntity.ok(true);
    }
}
