package com.ironhack.restoranmanagementsystem.dto.request;

import jakarta.validation.constraints.NotBlank;

public class CategoryCreateRequest {

    @NotBlank(message = "Category name is required")
    private String name;

    public CategoryCreateRequest() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}