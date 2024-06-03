package com.ph.domain.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ph.domain.abstracts.Entry;
import com.ph.domain.enums.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

@Entity
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tour_requests")
public class TourRequest extends Entry implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @NotNull
    private LocalDate tourDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    @NotNull
    private LocalTime tourTime;//Requarement'ta DateTime diyor biz Time aldık

    @NotNull
    private Status status;

    /**
     * Entity relationships start
     */

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "advert_id")
    @NotNull
    private Advert advert;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_user_id")
    @NotNull
    private User ownerUser;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "guest_user_id")
    @NotNull
    private User guestUser;

    /**
     * Entity relationships end
     */

    /**
     * Equals and HashCode - ToString methods start
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TourRequest that)) return false;
        return getId().equals(that.getId()) && getTourDate().equals(that.getTourDate()) && getTourTime()
                .equals(that.getTourTime()) && getStatus() == that.getStatus() && getAdvert()
                .equals(that.getAdvert()) && getOwnerUser().equals(that.getOwnerUser()) && getGuestUser().equals(that.getGuestUser());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTourDate(), getTourTime(), getStatus(), getAdvert(), getOwnerUser(), getGuestUser());
    }

    @Override
    public String toString() {
        return "TourRequest{" +
                "id=" + id +
                ", tourDate=" + tourDate +
                ", tourTime=" + tourTime +
                ", status=" + status +
                '}';
    }

    /**
     * Equals and HashCode - ToString methods end
     */

}
