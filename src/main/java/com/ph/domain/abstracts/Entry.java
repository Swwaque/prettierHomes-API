package com.ph.domain.abstracts;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@SuperBuilder
@MappedSuperclass
@AllArgsConstructor
@NoArgsConstructor
/**
 * This class handles the timestamps of the entities
 */
public abstract class Entry implements Serializable {

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    //DB create-update timestamps
    @PrePersist
    private void createdTime() {
        setCreatedAt(LocalDateTime.now());
    }

    @PreUpdate
    private void updatedTime() {
        setUpdatedAt(LocalDateTime.now());
    }


    @Override
    public String toString() {
        return "Entry{" +
                "createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

}
