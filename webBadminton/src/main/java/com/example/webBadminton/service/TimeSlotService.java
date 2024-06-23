package com.example.webBadminton.service;

import com.example.webBadminton.model.BookingCourt;
import com.example.webBadminton.model.court.Court;
import com.example.webBadminton.repository.IBookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
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
        List<BookingCourt> bookings = bookingRepository.findBookingsByCourtAndDate(court.getBadmintonId(), court.getCourtId(), date);
        for (BookingCourt booking : bookings) {
            if (startTime.isBefore(booking.getEndTime()) && endTime.isAfter(booking.getStartTime())) {
                return false; // Slot overlaps with an existing booking
            }
        }

        return true;
    }

    public List<LocalTime[]> filterAvailableTimeSlots(List<LocalTime[]> allTimeSlots, LocalDate date, Court court) {
        List<BookingCourt> bookings = bookingRepository.findBookingsByCourtAndDate(court.getBadmintonId(), court.getCourtId(), date);
        List<LocalTime[]> availableSlots = new ArrayList<>(allTimeSlots);
        for (LocalTime[] timeSlot : allTimeSlots){
            for (BookingCourt booking : bookings) {
                LocalTime startTime = booking.getStartTime();
                LocalTime endTime = booking.getEndTime();

                if ((timeSlot[0] == startTime && timeSlot[1] == endTime))
                {
                    availableSlots.remove(timeSlot);
                }
            }
        }

        return availableSlots;
    }

}
