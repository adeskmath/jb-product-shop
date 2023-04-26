package com.adeskmath.backend.shop.controller;

import com.adeskmath.backend.shop.entity.Customer;
import com.adeskmath.backend.shop.search.SearchExpenseRange;
import com.adeskmath.backend.shop.search.SearchLastName;
import com.adeskmath.backend.shop.search.SearchLowestRank;
import com.adeskmath.backend.shop.search.SearchProductMin;
import com.adeskmath.backend.shop.service.CustomerService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/customer")
public class CustomerController {
    private final CustomerService customerService;
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/search")
    public ResponseEntity<Map<String, Object>> search(@RequestBody JsonNode searchCriteria) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(searchCriteria.traverse());
        rootNode.path("criterias").forEach(System.out::println);

        List<Map<String, Object>> mapList = new ArrayList<>();

        rootNode.path("criterias").forEach(jsonNode -> {
            Map<String, Object> map = new HashMap<>();
            String criteria = jsonNode.fieldNames().next();
            List<Customer> list = switch (criteria) {
                case "lastName" -> customerService.findBylastName(jsonNode.path("lastName").asText());
                case "minExpenses" ->
                        customerService.findByExpenseRange(jsonNode.path("minExpenses").decimalValue(), jsonNode.path("maxExpenses").decimalValue());
                case "productName" ->
                        customerService.findByProductMinTimes(jsonNode.path("productName").asText(), jsonNode.path("minTimes").asInt());
                case "lowestRank" -> customerService.findByLowestRank(jsonNode.path("lowestRank").asInt());
                default -> null;
            };
            map.put("criteria", jsonNode);
            map.put("results", list);
            mapList.add(map);
        });
        return ResponseEntity.ok(generateResponse(mapList));
    }

    // протестил как формировать ответ = критерий+ответ сервиса
    @PostMapping("/search0")
    public ResponseEntity<Map<String, Object>> search0(@RequestBody SearchLowestRank lowestRank) {
        List<Customer> list = customerService.findByLowestRank(lowestRank.getLowestRank());
        var criteriaTest = lowestRank;
        Map<String, Object> map = new HashMap<>();
        map.put("criteria", criteriaTest);
        map.put("results", list);

        return ResponseEntity.ok(map);
    }

    @PostMapping("/search4")
    public ResponseEntity<List<Customer>> search4(@RequestBody SearchLowestRank lowestRank) {
        List<Customer> list = customerService.findByLowestRank(lowestRank.getLowestRank());
        return ResponseEntity.ok(list);
    }

    @PostMapping("/search3")
    public ResponseEntity<List<Customer>> search3(@RequestBody SearchExpenseRange expenseRange) {
        List<Customer> list = customerService.findByExpenseRange(expenseRange.getMinExpenses(), expenseRange.getMaxExpenses());
        return ResponseEntity.ok(list);
    }

    @PostMapping("/search2")
    public ResponseEntity<List<Customer>> search2(@RequestBody SearchProductMin productMin) {
        List<Customer> list = customerService.findByProductMinTimes(productMin.getProductName(), productMin.getMinTimes());
        return ResponseEntity.ok(list);
    }

    @PostMapping("/search1")
    public ResponseEntity<List<Customer>> search1(@RequestBody SearchLastName lastName) {
        List<Customer> list = customerService.findBylastName(lastName.getLastName());
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

    // принимает все (list) результаты (критерий+ответ) и формирует общий ответ
    private static Map<String, Object> generateResponse(List<Map<String, Object>> mapList) {
        Map<String, Object> map = new HashMap<>();
        map.put("type", "search");
        map.put("results", mapList);
        return map;
    }
}
