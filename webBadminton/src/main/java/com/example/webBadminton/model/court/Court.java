package com.example.webBadminton.model;

import lombok.*;

import jakarta.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@Entity
@Table(name = "court")
public class Court {

    @Id
    private Long badmintonId;

    @Id
    private Long courtId;

    private String details;
}

public class CourtId implements Serializable {
    private Long badmintonId;
    private Long courtId;

    // constructors, getters, setters, equals, and hashCode
}
