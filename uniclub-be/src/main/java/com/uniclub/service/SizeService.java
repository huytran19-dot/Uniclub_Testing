package com.uniclub.service;

import com.uniclub.dto.request.Size.CreateSizeRequest;
import com.uniclub.dto.request.Size.UpdateSizeRequest;
import com.uniclub.dto.response.Size.SizeResponse;
import java.util.List;

public interface SizeService {
    SizeResponse createSize(CreateSizeRequest request);
    SizeResponse updateSize(Integer sizeId, UpdateSizeRequest request);
    void deleteSize(Integer id);

    List<SizeResponse> getAllSizes();
    SizeResponse getSizeById(Integer id);
}