package com.uniclub.service.impl;

import com.uniclub.config.FileStorageProperties;
import com.uniclub.exception.FileStorageException;
import com.uniclub.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class FileStorageServiceImpl implements FileStorageService {

    private final Path rootLocation;

    @Autowired
    public FileStorageServiceImpl(FileStorageProperties properties) {
        this.rootLocation = Paths.get(properties.getUploadDir());
        init();
    }

    @Override
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new FileStorageException("Could not initialize storage", e);
        }
    }

    @Override
    public String store(MultipartFile file) {
        return "";
    }

    @Override
    public Stream<Path> loadAll() {
        return Stream.empty();
    }

    @Override
    public Path load(String filename) {
        return null;
    }

    @Override
    public Resource loadAsResource(String filename) {
        return null;
    }

    @Override
    public void deleteAll() {

    }
}
