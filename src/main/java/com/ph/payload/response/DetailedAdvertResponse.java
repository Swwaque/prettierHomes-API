package com.ph.payload.response;

import com.ph.domain.enums.StatusForAdvert;
import com.ph.domain.entities.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class DetailedAdvertResponse implements Serializable {

    private Long id;
    private String title;
    private String description;
    private Double price;
    private StatusForAdvert statusForAdvert;
    private boolean builtIn;
    private boolean isActive;
    private int viewCount;
    private Location location;
    private AdvertTypeResponse advertType;
    private CountryResponse country;
    private CityResponse city;
    private DistrictResponse district;
    private String address;
    private UserResponse user;
    private CategoryResponse category;
    private List<ImageResponse> images;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<Favorite> favorites;
    private List<PropertyValueResponse> categoryPropertyValue;
    private String slug;

}
