package com.example.webBadminton.modelView;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class SearchCriteria {
    private String province;
    private String district;
    private String ward;
    private LocalDate day;
    private LocalTime startTime;
    private LocalTime endTime;
}
