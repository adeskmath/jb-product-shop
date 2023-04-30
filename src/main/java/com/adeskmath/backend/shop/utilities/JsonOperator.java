package com.adeskmath.backend.shop.utilities;

import com.adeskmath.backend.shop.dto.CustomerMapper;
import com.adeskmath.backend.shop.dto.DtoCustomer;
import com.adeskmath.backend.shop.entity.Customer;
import com.adeskmath.backend.shop.service.CustomerService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class JsonOperator implements ApplicationRunner {

    private final CustomerService customerService;

    public JsonOperator(CustomerService customerService) {
        this.customerService = customerService;
    }

    public void readWriteJson(String inputFile, String outputFile) throws IOException {

        writeJsonFile(search(readJsonFile(inputFile)), outputFile);
    }
    public Map<String, Object> search(JsonNode searchCriteria) throws IOException {
        return getResults(searchCriteria, customerService);
    }

    public static Map<String, Object> getResults(JsonNode searchCriteria, CustomerService customerService) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(searchCriteria.traverse());

        System.out.println("Print out search criterias:");
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
            // TODO если list пустой?
            map.put("results", customers);
            mapList.add(map);
        });

        Map<String, Object> map = new HashMap<>();
        map.put("type", "search");
        map.put("results", mapList);

        return map;
    }

    public JsonNode readJsonFile(String inputFile) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode inputJson = objectMapper.readValue(new File(inputFile), JsonNode.class);
        System.out.println(inputJson);
        return inputJson;
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

    @Override
    public void run(ApplicationArguments args) throws Exception {

        if (args.getSourceArgs().length != 0) {
            String[] params = args.getSourceArgs();

            if (params[0].equals("search") && params.length == 3 && !params[1].equals(params[2])) {
                String inputFile = params[1];
                String outputFile = params[2];

                System.out.println("Executing search");
                System.out.println("reading criterias from json file: " + inputFile);
                System.out.println("saving results into json file: " + outputFile);

                readWriteJson(inputFile, outputFile);
            } else {
                System.out.println("no or wrong search parameters");
            }
        }
    }

}
