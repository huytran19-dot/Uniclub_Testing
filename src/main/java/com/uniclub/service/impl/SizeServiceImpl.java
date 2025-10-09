package com.uniclub.service.impl;

import com.uniclub.dto.request.Size.CreateSizeRequest;
import com.uniclub.dto.response.Color.ColorResponse;
import com.uniclub.dto.response.Size.SizeResponse;
import com.uniclub.entity.Size;
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
            throw new IllegalArgumentException("Size name already exists");
        }

        Size size = new Size();
        size.setName(request.getName());

        Size savedSize = sizeRepository.save(size);
        return SizeResponse.fromEntity(savedSize);
    }

    @Override
    public List<SizeResponse> getAllSizes() {
        return sizeRepository.findAll()
                .stream()
                .map(SizeResponse::fromEntity)
                .toList();
    }
}
