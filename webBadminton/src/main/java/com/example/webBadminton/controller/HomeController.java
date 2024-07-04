package com.example.webBadminton.controller;

import com.example.webBadminton.model.BookingCourt;
import com.example.webBadminton.model.court.Badminton;
import com.example.webBadminton.service.BadmintonService;
import com.example.webBadminton.service.BookingService;
import com.example.webBadminton.service.LocationService;
import com.example.webBadminton.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/")
public class HomeController {
    @Autowired
    private BadmintonService badmintonService;

    @Autowired
    private LocationService locationService;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private UserService userService;

    @GetMapping
    public String showAllBadmintons(Model model) {
        List<Badminton> badmintons = badmintonService.getAllBadmintons();
        model.addAttribute("badmintons", badmintons);
        model.addAttribute("location", locationService.getAll());
        return "home/index";
    }

    @GetMapping("/history")
    public String showAllHistoryUser(Model model) {
        Long currentUserId = userService.getCurrentUserId();
        List<BookingCourt> bookingCourtList = bookingService.getAllBookingByUser(currentUserId);
        model.addAttribute("bookings", bookingCourtList);
        return "user/history/list";
    }
}

