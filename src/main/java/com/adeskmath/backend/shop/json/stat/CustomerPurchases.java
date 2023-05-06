package com.adeskmath.backend.shop.json.stat;

import com.adeskmath.backend.shop.repo.CustomerRepository;
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
public class CustomerPurchases {
    private String name;
    private List<CustomerRepository.CustomerStatJSON> purchases;
    private BigDecimal totalExpenses;
}
