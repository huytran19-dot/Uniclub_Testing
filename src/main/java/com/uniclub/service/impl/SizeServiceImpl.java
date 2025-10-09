package com.uniclub.service.impl;

import com.uniclub.dto.request.Size.CreateSizeRequest;
import com.uniclub.dto.response.Size.SizeResponse;
import com.uniclub.entity.Size;
import com.uniclub.repository.SizeRepository;
import com.uniclub.service.SizeService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SizeServiceImpl implements SizeService {

    private final SizeRepository sizeRepository;

    public SizeServiceImpl(SizeRepository sizeRepository) {
        this.sizeRepository = sizeRepository;
    }

    @Override
    public SizeResponse createSize(CreateSizeRequest req) {
        Size size = new Size();
        size.setName(req.getName());
        size.setStatus(1); // mặc định active
        sizeRepository.save(size);
        return SizeResponse.fromEntity(size);
    }

    @Override
    public List<SizeResponse> getAllSizes() {
        return SizeResponse.fromEntityList(sizeRepository.findAll());
    }


}
