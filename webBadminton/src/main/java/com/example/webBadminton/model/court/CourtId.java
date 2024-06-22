package com.example.webBadminton.model.court;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class CourtId implements Serializable {
    private Long badmintonId;
    private Long courtId;
}
