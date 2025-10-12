package com.uniclub.service.impl;

import com.uniclub.dto.request.Color.CreateColorRequest;
import com.uniclub.dto.request.Color.UpdateColorRequest;
import com.uniclub.dto.response.Color.ColorResponse;
import com.uniclub.entity.Color;
import com.uniclub.entity.Role;
import com.uniclub.exception.ResourceNotFoundException;
import com.uniclub.repository.ColorRepository;
import com.uniclub.service.ColorService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class ColorServiceImpl implements ColorService {

    @Autowired
    private ColorRepository colorRepository;

    @Override
    public ColorResponse createColor(CreateColorRequest request) {
        // Kiểm tra trùng tên color
        if (colorRepository.existsByNameIgnoreCase(request.getName())) {
            throw new IllegalArgumentException("Color name already exists");
        }

        Color color = new Color();
        color.setName(request.getName());

        Color savedColor = colorRepository.save(color);
        return ColorResponse.fromEntity(savedColor);
    }

    @Override
    public ColorResponse updateColor(Integer colorId, UpdateColorRequest request) {
        Color color = colorRepository.findById(colorId)
                .orElseThrow(() -> new ResourceNotFoundException("Color", "id", colorId));

        if (request.getName() != null && !request.getName().equals(color.getName())) {
            // Kiểm tra trùng tên color
            if (colorRepository.existsByNameIgnoreCase(request.getName())) {
                throw new IllegalArgumentException("Color name already exists");
            }
            color.setName(request.getName());
        }

        if (request.getStatus() != null) {
            color.setStatus(request.getStatus());
        }

        Color updatedColor = colorRepository.save(color);
        return ColorResponse.fromEntity(updatedColor);
    }

    @Override
    public List<ColorResponse> getAllColors() {
        return colorRepository.findAll()
                .stream()
                .map(ColorResponse::fromEntity)
                .toList();
    }

    @Override
    public ColorResponse getColorById(Integer colorId) {
        Color color = colorRepository.findById(colorId)
                .orElseThrow(() -> new ResourceNotFoundException("Color", "id", colorId));
        return ColorResponse.fromEntity(color);
    }

    @Override
    public void deleteColor(Integer colorId) {
        if (!colorRepository.existsById(colorId)) {
            throw new ResourceNotFoundException("Color", "id", colorId);
        }
        colorRepository.deleteById(colorId); // Hard delete
    }
}
