package com.example.webBadminton.controller;

import com.example.webBadminton.model.BookingCourt;
import com.example.webBadminton.model.User;
import com.example.webBadminton.model.court.Badminton;
import com.example.webBadminton.model.court.Court;
import com.example.webBadminton.modelView.SearchCriteria;
import com.example.webBadminton.service.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/owner")
public class OwnerController {
    @Autowired
    private CourtService courtService;
    @Autowired
    private BadmintonService badmintonService;
    @Autowired
    private LocationService locationService;
    @Autowired
    private SearchService searchService;
    @Autowired
    private UserService userService;
    @Autowired
    private BookingService bookingService;

//    @GetMapping
//    public String home(Model model) {
//        List<Badminton> badmintonList = badmintonService.getAllBadmintonByUser(userService.getCurrentUserId());
//        var booking = bookingService.getBookingsByBadminton(badmintonList);
//        booking.forEach(p -> System.out.println(p.getCourt().getCourtId()));
//        model.addAttribute("bookings", booking);
//        model.addAttribute("court", courtService.getAllCourtByBadminton(badmintonList));
//        return "owner/home/index";
//    }

    @GetMapping
    public String home(Model model) throws JsonProcessingException {
        Long currentUserId = userService.getCurrentUserId();
        List<Badminton> badmintonList = badmintonService.getAllBadmintonByUser(currentUserId);
        List<BookingCourt> bookingList = bookingService.getBookingsByBadminton(badmintonList);

        // Prepare data for the timeline
        List<Map<String, Object>> bookings = bookingList.stream().map(booking -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", booking.getId());
            map.put("courtId", booking.getCourt().getCourtId());
            map.put("details", booking.getCourt().getDetails());
            map.put("bookingDate", booking.getBookingDate().toString());
            map.put("startTime", booking.getStartTime().toString());
            map.put("endTime", booking.getEndTime().toString());
            return map;
        }).collect(Collectors.toList());

        model.addAttribute("bookings", bookings);
        model.addAttribute("courts", courtService.getAllCourtByBadminton(badmintonList));
        return "owner/home/index";
    }


    @GetMapping("/badmintons")
    public String getAllBadmintonsAdmin(Model model){
        Long userId = userService.getCurrentUserId();
        List<Badminton> badmintons = badmintonService.getAllBadmintonByUser(userId);

        model.addAttribute("badmintons", badmintons);
        model.addAttribute("location", locationService.getAll());
        return "/owner/badminton/list";
    }

    @GetMapping("/badmintons/add")
    public String showAddBadmintonForm(Model model){
        model.addAttribute("badminton", new Badminton());
        return "/owner/badminton/add";
    }


    @PostMapping("/badmintons/add")
    public String addBadminton(@Valid Badminton badminton, BindingResult result){
        if(result.hasErrors()){
            return "/owner/badminton/add";
        }
        badminton.getLocation().setProvinceName
                (locationService.getProvinceName(badminton.getLocation().getProvinceId()));
        badminton.getLocation().setDistrictName
                (locationService.getDistrictName(badminton.getLocation().getDistrictId()));
        badminton.getLocation().setWardName
                (locationService.getWardName(badminton.getLocation().getWardId()));
        User loggedUser = userService.getUserById(userService.getCurrentUserId());
        badminton.setUser(loggedUser);
        badmintonService.addBadminton(badminton);
        courtService.addCourt(badminton);
        return "redirect:/owner/badmintons";
    }

    @GetMapping("/badmintons/edit/{id}")
    public String showUpdateForm(@PathVariable Long id, Model model){
        Badminton badminton  = badmintonService.getBadmintonById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid badminton Id:" + id));
        model.addAttribute("badminton", badminton);
        return "/owner/badminton/update";
    }
    @PostMapping("/badmintons/edit/{id}")
    public String updateBadminton(@PathVariable Long id, Model model, @Valid Badminton badminton, BindingResult result){
        if(result.hasErrors()) {
            badminton.setId(id);
            return "/owner/badminton/update";
        }
        badmintonService.updateBadminton(badminton);

        model.addAttribute("badmintons", badmintonService.getAllBadmintons()); //Dòng này để làm gì?
        return "redirect:/owner/badmintons";
    }

    @GetMapping("/badmintons/delete/{id}")
    public String deleteBadminton(@PathVariable("id") Long id) {
        badmintonService.deleteBadminton(id);
        return "redirect:/owner/badmintons";
    }

    @GetMapping("/badmintons/search")
    public String searchByNameBadminton(@RequestParam("keyword") String keyword, Model model){
        List<Badminton> badmintons = badmintonService.getAllBadmintons()
                .stream()
                .filter(p-> p.getBadmintonName().toLowerCase().contains(keyword.toLowerCase())).collect(Collectors.toList());
        model.addAttribute("badmintons", badmintons);
        model.addAttribute("keyword", keyword);
        return "/admin/badminton/list";
    }

    @GetMapping("/courts")
    public String showCourtListAdmin(Model model) {
        List<Badminton> badmintonList = badmintonService.getAllBadmintonByUser(userService.getCurrentUserId());
        model.addAttribute("courts", courtService.getAllCourtByBadminton(badmintonList));
        model.addAttribute("badmintons", badmintonList);
        return "/owner/court/list";
    }

//    @PostMapping("/courts/add")
//    public String addCourt(@Valid Court court, BindingResult result){
//        if(result.hasErrors()){
//            return "/admin/court/add";
//        }
//        courtService.addCourt(court);
//        return "redirect:/admin/courts";
//    }

    @GetMapping("/courts/edit/{badmintonId}/{courtId}")
    public String showEditForm(@PathVariable Long badmintonId, @PathVariable Long courtId, Model model) {
        Court court = courtService.getCourtById(badmintonId, courtId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid court Ids: BadmintonId=" + badmintonId + ", CourtId=" + courtId));
        model.addAttribute("court", court);
        return "/owner/court/update";
    }

    @PostMapping("/courts/edit/{badmintonId}/{courtId}")
    public String updateCourt(@Valid Court court, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "/owner/court/update";
        }
        courtService.updateCourt(court);
        return "redirect:/owner/courts";
    }

    @GetMapping("/courts/delete/{badmintonId}/{courtId}")
    public String deleteCourt(@PathVariable("badmintonId") Long badmintonId, @PathVariable("courtId") Long courtId) {
        courtService.deleteCourt(badmintonId, courtId);
        badmintonService.updateQuantity(badmintonId);
        return "redirect:/owner/courts";
    }

    @PostMapping("/search")
    public ResponseEntity<?> performSearch(@RequestBody SearchCriteria criteria) {
        // Process the search criteria
        List<Badminton> results = searchService.search(criteria);

        // Convert results to HTML or JSON, depending on your needs
        String resultsHtml = generateResultsHtml(results);
        return ResponseEntity.ok(resultsHtml);
    }

    public String generateResultsHtml(List<Badminton> results) {
        if (results == null || results.isEmpty()) {
            return "<p>No results found.</p>";
        }

        StringBuilder htmlBuilder = new StringBuilder();
        htmlBuilder.append("<table style='width:100%; border: 1px solid black;'>");
        htmlBuilder.append("<tr>")
                .append("<th>Name</th>")
                .append("<th>Rental Price</th>")
                .append("<th>Rating</th>")
                .append("<th>Court Quantity</th>")
                .append("<th>Opening Time</th>")
                .append("<th>Closing Time</th>")
                .append("<th>Image</th>")
                .append("</tr>");

        for (Badminton badminton : results) {
            htmlBuilder.append("<tr>")
                    .append("<td>").append(badminton.getBadmintonName()).append("</td>")
                    .append("<td>").append(badminton.getRentalPrice()).append("</td>")
                    .append("<td>").append(badminton.getRating()).append("</td>")
                    .append("<td>").append(badminton.getCourtQuantity()).append("</td>")
                    .append("<td>").append(badminton.getOpeningTime()).append("</td>")
                    .append("<td>").append(badminton.getClosingTime()).append("</td>")
                    .append("<td><img src='").append(badminton.getImageUrl()).append("' alt='Badminton Image' style='height: 50px;'></td>")
                    .append("</tr>");
        }

        htmlBuilder.append("</table>");
        return htmlBuilder.toString();
    }
}
