package com.ph.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
public class AdvertCategoryResponse implements Serializable {

    private Long categoryId;
    private String categoryName;
    private String icon;
    private Long categoryQuantity;

}
