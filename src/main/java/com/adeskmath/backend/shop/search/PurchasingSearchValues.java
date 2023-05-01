package com.adeskmath.backend.shop.search;

import com.adeskmath.backend.shop.entity.Customer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class PurchasingSearchValues {
    private Date startDate;
    private Date endDate;
    private Customer customer;
}
