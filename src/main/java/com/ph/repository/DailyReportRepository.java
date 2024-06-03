package com.ph.repository;

import com.ph.domain.entities.DailyReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DailyReportRepository extends JpaRepository<DailyReport, Long> {
    @Query("select d from DailyReport d where d.date between ?1 and ?2")
    List<DailyReport> findByDateBetween(LocalDate dateStart, LocalDate dateEnd);

    boolean existsByDate(LocalDate now);
}
