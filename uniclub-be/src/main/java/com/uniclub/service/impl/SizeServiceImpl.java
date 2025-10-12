package com.uniclub.service.impl;

import com.uniclub.dto.request.Size.CreateSizeRequest;
import com.uniclub.dto.request.Size.UpdateSizeRequest;
import com.uniclub.dto.response.Size.SizeResponse;
import com.uniclub.entity.Size;
import com.uniclub.exception.ResourceNotFoundException;
import com.uniclub.repository.SizeRepository;
import com.uniclub.service.SizeService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class SizeServiceImpl implements SizeService {
    
    @Autowired
    private SizeRepository sizeRepository;

    @Override
    public SizeResponse createSize(CreateSizeRequest request) {
        // Kiểm tra trùng tên Size
        if (sizeRepository.existsByNameIgnoreCase(request.getName())) {
            throw new IllegalArgumentException("Tên kích thước đã tồn tại");
        }

        Size size = new Size();
        size.setName(request.getName());

        Size savedSize = sizeRepository.save(size);
        return SizeResponse.fromEntity(savedSize);
    }

    @Override
    public SizeResponse updateSize(Integer sizeId, UpdateSizeRequest request) {
        Size size = sizeRepository.findById(sizeId)
                .orElseThrow(() -> new ResourceNotFoundException("Size", "id", sizeId));

        // Check if name is being changed and if it already exists
        if (request.getName() != null && !request.getName().equals(size.getName())) {
            if (sizeRepository.existsByNameIgnoreCase(request.getName())) {
                throw new IllegalArgumentException("Tên kích thước đã tồn tại");
            }
            size.setName(request.getName());
        }

        if (request.getStatus() != null) {
            size.setStatus(request.getStatus());
        }

        Size updatedSize = sizeRepository.save(size);
        return SizeResponse.fromEntity(updatedSize);
    }

    @Override
    public List<SizeResponse> getAllSizes() {
        return sizeRepository.findAll()
                .stream()
                .map(SizeResponse::fromEntity)
                .toList();
    }

    @Override
    public SizeResponse getSizeById(Integer sizeId) {
        Size size = sizeRepository.findById(sizeId)
                .orElseThrow(() -> new ResourceNotFoundException("Size", "id", sizeId));
        return SizeResponse.fromEntity(size);
    }

    @Override
    public void deleteSize(Integer sizeId) {
        if (!sizeRepository.existsById(sizeId)) {
            throw new ResourceNotFoundException("Size", "id", sizeId);
        }
        sizeRepository.deleteById(sizeId);
    }
}
