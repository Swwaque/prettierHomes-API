package com.ph.service;

import com.ph.domain.entities.DailyReport;
import com.ph.domain.entities.TourRequest;
import com.ph.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class SchedulerService {

    private final EmailService emailService;
    private final TourRequestsService tourRequestsService;
    private final AdvertRepository advertRepository;
    private final TourRequestsRepository tourRequestsRepository;
    private final FavoritesRepository favoritesRepository;
    private final UserRepository userRepository;
    private final DailyReportRepository dailyReportRepository;
    private final ContactRepository contactRepository;

    @Scheduled(cron = "${scheduler.cron.daily-at-midnight-3}")
    public void sendReminderEmails() {
        List<TourRequest> tourRequestList = tourRequestsService.checkingTheDatesOfTourRequests();
        if (tourRequestList.size() > 0) {
            tourRequestList.forEach(emailService::sendInformationEmail);
        }

    }

    @Scheduled(cron = "${scheduler.cron.daily-at-midnight-2}")
    public void updateExpiredPendingTourRequests() {
        List<TourRequest> expiredPendingTourRequests = tourRequestsService.getExpiredPendingTourRequests();
        if (!expiredPendingTourRequests.isEmpty()) {
            tourRequestsService.declineExpiredPendingTourRequests(expiredPendingTourRequests);
        }
    }

    @Scheduled(cron = "${scheduler.cron.daily-at-midnight-1}")
    public void saveDailyReport() {
        if (dailyReportRepository.existsByDate(LocalDate.now())) {
            return;
        }
        DailyReport dailyReport = DailyReport.builder()
                .date(LocalDate.now())
                .numberOfRentAdverts(advertRepository.countActivatedRentAdverts())
                .numberOfSaleAdverts(advertRepository.countActivatedSaleAdverts())
                .numberOfContactMessage((int) contactRepository.count())
                .numberOfTourRequests((int) tourRequestsRepository.count())
                .numberOfFavorites((int) favoritesRepository.count())
                .numberOfUsers((int) userRepository.count())
                .build();
        dailyReportRepository.save(dailyReport);
    }


    @Cacheable(value = "dailyReport")
    public Map<String, Map<LocalDate, Integer>> getDailyReport() {
        LocalDate currentDate = LocalDate.now();
        LocalDate startDate = currentDate.minusDays(7);
        List<DailyReport> reports = dailyReportRepository.findByDateBetween(startDate, currentDate).stream().sorted(Comparator.comparing(DailyReport::getDate)).toList();
        Map<String, Map<LocalDate, Integer>> resultMap = new TreeMap<>();

        for (DailyReport report : reports) {
            LocalDate date = report.getDate();

            // numberOfAdverts
            resultMap.computeIfAbsent("RentAdverts", k -> new TreeMap<>())
                    .merge(date, report.getNumberOfRentAdverts(), Integer::sum);

            resultMap.computeIfAbsent("SaleAdverts", k -> new TreeMap<>())
                    .merge(date, report.getNumberOfSaleAdverts(), Integer::sum);

            // numberOfContactMessage
            resultMap.computeIfAbsent("ContactMessages", k -> new TreeMap<>())
                    .merge(date, report.getNumberOfContactMessage(), Integer::sum);

            // numberOfFavorites
            resultMap.computeIfAbsent("Favorites", k -> new TreeMap<>())
                    .merge(date, report.getNumberOfFavorites(), Integer::sum);

            // numberOfTourRequests
            resultMap.computeIfAbsent("TourRequests", k -> new TreeMap<>())
                    .merge(date, report.getNumberOfTourRequests(), Integer::sum);

            // numberOfUsers
            resultMap.computeIfAbsent("Users", k -> new TreeMap<>())
                    .merge(date, report.getNumberOfUsers(), Integer::sum);
        }
        return resultMap;
    }


}