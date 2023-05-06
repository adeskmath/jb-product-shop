package com.adeskmath.backend.shop.service;

import com.adeskmath.backend.shop.json.search.CustomerResult;
import com.adeskmath.backend.shop.json.search.SearchResult;
import com.adeskmath.backend.shop.json.stat.CustomerPurchases;
import com.adeskmath.backend.shop.json.stat.StatResult;
import com.adeskmath.backend.shop.repo.CustomerRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.TimeUnit;

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

    public StatResult statWithJson(Date startDate, Date endDate) {
        List<CustomerRepository.CustomerStatJSON> purchases = customerService.findByPurchasingPeriod(startDate, endDate);
        Map<String, List<CustomerRepository.CustomerStatJSON>> map = new HashMap<>();

        purchases.forEach(purchase -> {
            List list;
            if (!map.containsKey(purchase.getCustomerName())) {
                list = new ArrayList<>();
            } else {
                list = map.get(purchase.getCustomerName());
            }
            list.add(purchase);
            map.put(purchase.getCustomerName(), list);
        });

        StatResult statResult = new StatResult();
        List<CustomerPurchases> customers = new ArrayList<>();
        statResult.setCustomers(customers);

        long diffDates = Math.abs(startDate.getTime() - endDate.getTime());
        long diffDays = TimeUnit.DAYS.convert(diffDates, TimeUnit.MILLISECONDS);
        statResult.setTotalDays(diffDays + 1);

        BigDecimal totalExpensesAll = BigDecimal.ZERO;

        for (Map.Entry<String, List<CustomerRepository.CustomerStatJSON>> entry : map.entrySet()) {
            CustomerPurchases customer = new CustomerPurchases();
            customer.setName(entry.getKey());
            customer.setPurchases(entry.getValue());

            BigDecimal customerTotalExpenses = BigDecimal.ZERO;
            for (CustomerRepository.CustomerStatJSON purchase : entry.getValue()) {
                customerTotalExpenses = customerTotalExpenses.add(purchase.getExpenses());
            }

            customer.setTotalExpenses(customerTotalExpenses);
            totalExpensesAll = totalExpensesAll.add(customerTotalExpenses);

            customers.add(customer);
        }

        statResult.setTotalExpenses(totalExpensesAll);

        BigDecimal avgExpenses = totalExpensesAll.divide(BigDecimal.valueOf(customers.size()), RoundingMode.HALF_UP);
        statResult.setAvgExpenses(avgExpenses);

        return statResult;
    }


}
