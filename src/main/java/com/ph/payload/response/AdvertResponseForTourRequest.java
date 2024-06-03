package com.ph.payload.response;

import com.ph.domain.entities.Image;
import com.ph.domain.entities.Location;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class AdvertResponseForTourRequest implements Serializable {

    private Long id;
    private String title;
    private Double price;
    private Location location;
    private CountryResponse country;
    private CityResponse city;
    private DistrictResponse district;
    private List<Image> images;
    private String slug;

}
