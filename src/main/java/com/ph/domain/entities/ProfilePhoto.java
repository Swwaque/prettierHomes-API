package com.ph.domain.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "profile_photos")
public class ProfilePhoto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    private byte[] data;
    private String name;
    private String type;

    /**
     * Entity relationships start
     */

    @OneToOne(mappedBy = "profilePhoto")
    private User user;

    /**
     * Entity relationships end
     */

}
