package com.ph.service;

import com.ph.domain.entities.ProfilePhoto;
import com.ph.payload.mapper.ImageMapper;
import com.ph.repository.ProfilePhotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ProfilePhotoService {

    private final ProfilePhotoRepository profilePhotoRepository;
    private final ImageMapper imageMapper;


    public ProfilePhoto saveProfilePhoto(MultipartFile photo) {
        return profilePhotoRepository.save(imageMapper.toProfilePhoto(photo));
    }

    public ProfilePhoto updateProfilePhoto(ProfilePhoto profilePicture, MultipartFile photo) {
        ProfilePhoto newProfilePhoto = imageMapper.toProfilePhoto(photo);
        profilePicture.setName(newProfilePhoto.getName());
        profilePicture.setType(newProfilePhoto.getType());
        profilePicture.setData(newProfilePhoto.getData());
        return profilePhotoRepository.save(profilePicture);


    }

    // TODO delete photo

    public void deleteProfilePhoto(ProfilePhoto profilePhoto) {
        profilePhotoRepository.deleteById(profilePhoto.getId());
    }
}
