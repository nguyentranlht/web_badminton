package com.example.webBadminton.controller;

import com.example.webBadminton.model.User;
import com.example.webBadminton.model.court.Badminton;
import com.example.webBadminton.model.court.Court;
import com.example.webBadminton.modelView.SearchCriteria;
import com.example.webBadminton.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {
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

    @GetMapping
    public String index() {

        return "admin/home/index";
    }

    @GetMapping("/owners")
    public String showOwners(Model model) {
        List<User> owners = userService.findOwners();
        model.addAttribute("owners", owners);
        return "/admin/account/list"; // Name of the Thymeleaf template
    }

    @GetMapping("/owners/badmintons")
    public String listOwnerCourts(@RequestParam("ownerId") Long ownerId, Model model) {
        User owner = userService.getUserById(ownerId).orElseThrow(
                () -> new IllegalArgumentException("Invalid user with id:" + userService.getUserById(ownerId))); // Assume this method exists
        List<Badminton> badmintons = owner.getBadmintons(); // Assuming Owner has a list of Courts
        model.addAttribute("owner", owner);
        model.addAttribute("badmintons", badmintons);
        return "/admin/owner/badminton/list"; // Thymeleaf template
    }


    @GetMapping("/badmintons")
    public String getAllBadmintonsAdmin(Model model) {
        List<Badminton> badmintons = badmintonService.getAllBadmintons();

        model.addAttribute("badmintons", badmintons);
        model.addAttribute("location", locationService.getAll());
        return "/admin/badminton/list";
    }

    @GetMapping("/badmintons/add")
    public String showAddBadmintonForm(Model model) {
        model.addAttribute("badminton", new Badminton());
        return "/admin/badminton/add";
    }


    @PostMapping("/badmintons/add")
    public String addBadminton(@Valid Badminton badminton, BindingResult result) {
        if (result.hasErrors()) {
            return "/admin/badminton/add";
        }
        badminton.getLocation().setProvinceName
                (locationService.getProvinceName(badminton.getLocation().getProvinceId()));
        badminton.getLocation().setDistrictName
                (locationService.getDistrictName(badminton.getLocation().getDistrictId()));
        badminton.getLocation().setWardName
                (locationService.getWardName(badminton.getLocation().getWardId()));
        badmintonService.addBadminton(badminton);
        courtService.addCourt(badminton);
        return "redirect:/admin/badmintons";
    }

    @GetMapping("/badmintons/edit/{id}")
    public String showUpdateForm(@PathVariable Long id, Model model) {
        Badminton badminton = badmintonService.getBadmintonById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid badminton Id:" + id));
        model.addAttribute("badminton", badminton);
        return "/admin/badminton/update";
    }

    @PostMapping("/badmintons/edit/{id}")
    public String updateBadminton(@PathVariable Long id, Model model, @Valid Badminton badminton, BindingResult result) {
        if (result.hasErrors()) {
            badminton.setId(id);
            return "/admin/badminton/update";
        }
        badmintonService.updateBadminton(badminton);

        model.addAttribute("badmintons", badmintonService.getAllBadmintons()); //Dòng này để làm gì?
        return "redirect:/admin/badmintons";
    }

    @GetMapping("/badmintons/delete/{id}")
    public String deleteBadminton(@PathVariable("id") Long id) {
        badmintonService.deleteBadminton(id);
        return "redirect:/admin/badmintons";
    }

    @GetMapping("/badmintons/search")
    public String searchByNameBadminton(@RequestParam("keyword") String keyword, Model model) {
        List<Badminton> badmintons = badmintonService.getAllBadmintons()
                .stream()
                .filter(p -> p.getBadmintonName().toLowerCase().contains(keyword.toLowerCase())).collect(Collectors.toList());
        model.addAttribute("badmintons", badmintons);
        model.addAttribute("keyword", keyword);
        return "/admin/badminton/list";
    }

    @GetMapping("/courts/add")
    public String showAddCourtForm(Model model) {
        model.addAttribute("court", new Court());
        model.addAttribute("badmintons", badmintonService.getAllBadmintons());
        return "/admin/court/add";
    }

    @GetMapping("/courts/{badmintonId}")
    public String showCourtListAdmin(@PathVariable Long badmintonId, Model model) {
        List<Court> courts = courtService.getAllCourtsByIdBadminton(badmintonId);
        model.addAttribute("courts", courts);
        return "/admin/court/list";
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
        model.addAttribute("badmintons", badmintonService.getAllBadmintons());
        return "/admin/court/update";
    }

    @PostMapping("/courts/edit/{id}")
    public String updateCourt(@PathVariable Long badmintonId, @PathVariable Long courtId, @Valid Court court,
                              BindingResult result, Model model) {
        if (result.hasErrors()) {
            court.setCourtId(courtId);
            court.setBadmintonId(badmintonId);
            return "/admin/court/update";
        }
        courtService.updateCourt(court);
        model.addAttribute("courts", courtService.getAllCourts());
        return "redirect:/admin/courts";
    }

    @GetMapping("/courts/delete/{badmintonId}/{courtId}")
    public String deleteCourt(@PathVariable("badmintonId") Long badmintonId, @PathVariable("courtId") Long courtId) {
        courtService.deleteCourt(badmintonId, courtId);
        badmintonService.updateQuantity(badmintonId);
        return "redirect:/admin/courts";
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
