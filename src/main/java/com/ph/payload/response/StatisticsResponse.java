package com.ph.payload.response;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StatisticsResponse implements Serializable {

    private Long totalAdverts;
    private Long totalUsers;
    private Long totalAdvertTypes;
    private Long totalTourRequests;
    private Long totalCategories;

}
