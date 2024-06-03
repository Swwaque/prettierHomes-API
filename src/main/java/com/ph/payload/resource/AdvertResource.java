package com.ph.payload.resource;

import com.ph.aboutexcel.ExportToExcel;
import com.ph.domain.enums.StatusForAdvert;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class AdvertResource implements Serializable {

    @ExportToExcel(index = 0, headerText = "PROPERTY ID", width = 4500)
    private Long id;

    @ExportToExcel(index = 1, headerText = "TITLE", width = 5000)
    private String title;

    @ExportToExcel(index = 2, headerText = "DESCRIPTION", width = 10000)
    private String description;

    @ExportToExcel(index = 3, headerText = "PRICE", width = 3000)
    private Double price;

    @ExportToExcel(index = 4, headerText = "STATUS", width = 3000)
    private StatusForAdvert statusForAdvert;

    @ExportToExcel(index = 5, headerText = "BUILT IN", width = 2500)
    private boolean builtIn;

    @ExportToExcel(index = 6, headerText = "ACTIVE", width = 2500)
    private boolean isActive;

    @ExportToExcel(index = 7, headerText = "VIEW COUNT", width = 4500)
    private int viewCount;

    @ExportToExcel(index = 8, headerText = "ADVERT TYPE", width = 5000)
    private String advertType;

    @ExportToExcel(index = 9, headerText = "COUNTRY", width = 3000)
    private String countryName;

    @ExportToExcel(index = 10, headerText = "CITY", width = 3000)
    private String cityName;

    @ExportToExcel(index = 11, headerText = "DISTRICT", width = 3000)
    private String districtName;

    @ExportToExcel(index = 12, headerText = "USER", width = 5000)
    private String userName;

    @ExportToExcel(index = 13, headerText = "CREATED AT", width = 8000)
    private LocalDateTime createdAt;

}
