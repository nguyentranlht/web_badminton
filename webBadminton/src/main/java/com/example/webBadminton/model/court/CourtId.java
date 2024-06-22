package com.example.webBadminton.model.court;


import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

@Data
public class CourtId implements Serializable {
    private Long badmintonId;
    private Long courtId;

    public CourtId() {
    }

    public CourtId(Long badmintonId, Long courtId) {
        this.badmintonId = badmintonId;
        this.courtId = courtId;
    }

    // Getters and setters

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CourtId courtId = (CourtId) o;
        return Objects.equals(badmintonId, courtId.badmintonId) &&
                Objects.equals(courtId, courtId.courtId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(badmintonId, courtId);
    }
}
