package com.example.webBadminton.controller;

import com.example.webBadminton.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class BookingController {
    @Autowired
    private BookingService bookingService;

    @GetMapping("/api/booked-times/{date}")
    public ResponseEntity<List<String>> getBookedTimes(@PathVariable LocalDate date) {
        List<LocalTime> bookedTimes = bookingService.getBookedTimes(date);
        List<String> times = bookedTimes.stream()
                .map(time -> time.toString())
                .collect(Collectors.toList());
        return ResponseEntity.ok(times);
    }
}
