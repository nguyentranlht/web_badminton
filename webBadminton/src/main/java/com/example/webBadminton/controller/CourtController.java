package com.example.webBadminton.controller;

import com.example.webBadminton.model.Badminton;
import com.example.webBadminton.model.BookingCourt;
import com.example.webBadminton.model.Court;
import com.example.webBadminton.service.BadmintonService;

import com.example.webBadminton.service.BookingService;
import com.example.webBadminton.service.CourtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/courts")
public class CourtController {
    @Autowired
    private CourtService courtService;

    @Autowired
    private BadmintonService badmintonService; // Đảm bảo bạn đã inject

    @Autowired
    private BookingService bookingService;

    // Đường dẫn thư mục để lưu trữ hình ảnh
    private final String uploadDir = "src/main/resources/static/img/";
    // Display a list of all products
    @GetMapping("/{id}")
    public String showCourtList(@PathVariable Long id, Model model) {
        List<Court> courts = courtService.getAllCourtsByIdBadminton(id);
        Badminton badminton  = badmintonService.getBadmintonById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid badminton Id:" + id));
        if (courts.isEmpty()) {
            return "/error";
        }
        model.addAttribute("badminton", badminton);
        model.addAttribute("courts", courts);
        return "/user/court/list";
    }

    @GetMapping("/bookCourt/{courtId}")
    public String getBookCourtDetails(@PathVariable Long courtId, Model model) {
        Court court = courtService.getCourtById(courtId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid court Id: " + courtId));
        BookingCourt bookingCourt = new BookingCourt();  // Giả sử Booking có thuộc tính phoneNumber
        bookingCourt.setCourt(court);
        model.addAttribute("bookingCourt", bookingCourt);
        return "user/court/booking";  // Trả về trang Thymeleaf với form đã được điền
    }

    @PostMapping("/bookCourt")
    public String bookingCourt(@ModelAttribute("bookingCourt") BookingCourt bookingCourt, BindingResult result){
        if(result.hasErrors()){
            return "/user/home";
        }
        bookingService.addBooking(bookingCourt);
        return "redirect:/";
    }
}
