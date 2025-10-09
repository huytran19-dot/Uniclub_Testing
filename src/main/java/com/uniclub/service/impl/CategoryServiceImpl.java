package com.uniclub.service.impl;

import com.uniclub.dto.request.Category.CreateCategoryRequest;
import com.uniclub.dto.request.Category.UpdateCategoryRequest;
import com.uniclub.dto.response.Category.CategoryResponse;
import com.uniclub.entity.Category;
import com.uniclub.repository.CategoryRepository;
import com.uniclub.service.CategoryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public CategoryResponse createCategory(CreateCategoryRequest request) {
        // Kiểm tra trùng tên
        if (categoryRepository.existsByNameIgnoreCase(request.getName())) {
            throw new RuntimeException("Tên danh mục đã tồn tại");
        }

        Category category = Category.builder()
                .name(request.getName())
                .status(request.getStatus()) // mặc định là 1 nếu không truyền
                .build();

        Category saved = categoryRepository.save(category);
        return CategoryResponse.fromEntity(saved);
    }

    @Override
    public CategoryResponse updateCategory(UpdateCategoryRequest request) {
        Category category = categoryRepository.findById(request.getId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy category với id: " + request.getId()));

        // Nếu đổi tên thì kiểm tra trùng
        if (request.getName() != null
                && !request.getName().equalsIgnoreCase(category.getName())
                && categoryRepository.existsByNameIgnoreCase(request.getName())) {
            throw new RuntimeException("Tên danh mục đã tồn tại");
        }

        category.setName(request.getName());
        if (request.getStatus() != null) {
            category.setStatus(request.getStatus());
        }

        Category updated = categoryRepository.save(category);
        return CategoryResponse.fromEntity(updated);
    }

    @Override
    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(CategoryResponse::fromEntity)
                .toList();
    }

    @Override
    public void deleteCategory(Integer id) {
        if (!categoryRepository.existsById(id)) {
            throw new RuntimeException("Không tìm thấy category với id: " + id);
        }
        // Hard delete (xóa hẳn trong DB)
        categoryRepository.deleteById(id);

    }
}
