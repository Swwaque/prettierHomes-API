package com.ph.payload.response;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class CategoryResponse implements Serializable {

    private Long id;
    private String title;
    private String slug;
    private String icon;
    private int seq;
    private boolean builtIn;
    private boolean active;
    private List<CategoryPropertyKeyResponse> categoryPropertyKeysResponse;

}
