package com.ph.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class FavoriteCountResponse {

    private Long advertId;
    private Long favoriteCount;

}
