package com.example.webBadminton.service;

import com.example.webBadminton.model.BookingCourt;
import com.example.webBadminton.model.court.Badminton;
import com.example.webBadminton.repository.IBookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class BookingService {
    @Autowired
    IBookingRepository bookingRepository;
    public List<BookingCourt> getAllBooking() {
        return bookingRepository.findAll();
    }

    public Optional<BookingCourt> getBookingById(Long id) {
        return bookingRepository.findById(id);
    }

    public void addBooking(BookingCourt bookingCourt) {
        bookingRepository.save(bookingCourt);
    }

    public List<LocalTime> getBookedTimes(LocalDate date) {
        // Lấy tất cả các bản ghi đặt sân cho ngày cụ thể
        List<BookingCourt> bookings = bookingRepository.findByBookingDate(date);
        return bookings.stream()
                .map(BookingCourt::getStartTime)
                .collect(Collectors.toList());
    }

    public List<BookingCourt> getBookingsByBadminton(List<Badminton> badmintonList) {
        List<Long> badmintonIds = badmintonList.stream().map(Badminton::getId).collect(Collectors.toList());
        return bookingRepository.findBookingsByBadmintonIds(badmintonIds);
    }

    public void deleteBooking(Long id) {
        bookingRepository.deleteById(id);
    }

    public void updateBooking(BookingCourt bookingCourt) {
        bookingRepository.save(bookingCourt);
    }
}
