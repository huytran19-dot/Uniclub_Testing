package com.uniclub.service.impl;

import com.uniclub.dto.request.Product.CreateProductRequest;
import com.uniclub.dto.request.Product.UpdateProductRequest;
import com.uniclub.dto.response.Product.ProductResponse;
import com.uniclub.entity.Brand;
import com.uniclub.entity.Category;
import com.uniclub.entity.Product;
import com.uniclub.repository.BrandRepository;
//import com.uniclub.repository.CategoryRepository;
import com.uniclub.repository.ProductRepository;
import com.uniclub.service.ProductService;
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
//    private final CategoryRepository categoryRepository;

//    @Override
//    public ProductResponse createProduct(CreateProductRequest request) {
//        // Lấy Brand & Category từ id
//        Brand brand = brandRepository.findById(request.getIdBrand())
//                .orElseThrow(() -> new RuntimeException("Không tìm thấy brand với id=" + request.getIdBrand()));
//        Category category = categoryRepository.findById(request.getIdCategory())
//                .orElseThrow(() -> new RuntimeException("Không tìm thấy category với id=" + request.getIdCategory()));
//
//        Product product = Product.builder()
//                .name(request.getName())
//                .description(request.getDescription())
//                .information(request.getInformation())
//                .price(request.getPrice())       // DB: INT, entity bạn đang để Integer hoặc double -> khớp DTO
//                .status(request.getStatus())     // nếu entity bạn chưa có field status thì bỏ dòng này
//                .brand(brand)
//                .category(category)
//                .build();
//
//        Product saved = productRepository.save(product);
//        return ProductResponse.fromEntity(saved);
//    }

//    @Override
//    public ProductResponse updateProduct(UpdateProductRequest request) {
//        Product product = productRepository.findById(request.getId())
//                .orElseThrow(() -> new RuntimeException("Không tìm thấy product với id=" + request.getId()));
//
//        product.setName(request.getName());
//        product.setDescription(request.getDescription());
//        product.setInformation(request.getInformation());
//        product.setPrice(request.getPrice());
//
//        if (request.getStatus() != null) {
//            product.setStatus(request.getStatus());
//        }
//        if (request.getIdBrand() != null) {
//            Brand brand = brandRepository.findById(request.getIdBrand())
//                    .orElseThrow(() -> new RuntimeException("Không tìm thấy brand với id=" + request.getIdBrand()));
//            product.setBrand(brand);
//        }
//        if (request.getIdCategory() != null) {
//            Category category = categoryRepository.findById(request.getIdCategory())
//                    .orElseThrow(() -> new RuntimeException("Không tìm thấy category với id=" + request.getIdCategory()));
//            product.setCategory(category);
//        }
//
//        Product updated = productRepository.save(product);
//        return ProductResponse.fromEntity(updated);
//    }


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
                .orElseThrow(() -> new RuntimeException("Không tìm thấy product với id=" + id));
        return ProductResponse.fromEntity(product);
    }

    @Override
    public void deleteProduct(Integer id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Không tìm thấy product với id=" + id);
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
