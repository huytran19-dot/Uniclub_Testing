package com.uniclub.service.impl;

import com.uniclub.dto.request.Brand.CreateBrandRequest;
import com.uniclub.dto.request.Brand.UpdateBrandRequest;
import com.uniclub.dto.response.Brand.BrandResponse;
import com.uniclub.entity.Brand;
import com.uniclub.repository.BrandRepository;
import com.uniclub.service.BrandService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {

    private final BrandRepository brandRepository;

    @Override
    public BrandResponse createBrand(CreateBrandRequest request) {
        Brand brand = Brand.builder()
                .name(request.getName())
                .status(request.getStatus())
                .build();
        Brand saved = brandRepository.save(brand);
        return BrandResponse.fromEntity(saved);
    }

    @Override
    public BrandResponse updateBrand(UpdateBrandRequest request) {
        Brand brand = brandRepository.findById(request.getId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy brand với id: " + request.getId()));

        brand.setName(request.getName());
        if (request.getStatus() != null) {
            brand.setStatus(request.getStatus());
        }
        Brand updated = brandRepository.save(brand);
        return BrandResponse.fromEntity(updated);
    }

    @Override
    public List<BrandResponse> getAllBrands() {
        return brandRepository.findAll()
                .stream()
                .map(BrandResponse::fromEntity)
                .toList();
    }

    @Override
    public BrandResponse getBrandById(Integer id) {
        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy brand với id: " + id));
        return BrandResponse.fromEntity(brand);
    }

    // Hard delete
    @Override
    public void deleteBrand(Integer id) {

        if (!brandRepository.existsById(id)) {
            throw new RuntimeException("Không tìm thấy brand với id: " + id);
        }
        brandRepository.deleteById(id);
    }
}
