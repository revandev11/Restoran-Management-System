package com.ironhack.restoranmanagementsystem.service;

import com.ironhack.restoranmanagementsystem.dto.request.CategoryCreateRequest;
import com.ironhack.restoranmanagementsystem.dto.response.CategoryResponse;
import com.ironhack.restoranmanagementsystem.entity.Category;
import com.ironhack.restoranmanagementsystem.exception.ConflictException;
import com.ironhack.restoranmanagementsystem.exception.ResourceNotFoundException;
import com.ironhack.restoranmanagementsystem.mapper.CategoryMapper;
import com.ironhack.restoranmanagementsystem.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public CategoryResponse create(CategoryCreateRequest request) {
        if (categoryRepository.existsByName(request.getName())) {
            throw new ConflictException("Category already exists: " + request.getName());
        }
        Category category = new Category(request.getName());
        return CategoryMapper.toResponse(categoryRepository.save(category));
    }

    public List<CategoryResponse> getAll() {
        return CategoryMapper.toResponseList(categoryRepository.findAll());
    }

    public CategoryResponse getById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found: " + id));
        return CategoryMapper.toResponse(category);
    }

    public CategoryResponse update(Long id, CategoryCreateRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found: " + id));
        category.setName(request.getName());
        return CategoryMapper.toResponse(categoryRepository.save(category));
    }

    public void delete(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Category not found: " + id);
        }
        categoryRepository.deleteById(id);
    }
}