package com.uniclub.service.impl;

import com.uniclub.dto.request.Product.CreateProductRequest;
import com.uniclub.dto.request.Product.UpdateProductRequest;
import com.uniclub.dto.response.Product.ProductResponse;
import com.uniclub.entity.Brand;
import com.uniclub.entity.Category;
import com.uniclub.entity.Product;
import com.uniclub.repository.BrandRepository;
import com.uniclub.repository.CategoryRepository;
import com.uniclub.repository.ProductRepository;
import com.uniclub.service.ProductService;
import com.uniclub.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public ProductResponse createProduct(CreateProductRequest request) {
        // Check if product name already exists
        if (productRepository.existsByNameIgnoreCase(request.getName())) {
            throw new IllegalArgumentException("Tên sản phẩm đã tồn tại");
        }

        // Get Brand and Category from IDs
        Brand brand = brandRepository.findById(request.getBrandId())
                .orElseThrow(() -> new ResourceNotFoundException("Brand", "id", request.getBrandId()));
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", request.getCategoryId()));

        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setInformation(request.getInformation());
        product.setPrice(request.getPrice());
        product.setBrand(brand);
        product.setCategory(category);

        Product saved = productRepository.save(product);
        return ProductResponse.fromEntity(saved);
    }

    @Override
    public ProductResponse updateProduct(Integer id, UpdateProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));

        // Check if name is being changed and if it already exists
        if (request.getName() != null && !request.getName().equals(product.getName())) {
            if (productRepository.existsByNameIgnoreCase(request.getName())) {
                throw new IllegalArgumentException("Tên sản phẩm đã tồn tại");
            }
            product.setName(request.getName());
        }

        if (request.getDescription() != null) {
            product.setDescription(request.getDescription());
        }
        if (request.getInformation() != null) {
            product.setInformation(request.getInformation());
        }
        if (request.getPrice() != null) {
            product.setPrice(request.getPrice());
        }
        if (request.getStatus() != null) {
            product.setStatus(request.getStatus());
        }
        if (request.getIdBrand() != null) {
            Brand brand = brandRepository.findById(request.getIdBrand())
                    .orElseThrow(() -> new ResourceNotFoundException("Brand", "id", request.getIdBrand()));
            product.setBrand(brand);
        }
        if (request.getIdCategory() != null) {
            Category category = categoryRepository.findById(request.getIdCategory())
                    .orElseThrow(() -> new ResourceNotFoundException("Category", "id", request.getIdCategory()));
            product.setCategory(category);
        }

        Product updated = productRepository.save(product);
        return ProductResponse.fromEntity(updated);
    }


    @Override
    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(ProductResponse::fromEntity)
                .toList();
    }

    @Override
    public ProductResponse getProductById(Integer id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));
        return ProductResponse.fromEntity(product);
    }

    @Override
    public void deleteProduct(Integer id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product", "id", id);
        }
        productRepository.deleteById(id); // Hard delete
        // Nếu muốn soft delete: load entity -> setStatus((byte)0) -> save(entity)
    }

    @Override
    public List<ProductResponse> getProductsByBrandId(Integer brandId) {
        return productRepository.findByBrand_Id(brandId)
                .stream()
                .map(ProductResponse::fromEntity)
                .toList();
    }

    @Override
    public List<ProductResponse> getProductsByCategoryId(Integer categoryId) {
        return productRepository.findByCategory_Id(categoryId)
                .stream()
                .map(ProductResponse::fromEntity)
                .toList();
    }

    @Override
    public List<ProductResponse> searchByName(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return getAllProducts();
        }
        return productRepository.findByNameContainingIgnoreCase(keyword)
                .stream()
                .map(ProductResponse::fromEntity)
                .toList();
    }
}