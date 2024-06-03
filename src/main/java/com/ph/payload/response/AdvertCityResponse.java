package com.ph.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
public class AdvertCityResponse implements Serializable {

    private Long cityId;
    private String cityName;
    private Long cityQuantity;

}
