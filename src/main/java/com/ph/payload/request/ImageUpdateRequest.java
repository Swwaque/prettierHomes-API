package com.ph.payload.request;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.io.Serializable;
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ImageUpdateRequest implements Serializable {

    @NotNull(message = "validation.image.id-notnull")
    Long imgId;
    @NotNull(message = "validation.advert.id.notnull")
    Long advertId;
}
