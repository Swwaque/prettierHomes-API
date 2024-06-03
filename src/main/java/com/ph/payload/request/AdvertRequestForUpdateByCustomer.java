package com.ph.payload.request;

import com.ph.payload.request.abstracts.AdvertRequestAbs;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Setter
@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class AdvertRequestForUpdateByCustomer  extends AdvertRequestAbs {

    private Boolean active;
    private List<String> propertyValues;

}
