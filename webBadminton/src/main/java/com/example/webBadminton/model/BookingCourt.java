package com.example.webBadminton.model;


import jakarta.persistence.*;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Entity
@Table(name = "bookingcourt")
public class BookingCourt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "User cannot be null")
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @NotNull(message = "Booking date cannot be null")
    private LocalDate bookingDate;

    @NotNull(message = "Start time cannot be null")
    private LocalTime startTime;

    @NotNull(message = "End time cannot be null")
    private LocalTime endTime;

    @NotBlank(message = "Status cannot be blank")
    private String status;

    @NotBlank(message = "Status cannot be blank")
    private String phoneNumber ;

    @ManyToOne
    @JoinColumn(name = "court_id", referencedColumnName = "id")
    private Court court;
}
