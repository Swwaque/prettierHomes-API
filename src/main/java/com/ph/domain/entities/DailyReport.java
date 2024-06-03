package com.ph.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "dailyReports")
public class DailyReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate date;
    private Integer numberOfRentAdverts;
    private Integer numberOfSaleAdverts;
    private Integer numberOfContactMessage;
    private Integer numberOfFavorites;
    private Integer numberOfTourRequests;
    private Integer numberOfUsers;


    @Override
    public String toString() {
        return "DailyReport{" +
                "id=" + id +
                ", date=" + date +
                ", numberOfRentAdverts=" + numberOfRentAdverts +
                ", numberOfSaleAdverts=" + numberOfSaleAdverts +
                ", numberOfContactMessage=" + numberOfContactMessage +
                ", numberOfFavorites=" + numberOfFavorites +
                ", numberOfTourRequests=" + numberOfTourRequests +
                ", numberOfUsers=" + numberOfUsers +
                '}';
    }
}
