package com.example.webBadminton.service;

import com.example.webBadminton.model.court.Badminton;
import com.example.webBadminton.model.court.Court;
import com.example.webBadminton.model.court.CourtId;
import com.example.webBadminton.repository.ICourtRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

    public List<LocalTime[]> getAvailableTimeSlots(@RequestParam LocalDate date, @RequestParam Long courtId, @RequestParam Long badmintonId) {
        Court court = getCourtById(badmintonId, courtId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid court Id: " + courtId));
        List<LocalTime[]> allTimeSlots = timeSlotService.generateTimeSlots(court);
        return timeSlotService.filterAvailableTimeSlots(allTimeSlots, date, court);
    }


    public boolean getAvailableCourt(LocalDate date, Long courtId, Long badmintonId, LocalTime startTime, LocalTime endTime) {
        List<LocalTime[]> availableTimeSlots = getAvailableTimeSlots(date, courtId, badmintonId);
        availableTimeSlots.forEach(p -> {
            System.out.println("BadId: " + badmintonId + ", CourtId: " + courtId + ", Date: " + date + ", Start: " + p[0] + ", End: " + p[1]);
        });

        // Define the sentinel value
        LocalTime sentinelTime = LocalTime.of(3, 0);

        if (availableTimeSlots.isEmpty()) {
            System.out.println("No available time slots on this day.");
            return false;
        }

        // Check if both times are the sentinel value, assuming no times provided
        if (isSentinel(startTime, sentinelTime) && isSentinel(endTime, sentinelTime)) {
            return true;  // Assume availability if no specific time is required
        }

        // Process only if both startTime and endTime are not sentinel values
        if (!isSentinel(startTime, sentinelTime) && !isSentinel(endTime, sentinelTime)) {
            long slotsNeeded = (endTime.getHour() - startTime.getHour());

            List<LocalTime[]> filteredSlots = availableTimeSlots.stream()
                    .filter(slot -> !slot[0].isBefore(startTime) && !slot[1].isAfter(endTime))
                    .collect(Collectors.toList());

            return hasConsecutiveSlots(filteredSlots, startTime, slotsNeeded);
        }

        // If only one of the times is the sentinel, consider any available slot as valid
        return true;
    }

    // Helper method to check if the time is the sentinel value
    private boolean isSentinel(LocalTime time, LocalTime sentinel) {
        return time.equals(sentinel);
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
