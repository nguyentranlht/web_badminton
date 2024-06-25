package com.example.webBadminton.service;

import com.example.webBadminton.model.court.Badminton;
import com.example.webBadminton.model.court.Court;
import com.example.webBadminton.model.court.CourtId;
import com.example.webBadminton.repository.ICourtRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional

public class CourtService {
    @Autowired
    private final ICourtRepository courtRepository;
    @Autowired
    private final TimeSlotService timeSlotService;

    public List<Court> getAllCourts() {
        return courtRepository.findAll();
    }

    public List<Court> getAllCourtsByIdBadminton(Long id) {
        return courtRepository.findByBadmintonId(id);
    }

//    public Optional<Court> getCourtById(Long badmintonId, Long courtId) {
//        CourtId id = new CourtId(badmintonId, courtId);
//        return courtRepository.findById(id);
//    }


    public void addCourt(Court court) {
        courtRepository.save(court);
    }

    @Transactional
    public void deleteCourt(Long badmintonId, Long courtId) {
        CourtId id = new CourtId(badmintonId, courtId);
        courtRepository.deleteById(id);
    }

    public Optional<Court> getCourtById(Long badmintonId, Long courtId) {
        CourtId id = new CourtId(badmintonId, courtId);
        return courtRepository.findById(id);
    }

    public void addCourt(Badminton badminton) {
        for (long i = 1; i <= badminton.getCourtQuantity(); i++) {
            Court court = new Court();
            court.setBadmintonId(badminton.getId()); // Set badmintonId directly
            court.setCourtId(i); // Set courtId directly as part of the composite key
            court.setDetails("Mini court #" + i + " of " + badminton.getBadmintonName());
            courtRepository.save(court);
        }
    }

    public void saveCourt(Court court) {
        courtRepository.save(court);
    }

    public void updateCourt(@NotNull Court court) {
        CourtId id = new CourtId(court.getBadmintonId(), court.getCourtId());
        Court existingCourt = courtRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException(court.getDetails() + " does not exist."));
        /*existingCourt.setPrice(court.getPrice());
        existingCourt.setDescription(court.getDescription());
        existingCourt.setCategory(court.getCategory());*/
        courtRepository.save(existingCourt);
    }

    public List<LocalTime[]> getAvailableTimeSlots(@RequestParam LocalDate date, @RequestParam Long courtId, @RequestParam Long badmintonId)
    {
        Court court = getCourtById(badmintonId, courtId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid court Id: " + courtId));
        List<LocalTime[]> allTimeSlots = timeSlotService.generateTimeSlots(court);
        return timeSlotService.filterAvailableTimeSlots(allTimeSlots, date, court);
    }


    public boolean getAvailableCourt(LocalDate date, Long courtId, Long badmintonId, LocalTime startTime, LocalTime endTime) {
        List<LocalTime[]> availableTimeSlots = getAvailableTimeSlots(date, courtId, badmintonId);

        if (availableTimeSlots.isEmpty()) {
            System.out.println("No available time slots on this day.");
            return false;
        }

        // Check if any slot is available if both start and end times are null
        if (startTime == null && endTime == null) {
            return true;
        }

        // Only calculate slotsNeeded if both startTime and endTime are provided
        long slotsNeeded = (startTime != null && endTime != null) ? (endTime.getHour() - startTime.getHour()) : 0;

        // Filter slots based on available times if startTime and endTime are provided
        List<LocalTime[]> filteredSlots = (startTime != null && endTime != null) ?
                availableTimeSlots.stream()
                        .filter(slot -> !slot[0].isBefore(startTime) && !slot[1].isAfter(endTime))
                        .collect(Collectors.toList()) : availableTimeSlots;

        // If only one of startTime or endTime is provided, consider any available slot as valid
        if (startTime == null || endTime == null) {
            return true;
        }

        // Check if there are enough consecutive slots available
        return hasConsecutiveSlots(filteredSlots, startTime, slotsNeeded);
    }

    // Method to check if there are enough consecutive slots
    private boolean hasConsecutiveSlots(List<LocalTime[]> slots, LocalTime startTime, long slotsNeeded) {
        int consecutiveCount = 0;

        for (LocalTime[] slot : slots) {
            if (slot[0].equals(startTime.plusHours(consecutiveCount))) {
                consecutiveCount++;
                if (consecutiveCount == slotsNeeded) {
                    return true;
                }
            } else {
                consecutiveCount = 0; // reset if any slot is not consecutive
            }
        }

        return false;
    }
}
