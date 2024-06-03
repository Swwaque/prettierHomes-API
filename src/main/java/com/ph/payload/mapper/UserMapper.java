package com.ph.payload.mapper;

import com.ph.domain.entities.Contact;
import com.ph.domain.entities.User;
import com.ph.payload.response.ContactResponse;
import com.ph.payload.response.LoginResponse;
import com.ph.payload.response.UserResponse;
import com.ph.payload.response.UserSaveResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final ImageMapper imageMapper;

    public LoginResponse toLoginResponse(User user, String token) {
        return LoginResponse.builder()
                .token(token)
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRole().name())
                .phone(user.getPhone())
                .profilePhoto(user.getProfilePhoto() != null ? imageMapper.toprofilePhotoResponse(user.getProfilePhoto()) : null)
                .build();
    }

    public UserResponse toUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phone(user.getPhone())
                .role(user.getRole().name())
                .profilePhoto(user.getProfilePhoto() != null ? imageMapper.toprofilePhotoResponse(user.getProfilePhoto()) : null)
                .build();
    }

    public UserSaveResponse toUserSaveResponse(User user) {
        return UserSaveResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .build();
    }

    public ContactResponse toContactResponse(Contact contact) {
        return ContactResponse.builder()
                .id(contact.getId())
                .firstName(contact.getFirstName())
                .lastName(contact.getLastName())
                .email(contact.getEmail())
                .message(contact.getMessage())
                .createdAt(contact.getCreatedAt())
                .status(contact.isStatus())
                .build();
    }

}
