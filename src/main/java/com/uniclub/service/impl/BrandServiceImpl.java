package com.uniclub.service.impl;

import com.uniclub.dto.request.Brand.CreateBrandRequest;
import com.uniclub.dto.request.Brand.UpdateBrandRequest;
import com.uniclub.dto.response.Brand.BrandResponse;
import com.uniclub.entity.Brand;
import com.uniclub.entity.Role;
import com.uniclub.exception.ResourceNotFoundException;
import com.uniclub.repository.BrandRepository;
import com.uniclub.service.BrandService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class BrandServiceImpl implements BrandService {
    @Autowired
    private BrandRepository brandRepository;

    @Override
    public BrandResponse createBrand(CreateBrandRequest request) {
        Brand brand = new Brand();
        brand.setName(request.getName());

        Brand savedBrand = brandRepository.save(brand);
        return BrandResponse.fromEntity(savedBrand);
    }

    @Override
    public BrandResponse updateBrand(Integer brandId, UpdateBrandRequest request) {
        Brand brand = brandRepository.findById(brandId)
                .orElseThrow(() -> new ResourceNotFoundException("Brand", "id", brandId));

        if (request.getName() != null) {
            brand.setName(request.getName());
        }
        if (request.getStatus() != null) {
            brand.setStatus(request.getStatus());
        }

        Brand updatedBrand = brandRepository.save(brand);
        return BrandResponse.fromEntity(updatedBrand);
    }

    @Override
    public List<BrandResponse> getAllBrands() {
        return brandRepository.findAll()
                .stream()
                .map(BrandResponse::fromEntity)
                .toList();
    }

    @Override
    public BrandResponse getBrandById(Integer brandId) {
        Brand brand = brandRepository.findById(brandId)
                .orElseThrow(() -> new ResourceNotFoundException("Brand", "id", brandId));
        return BrandResponse.fromEntity(brand);
    }

    // Hard delete
    @Override
    public void deleteBrand(Integer brandId) {
        if (!brandRepository.existsById(brandId)) {
            throw new ResourceNotFoundException("Brand", "id", brandId);
        }
        brandRepository.deleteById(brandId);
    }
}
