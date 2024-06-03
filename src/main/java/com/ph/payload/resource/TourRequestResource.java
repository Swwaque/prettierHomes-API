package com.ph.payload.resource;

import com.ph.aboutexcel.ExportToExcel;
import com.ph.domain.enums.Status;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class TourRequestResource implements Serializable {

    @ExportToExcel(index = 0, headerText = "TOUR ID", width = 2000)
    private Long id;

    @ExportToExcel(index = 1, headerText = "TOUR DATE", width = 5000)
    private LocalDate tourDate;

    @ExportToExcel(index = 2, headerText = "TOUR TIME", width = 5000)
    private LocalTime tourTime;

    @ExportToExcel(index = 3, headerText = "STATUS", width = 3000)
    private Status status;

    @ExportToExcel(index = 4, headerText = "ADVERT ID", width = 4500)
    private Long advertId;

    @ExportToExcel(index = 5, headerText = "ADVERT INFO", width = 7000)
    private String advertTitle;

    @ExportToExcel(index = 6, headerText = "OWNER FULL NAME ", width = 6000)
    private String ownerFullName;

    @ExportToExcel(index = 7, headerText = "GUEST FULL NAME", width = 6000)
    private String guestFullName;

}
