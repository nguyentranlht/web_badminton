package com.example.webBadminton.service;

import com.example.webBadminton.model.booking.Booking;
import com.example.webBadminton.model.court.Court;
import com.example.webBadminton.repository.IBookingRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class TimeSlotService {

    @Autowired
    private IBookingRepository bookingRepository;

    public List<LocalTime[]> generateTimeSlots(Court court) {
        LocalTime startTime = court.getBadminton().getOpeningTime();
        LocalTime endTime = court.getBadminton().getClosingTime();

        List<LocalTime[]> timeSlots = new ArrayList<>();
        while (!startTime.isAfter(endTime.minusHours(1))) {
            LocalTime slotEnd = startTime.plusHours(1);  // Assuming 1-hour time slots
            timeSlots.add(new LocalTime[]{startTime, slotEnd});
            startTime = slotEnd;
        }
        return timeSlots;
    }
    public boolean isTimeSlotAvailable(Court court, LocalDate date, LocalTime startTime, LocalTime endTime) {
        // Validate against the court's specific or inherited opening times
        if(startTime.isBefore(court.getBadminton().getOpeningTime()) || endTime.isAfter(court.getBadminton().getClosingTime())) {
            return false;
        }

        // Fetch existing bookings from the database
        List<Booking> bookings = bookingRepository.findBookingsByCourtAndDate(court.getBadmintonId(), court.getCourtId(), date);
        for (Booking booking : bookings) {
            if (startTime.isBefore(booking.getEndTime()) && endTime.isAfter(booking.getStartTime())) {
                return false; // Slot overlaps with an existing booking
            }
        }

        return true;
    }
}
