package com.adeskmath.backend.shop.dto;

import com.adeskmath.backend.shop.entity.Product;

public class ProductMapper {
    public static DtoProduct map(Product product) {
        DtoProduct dtoProduct = new DtoProduct();
        dtoProduct.setName(product.getName());
        dtoProduct.setPrice(product.getPrice());
        return dtoProduct;
    }
}
