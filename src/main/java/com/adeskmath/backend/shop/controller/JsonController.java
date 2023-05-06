package com.adeskmath.backend.shop.controller;

import com.adeskmath.backend.shop.json.search.SearchResult;
import com.adeskmath.backend.shop.json.stat.StatResult;
import com.adeskmath.backend.shop.search.CustomerSearchPeriod;
import com.adeskmath.backend.shop.service.JsonService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;

@RestController
@RequestMapping("/json")
public class JsonController {
    private final JsonService jsonService;

    public JsonController(JsonService jsonService) {
        this.jsonService = jsonService;
    }

    @PostMapping("/search")
    public ResponseEntity<SearchResult> search(@RequestBody Object object) {
        LinkedHashMap<String, List<Object>> map = (LinkedHashMap<String, List<Object>>) object;
        return ResponseEntity.ok(jsonService.searchWithJson(map));
    }

    @PostMapping("/stat")
    public ResponseEntity<StatResult> stat(@RequestBody CustomerSearchPeriod period) {
        return ResponseEntity.ok(jsonService.statWithJson(period.getStartDate(), period.getEndDate()));
    }
}
