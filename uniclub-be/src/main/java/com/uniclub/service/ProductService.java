package com.uniclub.service;

import com.uniclub.dto.request.Product.CreateProductRequest;
import com.uniclub.dto.request.Product.UpdateProductRequest;
import com.uniclub.dto.response.Product.ProductResponse;

import java.util.List;

public interface ProductService {

    ProductResponse createProduct(CreateProductRequest request);
    ProductResponse updateProduct(Integer id, UpdateProductRequest request);


    List<ProductResponse> getAllProducts();

    ProductResponse getProductById(Integer id);

    void deleteProduct(Integer id); // hard delete; nếu muốn soft delete thì đổi logic ở Impl

    // filter
    List<ProductResponse> getProductsByBrandId(Integer brandId);

    List<ProductResponse> getProductsByCategoryId(Integer categoryId);

    List<ProductResponse> searchByName(String keyword);
    
    List<ProductResponse> searchByDescription(String keyword);
}
