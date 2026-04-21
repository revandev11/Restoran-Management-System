package com.ironhack.restoranmanagementsystem.mapper;

import com.ironhack.restoranmanagementsystem.dto.response.CategoryResponse;
import com.ironhack.restoranmanagementsystem.entity.Category;

import java.util.List;

public class CategoryMapper {

    public static CategoryResponse toResponse(Category category) {
        return new CategoryResponse(
                category.getId(),
                category.getName()
        );
    }

    public static List<CategoryResponse> toResponseList(List<Category> categories) {
        return categories.stream()
                .map(CategoryMapper::toResponse)
                .toList();
    }
}