package com.example.webBadminton.controller;

import com.example.webBadminton.model.User;
import com.example.webBadminton.model.court.Court;
import com.example.webBadminton.model.court.Badminton;
import com.example.webBadminton.model.BookingCourt;
import com.example.webBadminton.service.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Controller
@RequestMapping("/courts")
public class CourtController {
    // Đường dẫn thư mục để lưu trữ hình ảnh
    //private final String uploadDir = "src/main/resources/static/img/";
    @Autowired
    private CourtService courtService;
    @Autowired
    private BadmintonService badmintonService; // Đảm bảo bạn đã inject
    @Autowired
    private BookingService bookingService;
    @Autowired
    private TimeSlotService timeSlotService;

    @Autowired
    private UserService userService;

    // Đường dẫn thư mục để lưu trữ hình ảnh
    //private final String uploadDir = "src/main/resources/static/img/";
    // Display a list of all products
    @GetMapping("/{id}")
    public String showCourtList(@PathVariable Long id, Model model) {
        List<Court> courts = courtService.getAllCourtsByIdBadminton(id);
        Badminton badminton = badmintonService.getBadmintonById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid badminton Id:" + id));
        if (courts.isEmpty()) {
            return "/error";
        }
        model.addAttribute("badminton", badminton);
        model.addAttribute("courts", courts);
        return "/user/court/list";
    }

    //    @GetMapping("/add")
//    public String showAddForm(Model model) {
//        model.addAttribute("court", new court());
//        model.addAttribute("badmintons", badmintonService.getAllBadmintons());
//        return "/admin/court/add";
//    }
//
//    @PostMapping("/add")
//    public String addBadminton(@Valid Badminton badminton, BindingResult result){
//        if(result.hasErrors()){
//            return "/admin/court/add";
//        }
//        badmintonService.addBadminton(badminton);
//        return "redirect:/admin/courts";
//    }
    @GetMapping("/bookCourt/{badmintonId}/{courtId}")
    public String getBookCourtDetails(@PathVariable Long badmintonId, @PathVariable Long courtId, Model model) {
        Court court = courtService.getCourtById(badmintonId, courtId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid court Id: " + courtId));
        BookingCourt bookingCourt = new BookingCourt();  // Giả sử Booking có thuộc tính phoneNumber
        bookingCourt.setCourt(court);
//        model.addAttribute("timeSlots", timeSlotService.generateTimeSlots(court));
        model.addAttribute("bookingCourt", bookingCourt);
        return "user/court/booking";  // Trả về trang Thymeleaf với form đã được điền
    }

    @PostMapping("/bookCourt")
    public String bookingCourt(@ModelAttribute("bookingCourt") BookingCourt bookingCourt, @RequestParam String timeSlot, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "/user/court/booking";
        }
        User user =  userService.getUserByCurentId().orElseThrow(
                () -> new IllegalArgumentException("Invalid user with id:" + userService.getUserByCurentId()));
        String[] times = timeSlot.split(" to ");
        LocalTime startTime = LocalTime.parse(times[0], DateTimeFormatter.ofPattern("HH:mm:ss"));
        LocalTime endTime = LocalTime.parse(times[1], DateTimeFormatter.ofPattern("HH:mm:ss"));
        bookingCourt.setStatus("false");
        bookingCourt.setStartTime(startTime);
        bookingCourt.setEndTime(endTime);
        bookingCourt.setUser(user);
        bookingService.addBooking(bookingCourt);
        redirectAttributes.addAttribute("bookingCourt", bookingCourt);
        return "redirect:/payment/create_payment";
    }

    @GetMapping("/availableTimeSlots")
    @ResponseBody
    public List<LocalTime[]> getAvailableTimeSlots(@RequestParam LocalDate date, @RequestParam Long courtId, @RequestParam Long badmintonId) {
        return courtService.getAvailableTimeSlots(date, courtId, badmintonId);
    }

}
