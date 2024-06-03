package com.ph.service;

import com.ph.domain.entities.Advert;
import com.ph.domain.entities.Category;
import com.ph.domain.entities.DailyReport;
import com.ph.domain.entities.User;
import com.ph.repository.AdvertRepository;
import com.ph.repository.CategoryRepository;
import com.ph.repository.DailyReportRepository;
import com.ph.repository.UserRepository;
import com.ph.utils.MessageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ResetDatabaseService {

    private final AdvertRepository advertRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final CacheManager cacheManager;
    private final DailyReportRepository dailyReportRepository;
    private final MessageUtil messageUtil;


    public ResponseEntity<String> resetDatabase() {

        // Not: Constraint: Can not delete  builtIn objects
        // delete all not builtIn User and related objects
        List<User> users = userRepository.findAllByBuiltIn(false);
        userRepository.deleteAll(users);

        // delete all not builtIn Category and related objects
        List<Category> categories = categoryRepository.findAllByBuiltIn(false);
        categoryRepository.deleteAll(categories);

        // delete all not builtIn Advert and related objects
        List<Advert> adverts = advertRepository.findAllByBuiltIn(false);
        advertRepository.deleteAll(adverts);

        // delete all DailyReport
        List<DailyReport> dailyReports = dailyReportRepository.findAll();
        dailyReportRepository.deleteAll(dailyReports);


        // clear all caches
        clearAllCaches();


        return ResponseEntity.ok(messageUtil.getMessage("success.db.reset"));
    }

    private void clearAllCaches() {
        cacheManager.getCacheNames().stream()
                .map(cacheManager::getCache)
                .filter(Objects::nonNull)
                .forEach(Cache::clear);
    }

}
