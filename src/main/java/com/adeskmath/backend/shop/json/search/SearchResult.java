package com.adeskmath.backend.shop.json.search;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class SearchResult {
    private String type = "search";
    private List<CustomerResult> results = new ArrayList<>();
}
