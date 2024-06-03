package com.ph.payload.mapper;

import com.ph.domain.entities.Image;
import com.ph.domain.entities.ProfilePhoto;
import com.ph.exception.customs.ImageException;
import com.ph.payload.response.ImageResponse;
import com.ph.payload.response.ProfilePhotoResponse;
import com.ph.utils.ImageUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;

@Component
public class ImageMapper {
    public ImageResponse toImageResponse(Image savedImage) {
        return ImageResponse.builder()
                .id(savedImage.getId())
                .name(savedImage.getName())
                .type(savedImage.getType())
                .featured(savedImage.isFeatured())
                .data(encodeImage(ImageUtil.decompressImage(savedImage.getData())))
                .advertId(savedImage.getAdvert().getId())
                .build();
    }


    public ProfilePhotoResponse toprofilePhotoResponse(ProfilePhoto savedImage) {
        return ProfilePhotoResponse.builder()
                .id(savedImage.getId())
                .name(savedImage.getName())
                .type(savedImage.getType())
                .data(encodeImage(ImageUtil.decompressImage(savedImage.getData())))
                .build();
    }


    public ProfilePhoto toProfilePhoto(MultipartFile photo) {
        ProfilePhoto profilePhoto;
        try {
            profilePhoto = ProfilePhoto.builder()
                    .data(ImageUtil.compressImage(photo.getBytes()))
                    .name(photo.getOriginalFilename())
                    .type(photo.getContentType())
                    .build();
        } catch (IOException e) {
            throw new ImageException(e.getMessage());
        }
        return profilePhoto;
    }

    private String encodeImage(byte[] imageData) {
        return Base64.getEncoder().encodeToString(imageData);
    }


}