package com.uniclub.service;

import com.uniclub.dto.request.Color.CreateColorRequest;
import com.uniclub.dto.request.Color.UpdateColorRequest;
import com.uniclub.dto.response.Color.ColorResponse;

import java.util.List;

public interface ColorService {

    ColorResponse createColor(CreateColorRequest request);
    ColorResponse updateColor(UpdateColorRequest request);

    List<ColorResponse> getAllColors();

    ColorResponse getColorById(Integer id);

    void deleteColor(Integer id);
}
