package com.ph.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdvertTypeRequest {

    @NotNull(message = "{validation.advert.notnull}")
    @NotBlank(message = "{validation.advert.type.notblank}")
    private String title;

}
