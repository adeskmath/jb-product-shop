package com.adeskmath.backend.shop.utilities;

import com.adeskmath.backend.shop.dto.CustomerMapper;
import com.adeskmath.backend.shop.dto.DtoCustomer;
import com.adeskmath.backend.shop.dto.ProductMapper;
import com.adeskmath.backend.shop.entity.Customer;
import com.adeskmath.backend.shop.search.PurchasingSearchPeriod;
import com.adeskmath.backend.shop.service.CustomerService;
import com.adeskmath.backend.shop.service.ProductService;
import com.adeskmath.backend.shop.service.PurchasingService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Component
public class JsonOperator implements ApplicationRunner {

    private final CustomerService customerService;
    private final ProductService productService;
    private final PurchasingService purchasingService;

    public JsonOperator(CustomerService customerService, ProductService productService, PurchasingService purchasingService) {
        this.customerService = customerService;
        this.productService = productService;
        this.purchasingService = purchasingService;
    }

//    public void readWriteJson(String inputFile, String outputFile) throws IOException {
//
//        writeJsonFile(search(readJsonFile(inputFile)), outputFile);
//    }
    public Map<String, Object> search(JsonNode searchCriteria) throws IOException {
        return getSearchResults(searchCriteria, customerService);
    }

    public Map<String, Object> stat(String inputFile) throws IOException {
        return getStatResults(readStatCriteriaFile(inputFile), customerService, productService, purchasingService);
    }

    public static Map<String, Object> getSearchResults(JsonNode searchCriteria, CustomerService customerService) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(searchCriteria.traverse());

        rootNode.path("criterias").forEach(System.out::println);

        List<Map<String, Object>> mapList = new ArrayList<>();

        rootNode.path("criterias").forEach(jsonNode -> {
            Map<String, Object> map = new HashMap<>();
            String criteria = jsonNode.fieldNames().next();
            List<Customer> list = switch (criteria) {
                case "lastName" -> customerService.findByLastName(jsonNode.path("lastName").asText());
                case "minExpenses" ->
                        customerService.findByExpenseRange(jsonNode.path("minExpenses").decimalValue(), jsonNode.path("maxExpenses").decimalValue());
                case "productName" ->
                        customerService.findByProductMinTimes(jsonNode.path("productName").asText(), jsonNode.path("minTimes").asInt());
                case "lowestRank" -> customerService.findByLowestRank(jsonNode.path("lowestRank").asInt());
                default -> null;
            };

            List<DtoCustomer> customers = list.stream().map(CustomerMapper::map).toList();
            map.put("criteria", jsonNode);
            map.put("results", customers);
            mapList.add(map);
        });

        Map<String, Object> map = new HashMap<>();
        map.put("type", "search");
        map.put("results", mapList);

        return map;
    }

    public JsonNode readSearchCriteriaFile(String inputFile) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode inputJson = objectMapper.readValue(new File(inputFile), JsonNode.class);
        System.out.println(inputJson);
        return inputJson;
    }

    public PurchasingSearchPeriod readStatCriteriaFile(String inputFile) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        PurchasingSearchPeriod p = objectMapper.readValue(new File(inputFile), PurchasingSearchPeriod.class);
        System.out.println(p);
        return p;
    }

    public void writeJsonFile(Map<String, Object> result, String outputFile) {
        ObjectMapper objectMapper = new ObjectMapper();

        //configure objectMapper for pretty input
        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        try {
            objectMapper.writeValue(new File(outputFile), result);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Map<String, Object> getStatResults(PurchasingSearchPeriod p, CustomerService customerService, ProductService productService, PurchasingService purchasingService) {

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
        BigDecimal grandTotal = purchasingService.getGrandTotalExpenses(startDate, endDate);
        BigDecimal avgExpenses = purchasingService.getAvgExpenses(startDate, endDate);
        map.put("totalExpenses", grandTotal == null ? 0 : grandTotal.setScale(2, RoundingMode.HALF_UP));
        map.put("avgExpenses", avgExpenses == null ? 0 : avgExpenses.setScale(2, RoundingMode.HALF_UP));

        return map;
    }

    public static long calcBusinessDays(Date startDate, Date endDate) {
        LocalDate d1 = convertToLocalDate(startDate);
        LocalDate d2 = convertToLocalDate(endDate);
        int weekends = 0;
        long totalDays = d1.until(d2, ChronoUnit.DAYS) + 1;
        long tail = totalDays % 7;
        LocalDate tailStart = d2.minusDays(tail - 1);
        while (!tailStart.equals(d2.plusDays(1)) && weekends < 2) {
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

    @Override
    public void run(ApplicationArguments args) throws Exception {
        String[] params;
        if (args.getSourceArgs().length != 0) {
            params = args.getSourceArgs();
        } else return;
        if (params.length != 3 || params[1].equals(params[2])) {
            System.out.println("error: wrong or missed parameters");
            return;
        }
        String inputFile = params[1];
        String outputFile = params[2];
        if (params[0].equals("search")) {
            writeJsonFile(search(readSearchCriteriaFile(inputFile)), outputFile);
            return;
        }
        if (params[0].equals("stat")) {
            writeJsonFile(stat(inputFile), outputFile);
            return;
        }
        System.out.println("error: 1st parameter must be 'search' or 'stat'");
    }

}
