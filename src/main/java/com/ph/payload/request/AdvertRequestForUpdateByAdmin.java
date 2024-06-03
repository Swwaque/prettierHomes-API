package com.ph.payload.request;

import com.ph.domain.enums.StatusForAdvert;
import com.ph.payload.request.abstracts.AdvertRequestAbs;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Setter
@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class AdvertRequestForUpdateByAdmin extends AdvertRequestAbs {

    private StatusForAdvert statusForAdvert;
    private List<String> propertyValues;

}
