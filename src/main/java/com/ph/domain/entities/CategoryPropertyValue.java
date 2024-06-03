package com.ph.domain.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "category_property_values")

public class CategoryPropertyValue implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100)
    private String value;

    /**
     * Entity relationships start
     */

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false )// Every category property values must have category property key
    private CategoryPropertyKey categoryPropertyKey;

    @ManyToOne(fetch = FetchType.LAZY)
    private Advert advert;
    /**
     * Entity relationships end
     */

    /**
     * Equals and HashCode - ToString methods start
     */

    @Override
    public String toString() {
        return "CategoryPropertyValue{" +
                "id=" + id +
                ", value='" + value + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CategoryPropertyValue that)) return false;
        return Objects.equals(id, that.id) && Objects.equals(value, that.value) && Objects.equals(categoryPropertyKey, that.categoryPropertyKey) && Objects.equals(advert, that.advert);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, value, categoryPropertyKey, advert);
    }

/**
     * Equals and HashCode - ToString methods end
     */

}
