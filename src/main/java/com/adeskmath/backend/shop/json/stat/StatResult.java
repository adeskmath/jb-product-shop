package com.adeskmath.backend.shop.json.stat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class StatResult {
    private String type = "stat";
    private Long totalDays;
    private List<CustomerPurchases> customers;
    private BigDecimal totalExpenses = BigDecimal.ZERO;
    private BigDecimal avgExpenses = BigDecimal.ZERO;
}
