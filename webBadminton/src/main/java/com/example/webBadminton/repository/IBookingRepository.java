package com.example.webBadminton.repository;

import com.example.webBadminton.model.booking.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.example.webBadminton.model.BookingCourt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
@Repository
public interface IBookingRepository extends JpaRepository<BookingCourt, Long> {
    @Query("SELECT b FROM Booking b WHERE b.court.badmintonId = ?1 AND b.court.courtId = ?2 AND b.date = ?3")
    List<BookingCourt> findBookingsByCourtAndDate(Long badmintonId, Long courtId, LocalDate date);

    List<BookingCourt> findByBookingDate(LocalDate date);
}
