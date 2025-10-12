package com.uniclub.service.impl;

import com.uniclub.dto.request.Category.CreateCategoryRequest;
import com.uniclub.dto.request.Category.UpdateCategoryRequest;
import com.uniclub.dto.response.Category.CategoryResponse;
import com.uniclub.entity.Category;
import com.uniclub.exception.ResourceNotFoundException;
import com.uniclub.repository.CategoryRepository;
import com.uniclub.service.CategoryService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public CategoryResponse createCategory(CreateCategoryRequest request) {
        // Kiểm tra trùng tên
        if (categoryRepository.existsByNameIgnoreCase(request.getName())) {
            throw new IllegalArgumentException("Tên danh mục đã tồn tại");
        }

        Category category = new Category();
        category.setName(request.getName());

        Category savedCategory = categoryRepository.save(category);
        return CategoryResponse.fromEntity(savedCategory);
    }

    @Override
    public CategoryResponse updateCategory(Integer categoryId, UpdateCategoryRequest request) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId));

        if (request.getName() != null && !request.getName().equals(category.getName())) {
            // Kiểm tra trùng tên role
            if (categoryRepository.existsByNameIgnoreCase(request.getName())) {
                throw new IllegalArgumentException("Tên danh mục đã tồn tại");
            }
            category.setName(request.getName());
        }

        if (request.getStatus() != null) {
            category.setStatus(request.getStatus());
        }

        Category updatedCategory = categoryRepository.save(category);
        return CategoryResponse.fromEntity(updatedCategory);
    }

    @Override
    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(CategoryResponse::fromEntity)
                .toList();
    }

    @Override
    public CategoryResponse getCategoryById(Integer categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId));
        return CategoryResponse.fromEntity(category);
    }

    @Override
    public void deleteCategory(Integer categoryId) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new ResourceNotFoundException("Category", "id", categoryId);
        }
        categoryRepository.deleteById(categoryId);
    }
}
