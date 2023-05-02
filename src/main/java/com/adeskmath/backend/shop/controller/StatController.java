package com.adeskmath.backend.shop.controller;

import com.adeskmath.backend.shop.search.PurchasingSearchPeriod;
import com.adeskmath.backend.shop.service.CustomerService;
import com.adeskmath.backend.shop.service.ProductService;
import com.adeskmath.backend.shop.service.PurchasingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static com.adeskmath.backend.shop.utilities.JsonOperator.getStatResults;

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
        return ResponseEntity.ok(getStatResults(p, customerService, productService, purchasingService));
    }

    /*
    @PostMapping("")
    public ResponseEntity<Map<String, Object>> stat(@RequestBody PurchasingSearchPeriod p) {

        Date startDate = p.getStartDate();
        Date endDate = p.getEndDate();

        List<Customer> customers = customerService.findByPurchasingPeriod(startDate, endDate);
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("type", "stat");
        map.put("totalDays", calcBusinessDays(startDate, endDate));
        List<Map<String, Object>> customersDataList = new ArrayList<>();
        map.put("customers", customersDataList);
        for (Customer customer : customers) {
            Map<String, Object> customerData = new LinkedHashMap<>();
            customerData.put("name", customer.getName() + " " + customer.getLastName());
            customerData.put("purchases",
                    productService.findPurchasingPeriodAndCustomer(startDate, endDate, customer)
                            .stream().map(ProductMapper::map).toList());
            customerData.put("totalExpenses", purchasingService.getTotalExpenses(startDate, endDate, customer));
            customersDataList.add(customerData);
        }
        map.put("totalExpenses", purchasingService.getGrandTotalExpenses(startDate, endDate));
        map.put("avgExpenses", purchasingService.getAvgExpenses(startDate, endDate).setScale(2, RoundingMode.HALF_UP));
        return ResponseEntity.ok(map);
    }

    public static long calcBusinessDays(Date startDate, Date endDate) {
        LocalDate d1 = convertToLocalDate(startDate);
        LocalDate d2 = convertToLocalDate(endDate);
        int weekends = 0;
        long totalDays = d1.until(d2, ChronoUnit.DAYS) + 1;
        long tail = totalDays % 7;
        LocalDate tailStart = d2.minusDays(tail - 1);
        while (!tailStart.getDayOfWeek().equals(d2.getDayOfWeek()) && weekends < 2) {
            if (tailStart.getDayOfWeek() == DayOfWeek.SATURDAY || tailStart.getDayOfWeek() == DayOfWeek.SUNDAY) {
                weekends++;
            }
            tailStart = tailStart.plusDays(1);
        }
        return (totalDays / 7) * 5 + tail - weekends;
    }

    public static LocalDate convertToLocalDate(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }
    */
}
