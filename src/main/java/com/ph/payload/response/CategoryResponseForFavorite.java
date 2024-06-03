package com.ph.payload.response;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class CategoryResponseForFavorite implements Serializable {

    private Long id;
    private String title;

}

