package com.uniclub.service.impl;

import com.uniclub.dto.request.Color.CreateColorRequest;
import com.uniclub.dto.request.Color.UpdateColorRequest;
import com.uniclub.dto.response.Color.ColorResponse;
import com.uniclub.entity.Color;
import com.uniclub.repository.ColorRepository;
import com.uniclub.service.ColorService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ColorServiceImpl implements ColorService {

    private final ColorRepository colorRepository;

    @Override
    public ColorResponse createColor(CreateColorRequest request) {
        if (colorRepository.existsByNameIgnoreCase(request.getName())) {
            throw new RuntimeException("Tên màu đã tồn tại");
        }

        Color color = Color.builder()
                .name(request.getName())
                .hexCode(request.getHexCode())
                .status(request.getStatus())
                .build();

        Color saved = colorRepository.save(color);
        return ColorResponse.fromEntity(saved);
    }

    @Override
    public ColorResponse updateColor(UpdateColorRequest request) {
        Color color = colorRepository.findById(request.getId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy màu với id: " + request.getId()));

        if (request.getName() != null
                && !request.getName().equalsIgnoreCase(color.getName())
                && colorRepository.existsByNameIgnoreCase(request.getName())) {
            throw new RuntimeException("Tên màu đã tồn tại");
        }

        color.setName(request.getName());
        color.setHexCode(request.getHexCode());
        if (request.getStatus() != null) {
            color.setStatus(request.getStatus());
        }

        Color updated = colorRepository.save(color);
        return ColorResponse.fromEntity(updated);
    }

    @Override
    public List<ColorResponse> getAllColors() {
        return colorRepository.findAll()
                .stream()
                .map(ColorResponse::fromEntity)
                .toList();
    }

    @Override
    public ColorResponse getColorById(Integer id) {
        Color color = colorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy màu với id: " + id));
        return ColorResponse.fromEntity(color);
    }

    @Override
    public void deleteColor(Integer id) {
        if (!colorRepository.existsById(id)) {
            throw new RuntimeException("Không tìm thấy màu với id: " + id);
        }
        colorRepository.deleteById(id); // Hard delete
    }
}
