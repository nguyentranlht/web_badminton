package com.example.webBadminton.controller;

import com.example.webBadminton.model.court.Badminton;
import com.example.webBadminton.model.court.Court;
import com.example.webBadminton.model.court.CourtId;
import com.example.webBadminton.service.BadmintonService;
import com.example.webBadminton.service.CourtService;
import com.example.webBadminton.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
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

    @GetMapping
    public String index() {
        return "admin/home/index";
    }

    @GetMapping("/badmintons")
    public String getAllBadmintonsAdmin(Model model){
        List<Badminton> badmintons = badmintonService.getAllBadmintons();

        model.addAttribute("badmintons", badmintons);
        model.addAttribute("location", locationService.getAll());
        return "/admin/badminton/list";
    }

    @GetMapping("/badmintons/add")
    public String showAddBadmintonForm(Model model){
        model.addAttribute("badminton", new Badminton());
        return "/admin/badminton/add";
    }


    @PostMapping("/badmintons/add")
    public String addBadminton(@Valid Badminton badminton, BindingResult result){
        if(result.hasErrors()){
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
    public String showUpdateForm(@PathVariable Long id, Model model){
        Badminton badminton  = badmintonService.getBadmintonById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid badminton Id:" + id));
        model.addAttribute("badminton", badminton);
        return "/admin/badminton/update";
    }
    @PostMapping("/badmintons/edit/{id}")
    public String updateBadminton(@PathVariable Long id, Model model, @Valid Badminton badminton, BindingResult result){
        if(result.hasErrors()) {
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
    public String searchByNameBadminton(@RequestParam("keyword") String keyword, Model model){
        List<Badminton> badmintons = badmintonService.getAllBadmintons()
                .stream()
                .filter(p-> p.getBadmintonName().toLowerCase().contains(keyword.toLowerCase())).collect(Collectors.toList());
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

    @GetMapping("/courts")
    public String showCourtListAdmin(Model model) {
        List<Court> courts = courtService.getAllCourts();
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
        CourtId id = new CourtId(badmintonId, courtId);
        Court court = courtService.getCourtById(id)
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
}
