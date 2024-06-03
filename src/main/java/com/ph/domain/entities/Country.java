package com.ph.domain.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "countries")
public class Country implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 30)
    private String name;

    @Embedded
    private Location location;

    /**
     * Entity relationships start
     */

    @OneToMany(mappedBy = "country", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Advert> adverts;

    @OneToMany(mappedBy = "country", cascade = CascadeType.ALL)
    private List<City> cities;
    /**
     * Entity relationships end
     */

    /**
     * Equals and HashCode - ToString methods start
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Country country)) return false;
        return getId().equals(country.getId()) && getName().equals(country.getName()) && getAdverts().equals(country.getAdverts()) && getCities().equals(country.getCities());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getAdverts(), getCities());
    }

    @Override
    public String toString() {
        return "Country{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    /**
     * Equals and HashCode - ToString methods end
     */

}
