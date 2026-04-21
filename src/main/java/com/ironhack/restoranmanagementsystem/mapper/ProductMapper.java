package com.ironhack.restoranmanagementsystem.mapper;

import com.ironhack.restoranmanagementsystem.dto.response.ProductResponse;
import com.ironhack.restoranmanagementsystem.entity.Product;

import java.util.List;

public class ProductMapper {

    public static ProductResponse toResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getAvailable(),
                product.getCategory().getId(),
                product.getCategory().getName()
        );
    }

    public static List<ProductResponse> toResponseList(List<Product> products) {
        return products.stream()
                .map(ProductMapper::toResponse)
                .toList();
    }
}