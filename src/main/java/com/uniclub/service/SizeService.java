package com.uniclub.service;

import com.uniclub.dto.request.Size.CreateSizeRequest;
import com.uniclub.dto.response.Size.SizeResponse;
import java.util.List;

public interface SizeService {
    SizeResponse createSize(CreateSizeRequest req);
    List<SizeResponse> getAllSizes();
}