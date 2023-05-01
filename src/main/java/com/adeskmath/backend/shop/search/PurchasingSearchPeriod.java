package com.adeskmath.backend.shop.search;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class PurchasingSearchPeriod {
    private Date startDate;
    private Date endDate;

}
