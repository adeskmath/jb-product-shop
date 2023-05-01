package com.adeskmath.backend.shop.search;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class CustomerSearchExpenseRange {
    private BigDecimal minExpenses;
    private BigDecimal maxExpenses;
}
