package com.ph.payload.mapper;

import com.ph.domain.entities.Advert;
import com.ph.domain.entities.TourRequest;
import com.ph.domain.entities.User;
import com.ph.payload.resource.AdvertResource;
import com.ph.payload.resource.TourRequestResource;
import com.ph.payload.resource.UserResource;
import com.ph.payload.response.StatisticsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ReportMapper {


    public AdvertResource mapToResource(Advert advert) {
        return AdvertResource.builder()
                .id(advert.getId())
                .title(advert.getTitle())
                .description(advert.getDescription())
                .price(advert.getPrice())
                .statusForAdvert(advert.getStatusForAdvert())
                .builtIn(advert.isBuiltIn())
                .isActive(advert.isActive())
                .viewCount(advert.getViewCount())
                .advertType(advert.getAdvertType().getTitle())
                .countryName(advert.getCountry().getName())
                .cityName(advert.getCity().getName())
                .districtName(advert.getDistrict().getName())
                .userName(advert.getUser().getFirstName() + " " + advert.getUser().getLastName())
                .createdAt(advert.getCreatedAt())
                .build();
    }


    public StatisticsResponse toStatisticsResponse(Long totalAdverts, Long totalUsers, Long totalAdvertTypes, Long totalTourRequests, Long totalCategories) {
        return StatisticsResponse.builder()
                .totalAdverts(totalAdverts)
                .totalUsers(totalUsers)
                .totalAdvertTypes(totalAdvertTypes)
                .totalTourRequests(totalTourRequests)
                .totalCategories(totalCategories)
                .build();
    }


    public UserResource toUserResource(User user) {
        return UserResource.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .builtIn(user.isBuiltIn())
                .role(user.getRole())
                .build();
    }


    public TourRequestResource toTourRequestResource(TourRequest tourRequest) {
        return TourRequestResource.builder()
                .id(tourRequest.getId())
                .tourDate(tourRequest.getTourDate())
                .tourTime(tourRequest.getTourTime())
                .status(tourRequest.getStatus())
                .advertId(tourRequest.getAdvert().getId())
                .advertTitle(tourRequest.getAdvert().getTitle())
                .ownerFullName(tourRequest.getAdvert().getUser().getFirstName() + " " + tourRequest.getAdvert().getUser().getLastName())
                .guestFullName(tourRequest.getGuestUser().getFirstName() + " " + tourRequest.getGuestUser().getLastName())
                .build();
    }

}
