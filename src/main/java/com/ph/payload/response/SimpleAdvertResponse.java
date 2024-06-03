package com.ph.payload.response;


import com.ph.domain.entities.Location;
import com.ph.domain.enums.StatusForAdvert;
import lombok.*;

import java.io.Serializable;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class SimpleAdvertResponse implements Serializable {

    private Long id;
    private String title;
    private StatusForAdvert statusForAdvert;
    private Double price;
    private ImageResponse image;
    private CityResponse city;
    private DistrictResponse district;
    private CountryResponse country;
    private String slug;
    private Location location;

}
