package com.ph.payload.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ph.domain.entities.TourRequest;
import com.ph.domain.enums.Status;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.function.Supplier;

@Setter
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class TourRequestRequest implements Supplier<TourRequest>, Serializable {

    @NotNull(message = "{validation.tour.request.tour.date.notnull}")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Future(message = "{validation.tour.request.tour.date.future}")
    private LocalDate tourDate;

    @NotNull(message = "{validation.tour.request.tour.time.notnull}")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm", timezone = "US")
    private LocalTime tourTime;

    @NotNull(message = "{validation.advert.id.notnull}")
    private Long advertId;

    @Override
    public TourRequest get() {
        return TourRequest.builder()
                .tourDate(getTourDate())
                .tourTime(getTourTime())
                .status(Status.PENDING)
                .build();
    }

}
