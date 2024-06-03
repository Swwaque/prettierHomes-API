package com.ph.payload.mapper;

import com.ph.domain.entities.Log;
import com.ph.payload.response.LogResponse;
import org.springframework.stereotype.Component;

@Component
public class LogMapper {
    public LogResponse toLogResponse(Log log) {
        return LogResponse.builder()
                .id(log.getId())
                .message(log.getMessage())
                .createdAt(log.getCreatedAt())
                .build();
    }
}
