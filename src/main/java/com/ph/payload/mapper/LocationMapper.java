package com.ph.payload.mapper;

import com.ph.domain.entities.City;
import com.ph.domain.entities.Country;
import com.ph.domain.entities.District;
import com.ph.payload.response.CityResponse;
import com.ph.payload.response.CountryResponse;
import com.ph.payload.response.DistrictResponse;
import org.springframework.stereotype.Component;

@Component
public class LocationMapper {


    //NOT: COUNTRY MAPPER
    public CountryResponse toCountryResponse(Country country) {
        return CountryResponse.builder()
                .id(country.getId())
                .name(country.getName())
                .location(country.getLocation())
                /*.advertsResponse(country.getAdverts().stream().map(advertMapper::toAdvertResponseForTourRequest).collect(Collectors.toList()))
                .citiesResponse(country.getCities().stream().map(cityMapper::toCityResponse).collect(Collectors.toList()))*/
                .build();
    }

    //NOT: CITY MAPPER
    public CityResponse toCityResponse(City city) {
        return CityResponse.builder()
                .id(city.getId())
                .name(city.getName())
                .location(city.getLocation())
                /*.countryResponse(countryMapper.toCountryResponse(city.getCountry()))
                .advertsResponse(city.getAdverts().stream().map(advertMapper::toAdvertResponseForTourRequest).collect(Collectors.toList()))
                .districtsResponse(city.getDistricts().stream().map(districtMapper::toDistrictResponse).collect(Collectors.toList()))*/
                .build();
    }

    //NOT: DISTRICTS MAPPER
    public DistrictResponse toDistrictResponse(District district) {
        return DistrictResponse.builder()
                .id(district.getId())
                .name(district.getName())
                .location(district.getLocation())
                /*.advertsResponse(district.getAdverts().stream().map(advertMapper::toAdvertResponseForTourRequest).collect(Collectors.toList()))
                .cityResponse(cityMapper.toCityResponse(district.getCity()))*/
                .build();
    }


}
