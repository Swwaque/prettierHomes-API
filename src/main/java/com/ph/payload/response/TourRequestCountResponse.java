package com.ph.payload.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
public class TourRequestCountResponse implements Serializable {

    private  Long advertId;
    private  Long TourRequestCount;



}
