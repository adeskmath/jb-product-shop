package com.adeskmath.backend.shop.service;

import com.adeskmath.backend.shop.json.search.CustomerResult;
import com.adeskmath.backend.shop.json.search.SearchResult;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;

@Service
public class JsonService {
    private final CustomerService customerService;

    public JsonService(CustomerService customerService) {
        this.customerService = customerService;
    }

    public SearchResult searchWithJson(LinkedHashMap<String, List<Object>> map) {

        SearchResult searchResult = new SearchResult();

        List<Object> criterias = map.get("criterias");

        criterias.forEach(criteria -> {
            LinkedHashMap<String, Object> criteriaMap = (LinkedHashMap<String, Object>) criteria;
            CustomerResult resultByCriteria = new CustomerResult();
            resultByCriteria.setCriteria(criteria);

            if (criteriaMap.containsKey("lastName")) {
                resultByCriteria.setResults(customerService.findByLastName(criteriaMap.get("lastName").toString()));

            } else if (criteriaMap.containsKey("productName")) {
                resultByCriteria.setResults(customerService.findByProductMinTimes(
                        criteriaMap.get("productName").toString(),
                        Integer.valueOf(criteriaMap.get("minTimes").toString())
                ));

            } else if (criteriaMap.containsKey("minExpenses")) {
                resultByCriteria.setResults(customerService.findByExpenseRange(
                        new BigDecimal(criteriaMap.get("minExpenses").toString()),
                        new BigDecimal(criteriaMap.get("maxExpenses").toString())
                ));

            } else if (criteriaMap.containsKey("leastActiveCustomers")) {
                resultByCriteria.setResults(customerService.findLeastActive(
                        Integer.valueOf(criteriaMap.get("leastActiveCustomers").toString())
                ));
            }

            searchResult.getResults().add(resultByCriteria);

        });

        return searchResult;

    }
}
