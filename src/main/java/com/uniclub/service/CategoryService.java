package com.uniclub.service;

import com.uniclub.dto.request.Category.CreateCategoryRequest;
import com.uniclub.dto.request.Category.UpdateCategoryRequest;
import com.uniclub.dto.response.Category.CategoryResponse;

import java.util.List;

public interface CategoryService {

    CategoryResponse createCategory(CreateCategoryRequest request);

    CategoryResponse updateCategory(UpdateCategoryRequest request);

    List<CategoryResponse> getAllCategories();

    void deleteCategory(Integer id);

}
