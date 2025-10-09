package com.uniclub.service.impl;

import com.uniclub.dto.request.Variant.CreateVariantRequest;
import com.uniclub.dto.request.Variant.UpdateVariantRequest;
import com.uniclub.dto.response.Variant.VariantResponse;
import com.uniclub.entity.*;
import com.uniclub.repository.*;
import com.uniclub.service.VariantService;
import jakarta.transaction.Transactional;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Builder
@Service
@Transactional
@RequiredArgsConstructor
public class VariantServiceImpl implements VariantService {

    private final VariantRepository variantRepository;
    private final ProductRepository productRepository;
    private final SizeRepository sizeRepository;
    private final ColorRepository colorRepository;

    // CREATE
    @Override
    public VariantResponse createVariant(CreateVariantRequest request) {
        Product product = productRepository.findById(request.getIdProduct())
                .orElseThrow(() ->
                        new RuntimeException("Không tìm thấy product: " + request.getIdProduct()));

        Size size = null;
        if (request.getIdSize() != null) {
            size = sizeRepository.findById(request.getIdSize())
                    .orElseThrow(() ->
                            new RuntimeException("Không tìm thấy size: " + request.getIdSize()));
        }

        Color color = null;
        if (request.getIdColor() != null) {
            color = colorRepository.findById(request.getIdColor())
                    .orElseThrow(() ->
                            new RuntimeException("Không tìm thấy color: " + request.getIdColor()));
        }


        Variant v = Variant.builder()
                .product(product)
                .size(size)
                .color(color)
                .images(request.getImages())
                .quantity(request.getQuantity() != null ? request.getQuantity() : 0)
                .price(request.getPrice())
                .status(request.getStatus() != null ? request.getStatus() : 1)
                .build();

        Variant saved = variantRepository.save(v);
        return VariantResponse.fromEntity(saved);
    }

    // GET by SKU
    @Override
    public VariantResponse getBySku(Integer sku) {
        Variant v = variantRepository.findById(sku)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy variant SKU: " + sku));
        return VariantResponse.fromEntity(v);
    }

    // GET all (optional filter by status)
    @Override
    public List<VariantResponse> getAllVariant(Byte status) {
        List<Variant> list = (status == null)
                ? variantRepository.findAll()
                : variantRepository.findByStatus(status);
        return list.stream().map(VariantResponse::fromEntity).toList();
    }


    // UPDATE
    @Override
    public VariantResponse updateVariant(Integer sku, UpdateVariantRequest request) {
        Variant v = variantRepository.findById(sku)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy variant SKU: " + sku));

        if (request.getIdProduct() != null) {
            Product product = productRepository.findById(request.getIdProduct())
                    .orElseThrow(() ->
                            new RuntimeException("Không tìm thấy product: " + request.getIdProduct()));
            v.setProduct(product);
        }

        if (request.getIdSize() != null) {
            Size size = sizeRepository.findById(request.getIdSize())
                    .orElseThrow(() ->
                            new RuntimeException("Không tìm thấy size: " + request.getIdSize()));
            v.setSize(size);
        }

        if (request.getIdColor() != null) {
            Color color = colorRepository.findById(request.getIdColor())
                    .orElseThrow(() ->
                            new RuntimeException("Không tìm thấy color: " + request.getIdColor()));
            v.setColor(color);
        }

        if (request.getImages() != null) v.setImages(request.getImages());
        if (request.getQuantity() != null) v.setQuantity(request.getQuantity());
        if (request.getPrice() != null) v.setPrice(request.getPrice());
        if (request.getStatus() != null) v.setStatus(request.getStatus());

        Variant updated = variantRepository.save(v);
        return VariantResponse.fromEntity(updated);
    }

    // DELETE
    @Override
    public void deleteVariantBySku(Integer sku) {
        if (!variantRepository.existsById(sku)) {
            throw new RuntimeException("Không tìm thấy variant SKU: " + sku);
        }
        variantRepository.deleteById(sku); // Hard delete; nếu muốn soft -> set status=0 rồi save
    }

    // STOCK +
    @Override
    public VariantResponse increaseStock(Integer sku, Integer amount) {
        if (amount == null || amount <= 0) {
            throw new RuntimeException("Số lượng tăng phải > 0");
        }
        Variant v = variantRepository.findById(sku)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy variant SKU: " + sku));
        v.setQuantity((v.getQuantity() == null ? 0 : v.getQuantity()) + amount);
        return VariantResponse.fromEntity(variantRepository.save(v));
    }

    // STOCK -
    @Override
    public VariantResponse decreaseStock(Integer sku, Integer amount) {
        if (amount == null || amount <= 0) {
            throw new RuntimeException("Số lượng giảm phải > 0");
        }
        Variant v = variantRepository.findById(sku)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy variant SKU: " + sku));

        int current = (v.getQuantity() == null ? 0 : v.getQuantity());
        int next = current - amount;
        if (next < 0) {
            throw new RuntimeException("Số lượng không đủ. Hiện có: " + current);
        }
        v.setQuantity(next);
        return VariantResponse.fromEntity(variantRepository.save(v));
    }

    @Override
    public boolean existsByCombination(Integer productId, Integer sizeId, Integer colorId) {
        return false;
    }

}
