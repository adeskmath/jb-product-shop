package com.adeskmath.backend.shop.search;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class SearchProductMin {
    private String productName;
    private Integer minTimes;
}
