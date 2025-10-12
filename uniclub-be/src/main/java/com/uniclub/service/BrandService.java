package com.uniclub.service;

import com.uniclub.dto.request.Brand.CreateBrandRequest;
import com.uniclub.dto.request.Brand.UpdateBrandRequest;
import com.uniclub.dto.response.Brand.BrandResponse;

import java.util.List;

public interface BrandService {
    BrandResponse createBrand(CreateBrandRequest request);
    BrandResponse updateBrand(Integer brandId, UpdateBrandRequest request);
    void deleteBrand(Integer id);


    List<BrandResponse> getAllBrands();
    BrandResponse getBrandById(Integer id);


}
