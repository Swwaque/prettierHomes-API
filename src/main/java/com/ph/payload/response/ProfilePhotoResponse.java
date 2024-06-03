package com.ph.payload.response;


import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfilePhotoResponse implements Serializable {

    private Long id;
    private String name;
    private String type;
    private String data;

}
