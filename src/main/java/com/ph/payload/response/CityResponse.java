package com.ph.payload.response;

import com.ph.domain.entities.Location;
import jakarta.persistence.Embedded;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class CityResponse implements Serializable {

    private Long id;
    private String name;

    @Embedded
    private Location location;

//    private List<AdvertResponseForTourRequest> advertsResponse;
//    private CountryResponse countryResponse;
//    private List<DistrictResponse> districtsResponse;
}
