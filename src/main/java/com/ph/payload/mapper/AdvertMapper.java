package com.ph.payload.mapper;

import com.ph.domain.entities.Advert;
import com.ph.domain.entities.AdvertType;
import com.ph.domain.entities.Image;
import com.ph.domain.enums.StatusForAdvert;
import com.ph.payload.request.AdvertRequest;
import com.ph.payload.request.AdvertRequestForUpdateByAdmin;
import com.ph.payload.request.AdvertRequestForUpdateByCustomer;
import com.ph.payload.request.AdvertTypeRequest;
import com.ph.payload.response.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AdvertMapper {

    private final CategoryMapper categoryMapper;
    private final UserMapper userMapper;
    private final LocationMapper locationMapper;
    private final ImageMapper imageMapper;


    public Advert toEntity(AdvertRequest request) {
        return Advert.builder()
                .title(request.getTitle())
                .description(request.getDesc())
                .price(request.getPrice())
                .location(request.getLocation())
                .address(request.getAddress())
                .statusForAdvert(StatusForAdvert.PENDING)
                .viewCount(0)
                .builtIn(false)
                .isActive(true)
                .build();
    }

    public DetailedAdvertResponse toDetailedAdvertResponse(Advert advert) {
        return DetailedAdvertResponse.builder()
                .id(advert.getId())
                .createdAt(advert.getCreatedAt())
                .updatedAt(advert.getUpdatedAt())
                .title(advert.getTitle())
                .slug(advert.getSlug())
                .description(advert.getDescription())
                .advertType(toResponse(advert.getAdvertType()))
                .category(categoryMapper.mapToCategoryResponsewithPropertyKeys(advert.getCategory()))
                .categoryPropertyValue(categoryMapper.entityToResponse(advert.getCategoryPropertyValues()))
                .price(advert.getPrice())
                .statusForAdvert(advert.getStatusForAdvert())
                .viewCount(advert.getViewCount())
                .builtIn(advert.isBuiltIn())
                .isActive(advert.isActive())
                .images(sortedByFeatured(advert.getImages()))
                .country(locationMapper.toCountryResponse(advert.getCountry()))
                .city(locationMapper.toCityResponse(advert.getCity()))
                .district(locationMapper.toDistrictResponse(advert.getDistrict()))
                .address(advert.getAddress())
                .location(advert.getLocation())
                .user(userMapper.toUserResponse(advert.getUser()))
                .build();
    }

    public DetailedAdvertResponse toResponseForSave(Advert advert) {
        return DetailedAdvertResponse.builder()
                .id(advert.getId())
                .createdAt(advert.getCreatedAt())
                .updatedAt(advert.getUpdatedAt())
                .title(advert.getTitle())
                .description(advert.getDescription())
                .price(advert.getPrice())
                .statusForAdvert(advert.getStatusForAdvert())
                .advertType(toResponse(advert.getAdvertType()))
                .viewCount(advert.getViewCount())
                .builtIn(advert.isBuiltIn())
                .isActive(advert.isActive())
                .country(locationMapper.toCountryResponse(advert.getCountry()))
                .city(locationMapper.toCityResponse(advert.getCity()))
                .district(locationMapper.toDistrictResponse(advert.getDistrict()))
//                .images(advert.getImages()) UNNECESSARY
                .location(advert.getLocation())
                .user(userMapper.toUserResponse(advert.getUser()))
                .category(categoryMapper.mapToCategoryResponsewithPropertyKeys(advert.getCategory()))
                .slug(advert.getSlug())
                .build();
    }


    public void toEntityForUpdate(Advert pojo, AdvertRequestForUpdateByAdmin request) {
        pojo.setTitle(request.getTitle());
        pojo.setDescription(request.getDesc());
        pojo.setPrice(request.getPrice());
        pojo.setLocation(request.getLocation());
        pojo.setStatusForAdvert(request.getStatusForAdvert());
    }

    public void toEntityForUpdateCustomer(Advert pojo, AdvertRequestForUpdateByCustomer request) {
        pojo.setTitle(request.getTitle());
        pojo.setDescription(request.getDesc());
        pojo.setPrice(request.getPrice());
        pojo.setActive(request.getActive());
        pojo.setLocation(request.getLocation());
    }


    public SimpleAdvertResponse toSimpleAdvertResponse(Advert advert) {

        return SimpleAdvertResponse.builder()
                .id(advert.getId())
                .title(advert.getTitle())
                .price(advert.getPrice())
                .statusForAdvert(advert.getStatusForAdvert())
                .city(locationMapper.toCityResponse(advert.getCity()))
                .district(locationMapper.toDistrictResponse(advert.getDistrict()))
                .country(locationMapper.toCountryResponse(advert.getCountry()))
                .image(imageMapper.toImageResponse(getFeaturedImage(advert.getImages())))
                .slug(advert.getSlug())
                .location(advert.getLocation())
                .build();
    }


    //NOT ADVERT TYPE

    public AdvertTypeResponse toResponse(AdvertType advertType) {
        return AdvertTypeResponse.builder()
                .id(advertType.getId())
                .title(advertType.getTitle())
                .builtIn(advertType.isBuiltIn())
                .build();
    }

    public AdvertType toEntity(AdvertTypeRequest request) {
        return AdvertType.builder()
                .title(request.getTitle())
                .build();
    }


    //NOT FAVOURITE

    public AdvertResponseForFavorite toAdvertResponseForFavorite(Long favoriteId, Advert advert) {
        return AdvertResponseForFavorite.builder()
                .favoriteId(favoriteId)
                .advertId(advert.getId())
//                .ownerId(advert.getUser().getId())
                .title(advert.getTitle())
                .price(advert.getPrice())
                .location(advert.getLocation())
                .advertType(toResponse(advert.getAdvertType()))
                .country(locationMapper.toCountryResponse(advert.getCountry()))
                .city(locationMapper.toCityResponse(advert.getCity()))
                .district(locationMapper.toDistrictResponse(advert.getDistrict()))
                .category(categoryMapper.toCategoryResponseForFavorite(advert.getCategory()))
                .image(imageMapper.toImageResponse(getFeaturedImage(advert.getImages())))
                .slug(advert.getSlug())
                .build();
    }


    //NOT TOURREQUEST

    public AdvertResponseForTourRequest toAdvertResponseForTourRequest(Advert advert) {
        return AdvertResponseForTourRequest.builder()
                .id(advert.getId())
                .price(advert.getPrice())
                .title(advert.getTitle())
                .city(locationMapper.toCityResponse(advert.getCity()))
                /* .images(advert.getImages())*/
                .location(advert.getLocation())
                .district(locationMapper.toDistrictResponse(advert.getDistrict()))
                .country(locationMapper.toCountryResponse(advert.getCountry()))
                .slug(advert.getSlug())
                .build();
    }

    //Not Helper Method to get featured image
    private Image getFeaturedImage(List<Image> images) {
        return images.stream()
                .filter(Image::isFeatured)
                .findFirst()
                .orElse(images.get(0));
    }


    //Not Helper Method to get sorted by featured
    private List<ImageResponse> sortedByFeatured(List<Image> images) {
        images.sort(Comparator.comparing(Image::isFeatured).reversed());
        return images.stream().map(imageMapper::toImageResponse).toList();
    }

}
