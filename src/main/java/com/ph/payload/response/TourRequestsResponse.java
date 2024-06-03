package com.ph.payload.response;

import com.ph.domain.enums.Status;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TourRequestsResponse implements Serializable {

    private Long id;
    private LocalDate tourDate;
    private LocalTime tourTime;
    private Status status;

}
