package com.ph.payload.response;

import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdvertTypeResponse implements Serializable {

    private Long id;
    private String title;
    private boolean builtIn;

}
