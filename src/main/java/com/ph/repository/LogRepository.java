package com.ph.repository;

import com.ph.domain.entities.Log;
import com.ph.domain.entities.User;
import com.ph.payload.response.LogResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LogRepository extends JpaRepository<Log, Long> {
    Page<Log> findByUser_Id(Long id,Pageable pageable);

}