package com.example.webBadminton.controller;

import com.example.webBadminton.model.court.Badminton;
import com.example.webBadminton.modelView.SearchCriteria;
import com.example.webBadminton.service.BadmintonService;
import com.example.webBadminton.service.LocationService;
import com.example.webBadminton.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    private SearchService searchService;

    @GetMapping
    public String showAllBadmintons(Model model) {
        List<Badminton> badmintons = badmintonService.getAllBadmintons();
        model.addAttribute("badmintons", badmintons);
        model.addAttribute("location", locationService.getAll());
        return "home/index";
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

