package com.adeskmath.backend.shop.controller;

import com.adeskmath.backend.shop.entity.Customer;
import com.adeskmath.backend.shop.search.CustomerSearchExpenseRange;
import com.adeskmath.backend.shop.search.CustomerSearchProductMin;
import com.adeskmath.backend.shop.service.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/customer")
public class CustomerController {
    private final CustomerService customerService;
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    /** CRUD is not required by task, just 'add' for quick test*/
    @PostMapping("/add")
    public ResponseEntity<Customer> add(@RequestBody Customer customer) {
        if (customer.getId() != null && customer.getId() !=0) {
            return new ResponseEntity("redundant param: id must be null", HttpStatus.NOT_ACCEPTABLE);
        }
        if (customer.getLastName() == null || customer.getLastName().trim().length() == 0) {
            return new ResponseEntity("missed param: name, must be not empty", HttpStatus.NOT_ACCEPTABLE);
        }
        return ResponseEntity.ok(customerService.add(customer));
    }

    @PostMapping("/leastActive")
    public ResponseEntity<List<Customer>> searchListActive(@RequestBody Integer leastActiveNumber) {
        return ResponseEntity.ok(customerService.findLeastActive(leastActiveNumber));
    }

    @PostMapping("/expenseRange")
    public ResponseEntity<List<Customer>> searchExpenseRange(@RequestBody CustomerSearchExpenseRange range) {
        return ResponseEntity.ok(customerService.findByExpenseRange(range.getMinExpenses(), range.getMaxExpenses()));
    }

    @PostMapping("/productMin")
    public ResponseEntity<List<Customer>> searchProductMin(@RequestBody CustomerSearchProductMin data) {
        return ResponseEntity.ok(customerService.findByProductMinTimes(data.getProductName(), data.getMinTimes()));
    }

    @PostMapping("/lastName")
    public ResponseEntity<List<Customer>> searchLastName(@RequestBody String text) {
        return ResponseEntity.ok(customerService.findByLastName(text));
    }

}
