package com.adeskmath.backend.shop.controller;

import com.adeskmath.backend.shop.entity.Customer;
import com.adeskmath.backend.shop.search.SearchExpenseRange;
import com.adeskmath.backend.shop.search.SearchLastName;
import com.adeskmath.backend.shop.search.SearchLowestRank;
import com.adeskmath.backend.shop.search.SearchProductMin;
import com.adeskmath.backend.shop.service.CustomerService;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.adeskmath.backend.shop.utilities.JsonOperator.getResults;

@RestController
@RequestMapping("/customer")
public class CustomerController {
    private final CustomerService customerService;
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/search")
    public ResponseEntity<Map<String, Object>> search(@RequestBody JsonNode searchCriteria) throws IOException {
        return ResponseEntity.ok(getResults(searchCriteria, customerService));
    }

    // протестил как формировать ответ = критерий+ответ сервиса
    @PostMapping("/search/test")
    public ResponseEntity<Map<String, Object>> searchTest(@RequestBody SearchLowestRank lowestRank) {
        List<Customer> list = customerService.findByLowestRank(lowestRank.getLowestRank());
        var criteriaTest = lowestRank;
        Map<String, Object> map = new HashMap<>();
        map.put("criteria", criteriaTest);
        map.put("results", list);

        return ResponseEntity.ok(map);
    }

    @PostMapping("/search/lowestRank")
    public ResponseEntity<List<Customer>> searchLowestRank(@RequestBody SearchLowestRank lowestRank) {
        List<Customer> list = customerService.findByLowestRank(lowestRank.getLowestRank());
        return ResponseEntity.ok(list);
    }

    @PostMapping("/search/expenseRange")
    public ResponseEntity<List<Customer>> searchExpenseRange(@RequestBody SearchExpenseRange expenseRange) {
        List<Customer> list = customerService.findByExpenseRange(expenseRange.getMinExpenses(), expenseRange.getMaxExpenses());
        return ResponseEntity.ok(list);
    }

    @PostMapping("/search/productMin")
    public ResponseEntity<List<Customer>> searchProductMin(@RequestBody SearchProductMin productMin) {
        List<Customer> list = customerService.findByProductMinTimes(productMin.getProductName(), productMin.getMinTimes());
        return ResponseEntity.ok(list);
    }

    @PostMapping("/search/lastName")
    public ResponseEntity<List<Customer>> searchLastName(@RequestBody SearchLastName lastName) {
        List<Customer> list = customerService.findByLastName(lastName.getLastName());
        return ResponseEntity.ok(list);
    }

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

}
