package com.ph.payload.response;


import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class PropertyValueResponse implements Serializable {

    private Long id;
    private String value;

}
