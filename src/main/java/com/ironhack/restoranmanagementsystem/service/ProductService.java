package com.ironhack.restoranmanagementsystem.service;

import com.ironhack.restoranmanagementsystem.dto.request.ProductCreateRequest;
import com.ironhack.restoranmanagementsystem.dto.response.ProductResponse;
import com.ironhack.restoranmanagementsystem.entity.Category;
import com.ironhack.restoranmanagementsystem.entity.Product;
import com.ironhack.restoranmanagementsystem.exception.ResourceNotFoundException;
import com.ironhack.restoranmanagementsystem.mapper.ProductMapper;
import com.ironhack.restoranmanagementsystem.repository.CategoryRepository;
import com.ironhack.restoranmanagementsystem.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository,
                          CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    public ProductResponse create(ProductCreateRequest request) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found: " + request.getCategoryId()));

        Product product = new Product(
                request.getName(),
                request.getDescription(),
                request.getPrice(),
                request.getAvailable(),
                category
        );
        return ProductMapper.toResponse(productRepository.save(product));
    }

    public List<ProductResponse> getAll() {
        return ProductMapper.toResponseList(productRepository.findAll());
    }

    public List<ProductResponse> getAvailable() {
        return ProductMapper.toResponseList(productRepository.findByAvailableTrue());
    }

    public List<ProductResponse> getByCategory(Long categoryId) {
        return ProductMapper.toResponseList(productRepository.findByCategoryId(categoryId));
    }

    public ProductResponse getById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + id));
        return ProductMapper.toResponse(product);
    }

    public ProductResponse update(Long id, ProductCreateRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + id));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found: " + request.getCategoryId()));

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setAvailable(request.getAvailable());
        product.setCategory(category);

        return ProductMapper.toResponse(productRepository.save(product));
    }

    public void delete(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product not found: " + id);
        }
        productRepository.deleteById(id);
    }
}