package com.adeskmath.backend.shop.service;

import com.adeskmath.backend.shop.json.search.SearchResult;
import com.adeskmath.backend.shop.json.stat.StatResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.stereotype.Service;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

@Service
public class FileService {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final JsonService jsonService;

    public FileService(JsonService jsonService) {
        this.jsonService = jsonService;
    }

    public void search(String inputFile, String outputFile) {
        try {
            LinkedHashMap<String, List<Object>> map = objectMapper.readValue(new File(inputFile), LinkedHashMap.class);
            SearchResult searchResult = jsonService.searchWithJson(map);
            objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true)
                            .writeValue(new File(outputFile), searchResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stat(String inputFile, String outputFile) {
        try {
            LinkedHashMap<String, String> map = objectMapper.readValue(new File(inputFile), LinkedHashMap.class);
            Date startDate = new SimpleDateFormat("yyyy-MM-dd").parse(map.get("startDate").toString());
            Date endDate = new SimpleDateFormat("yyyy-MM-dd").parse(map.get("endDate").toString());

            StatResult statResult = jsonService.statWithJson(startDate, endDate);
            objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true)
                    .writeValue(new File(outputFile), statResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
