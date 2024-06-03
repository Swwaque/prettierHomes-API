package com.ph.payload.response;

import com.ph.domain.enums.KeyType;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class CategoryPropertyKeyResponse implements Serializable {

    private Long id;
    private String name;
    private KeyType keyType;
    private String prefix;
    private String suffix;
    private Boolean builtIn;

}
