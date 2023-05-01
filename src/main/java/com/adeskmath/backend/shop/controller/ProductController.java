package com.adeskmath.backend.shop.controller;

import com.adeskmath.backend.shop.entity.Product;
import com.adeskmath.backend.shop.search.PurchasingSearchValues;
import com.adeskmath.backend.shop.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    //TODO move to stat controller
    @PostMapping("/statByCustomer")
    public ResponseEntity<List<Product>> statByCustomer(@RequestBody PurchasingSearchValues psv) {
        return ResponseEntity.ok(productService.findPurchasingPeriodAndCustomer(psv.getStartDate(), psv.getEndDate(), psv.getCustomer()));
    }

    @PostMapping("/add")
    public ResponseEntity<Product> add(@RequestBody Product product) {
        if (product.getId() != null && product.getId() !=0) {
            return new ResponseEntity("redundant param: id must be null", HttpStatus.NOT_ACCEPTABLE);
        }

        if (product.getName() == null || product.getName().trim().length() == 0) {
            return new ResponseEntity("missed param: name, must be not empty", HttpStatus.NOT_ACCEPTABLE);
        }

        return ResponseEntity.ok(productService.add(product));
    }
}
