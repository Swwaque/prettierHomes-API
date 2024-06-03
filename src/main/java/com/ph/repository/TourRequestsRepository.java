package com.ph.repository;

import com.ph.domain.entities.TourRequest;
import com.ph.domain.enums.Status;
import com.ph.payload.response.TourRequestCountResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;


@Repository
public interface TourRequestsRepository extends JpaRepository<TourRequest, Long> {

    @Query("""
            select (count(t) > 0) from TourRequest t
            where t.tourDate = ?1 and t.tourTime = ?2 and t.status = ?3 and t.guestUser.id = ?4""")
    boolean existsByTourDateAndTourTimeAndStatusAndGuestUser_Id(LocalDate tourDate, LocalTime tourTime, Status status, Long id);

    @Query("select (count(t) > 0) from TourRequest t where t.tourDate = ?1 and t.tourTime = ?2 and t.status = ?3")
    boolean existsByTourDateAndTourTimeAndStatus(LocalDate tourDate, LocalTime tourTime, Status status);

    List<TourRequest> findByTourDateEquals(LocalDate tourDate);

    Page<TourRequest> findByAdvert_Id(Long id, Pageable pageable);

    Optional<TourRequest> findByIdAndGuestUser_Id(Long id, Long id1);

    Page<TourRequest> findAllByGuestUser_Id(Long id, Pageable pageable);

    @Query("select t from TourRequest t where " +
            "(t.tourDate between :start_date and :end_date) and " +
            "(:status is null or t.status = :status)")
    List<TourRequest> findForExcel(
            @Param("start_date") LocalDate startDate,
            @Param("end_date") LocalDate endDate,
            @Param("status") Status status
    );

    Long countByAdvert_IdAndOwnerUser_Id(Long advertId, Long id);

    @Query("""
            select t from TourRequest t
            where t.advert.title ilike concat('%', ?1, '%')""")
    Page<TourRequest> search(String query, Pageable pageable);


    // Not: getTour\requestCount new
    @Query("SELECT NEW com.ph.payload.response.TourRequestCountResponse(tr.advert.id, COUNT(tr)) " +
            "FROM TourRequest tr " +
            "GROUP BY tr.advert.id")
    List<TourRequestCountResponse> getCountsTourRequests();

    @Query("SELECT NEW com.ph.payload.response.TourRequestCountResponse(tr.advert.id, COUNT(tr)) " +
            "FROM TourRequest tr " +
            "WHERE tr.advert.id IN :advertIds " +
            "GROUP BY tr.advert.id")
    List<TourRequestCountResponse> getCountsTourRequestsCustomer(@Param("advertIds") List<Long> advertIds);

    List<TourRequest> findAllByTourDateAndTourTime(LocalDate requestDate, LocalTime requestTime);

    List<TourRequest> findAllByTourDateAndTourTimeAndStatus(LocalDate requestDate, LocalTime requestTime, Status approved);

    @Query("SELECT tr FROM TourRequest tr WHERE tr.tourDate < :today AND tr.status = :status")
    List<TourRequest> findPendingTourRequestsBeforeToday(LocalDate today, Status status);

    Page<TourRequest> findByOwnerUser_Id(@NonNull Long id, Pageable pageable);
}