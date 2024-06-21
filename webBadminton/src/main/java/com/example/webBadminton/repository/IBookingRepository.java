package com.example.webBadminton.repository;

import com.example.webBadminton.model.BookingCourt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface IBookingRepository extends JpaRepository<BookingCourt, Long> {
    List<BookingCourt> findByBookingDate(LocalDate date);
}
