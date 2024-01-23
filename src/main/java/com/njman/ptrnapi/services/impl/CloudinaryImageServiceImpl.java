package com.njman.ptrnapi.services.impl;

import com.cloudinary.Cloudinary;
import com.njman.ptrnapi.services.CloudinaryImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CloudinaryImageServiceImpl implements CloudinaryImageService {
    private final Cloudinary cloudinary;
    @Override
    public String uploadImage(MultipartFile file) {
        try {
          Map data = cloudinary.uploader().upload(file.getBytes(), Map.of());
          return (String) data.get("url");
        }
        catch (IOException e) {
            throw new RuntimeException("Unable to upload file");
        }
    }
}
