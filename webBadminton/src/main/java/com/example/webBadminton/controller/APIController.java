package com.example.webBadminton.controller;

import com.example.webBadminton.model.BookingCourt;
import com.example.webBadminton.model.booking.Booking;
import com.example.webBadminton.model.court.Badminton;
import com.example.webBadminton.model.court.Court;
import com.example.webBadminton.service.BadmintonService;
import com.example.webBadminton.service.BookingService;
import com.example.webBadminton.service.CourtService;
import com.example.webBadminton.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/api")
public class APIController {
    @Autowired
    private UserService userService;
    @Autowired
    private BadmintonService badmintonService;
    @Autowired
    private BookingService bookingService;
    @Autowired
    private CourtService courtService;

    @GetMapping("/bookings")
    public ResponseEntity<?> getCourtsAndBookings() {
        Long currentUserId = userService.getCurrentUserId();
        List<Badminton> badmintons = badmintonService.getAllBadmintonByUser(currentUserId);

        List<Map<String, Object>> data = new ArrayList<>();

        badmintons.forEach(badminton -> {
            List<Court> courts = courtService.getAllCourtsByIdBadminton(badminton.getId());
            List<BookingCourt> bookings = bookingService.getBookingsByBadminton(Collections.singletonList(badminton));

            courts.forEach(court -> {
                Map<String, Object> courtData = new HashMap<>();
                courtData.put("courtId", court.getCourtId());
                courtData.put("courtDetails", court.getDetails());
                courtData.put("badmintonId", badminton.getId());
                courtData.put("badmintonName", badminton.getBadmintonName());

                // Find bookings for this court
                List<Map<String, Object>> courtBookings = bookings.stream()
                        .filter(booking -> booking.getCourt().getCourtId().equals(court.getCourtId()))
                        .map(booking -> {
                            Map<String, Object> bookingData = new HashMap<>();
                            bookingData.put("id", booking.getId());
                            bookingData.put("bookingDate", booking.getBookingDate().toString());
                            bookingData.put("startTime", booking.getStartTime().toString());
                            bookingData.put("endTime", booking.getEndTime().toString());
                            bookingData.put("userName", userService.getUserById(booking.getUser().getId()).getName());
                            return bookingData;
                        })
                        .collect(Collectors.toList());

                courtData.put("bookings", courtBookings);
                data.add(courtData);
            });
        });

        return ResponseEntity.ok(data);
    }

    @PostMapping("/addCourt")
    public String addCourt(@RequestParam("badmintonId") Long badmintonId, @RequestParam("description") String description, RedirectAttributes redirectAttributes) {
        try {
            Court court = new Court();
            Badminton badminton = badmintonService.getBadmintonById(badmintonId).orElseThrow(() -> new IllegalArgumentException("Invalid badminton Id:" + badmintonId));
            badminton.setCourtQuantity(badminton.getCourtQuantity()+1);
            badmintonService.updateBadminton(badminton);
            court.setBadmintonId(badmintonId);
            court.setDetails(description);
            court.setBadminton(badmintonService.getBadmintonById(badmintonId).orElseThrow());
            court.setCourtId(courtService.getLatestCourt().getCourtId() + 1);
            // Assuming there's a method in CourtService to save a court
            courtService.saveCourt(court);
            redirectAttributes.addFlashAttribute("message", "Court added successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error adding court: " + e.getMessage());
        }
        return "redirect:/owner/courts";
    }
}
