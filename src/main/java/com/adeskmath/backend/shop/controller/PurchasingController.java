package com.adeskmath.backend.shop.controller;

import com.adeskmath.backend.shop.entity.Purchasing;
import com.adeskmath.backend.shop.search.CustomerSearchPeriod;
import com.adeskmath.backend.shop.search.PurchasingSearchValues;
import com.adeskmath.backend.shop.service.PurchasingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/purchasing")
public class PurchasingController {
    private final PurchasingService purchasingService;

    public PurchasingController(PurchasingService purchasingService) {
        this.purchasingService = purchasingService;
    }

    //TODO move to stat controller
    @PostMapping("/statByCustomer")
    public ResponseEntity<List<Purchasing>> statByCustomer(@RequestBody PurchasingSearchValues psv) {
        return ResponseEntity.ok(purchasingService.findByParamsAndCustomer(psv.getStartDate(), psv.getEndDate(), psv.getCustomer()));
    }

    @PostMapping("/search")
    public ResponseEntity<List<Purchasing>> search(@RequestBody CustomerSearchPeriod period) {
        return ResponseEntity.ok(purchasingService.findAllByParams(period.getStartDate(), period.getEndDate()));
    }

    @PostMapping("/add")
    public ResponseEntity<Purchasing> add(@RequestBody Purchasing purchasing) {
        if (purchasing.getId() != null && purchasing.getId() !=0) {
            return new ResponseEntity("redundant param: id must be null", HttpStatus.NOT_ACCEPTABLE);
        }

        if (purchasing.getPurchasingDate() == null) {
            return new ResponseEntity("missed param: date, must be not empty", HttpStatus.NOT_ACCEPTABLE);
        }

        if (purchasing.getCustomer() == null) {
            return new ResponseEntity("missed param: Customer, must be not empty", HttpStatus.NOT_ACCEPTABLE);
        }

        if (purchasing.getProduct() == null) {
            return new ResponseEntity("missed param: Product, must be not empty", HttpStatus.NOT_ACCEPTABLE);
        }

        return ResponseEntity.ok(purchasingService.add(purchasing));
    }
}
