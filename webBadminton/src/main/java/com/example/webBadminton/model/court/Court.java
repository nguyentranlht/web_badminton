package com.example.webBadminton.model.court;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@IdClass(CourtId.class)
@Table(name = "court")
public class Court {

    @Id
    private Long badmintonId;

    @Id
    private Long courtId;

    private String details;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "badmintonId", referencedColumnName = "id", insertable = false, updatable = false)
    })
    private Badminton badminton;

}


