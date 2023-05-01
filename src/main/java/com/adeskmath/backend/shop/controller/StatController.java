package com.adeskmath.backend.shop.controller;

import com.adeskmath.backend.shop.dto.ProductMapper;
import com.adeskmath.backend.shop.entity.Customer;
import com.adeskmath.backend.shop.search.PurchasingSearchPeriod;
import com.adeskmath.backend.shop.service.CustomerService;
import com.adeskmath.backend.shop.service.ProductService;
import com.adeskmath.backend.shop.service.PurchasingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/stat")
public class StatController {
    private final CustomerService customerService;
    private final ProductService productService;
    private final PurchasingService purchasingService;

    public StatController(CustomerService customerService, ProductService productService, PurchasingService purchasingService) {
        this.customerService = customerService;
        this.productService = productService;
        this.purchasingService = purchasingService;
    }

    @PostMapping("")
    public ResponseEntity<Map<String, Object>> stat(@RequestBody PurchasingSearchPeriod p) {
        Date startDate = p.getStartDate();
        Date endDate = p.getEndDate();
        List<Customer> customers = customerService.findByPurchasingPeriod(startDate, endDate);
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("type", "stat");
        map.put("totalDays", "calc working days");

        List<Map<String, Object>> customersDataList = new ArrayList<>();
        map.put("customers", customersDataList);
        for (Customer customer : customers) {
            Map<String, Object> customerData = new LinkedHashMap<>();
            customerData.put("name", customer.getName() + " " + customer.getLastName());
            customerData.put("purchases",
                    productService.findPurchasingPeriodAndCustomer(startDate, endDate, customer)
                            .stream().map(ProductMapper :: map).toList());
            customerData.put("totalExpenses", purchasingService.getTotalExpenses(customer));
            customersDataList.add(customerData);
        }
        map.put("totalExpenses","service...");
        map.put("avgExpenses","service...");

        return ResponseEntity.ok(map);
    }
}
