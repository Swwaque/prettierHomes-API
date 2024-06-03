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
public class AdvertRequest extends AdvertRequestAbs {

    private List<String> propertyValues;

}
