package com.ph.payload.request.abstracts;

import com.ph.domain.entities.Location;
import jakarta.annotation.Nullable;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@Getter
@Setter
@SuperBuilder
@MappedSuperclass
@AllArgsConstructor
@NoArgsConstructor
public abstract class AdvertRequestAbs implements Serializable {

    @NotNull(message = "validation.advert.notnull")
    @NotBlank(message = "validation.advert.notblank")
    @Size(min = 5, max = 150, message = "validation.advert.size")
    private String title;

    @Nullable
    @Size(max = 300, message = "validation.advert.desc.size")
    private String desc;

    @NotNull(message = "validation.advert.price.notnull")
    private Double price;

    @NotNull(message = "validation.advert.category.notnull")
    private Long categoryId;

    @NotNull(message = "validation.advert.type.notnull")
    private Long advertTypeId;

    @NotNull(message = "validation.advert.country.notnull")
    private Long countryId;

    @NotNull(message = "validation.advert.city.notnull")
    private Long cityId;

    @NotNull(message = "validation.advert.district.notnull")
    private Long districtId;

    @Valid
    private Location location;

    @NotNull(message = "validation.advert.address.notnull")
    @NotBlank(message = "validation.advert.address.notblank")
    @Size(min = 2, max = 100, message = "validation.advert.address.size")
    private String address;
}
