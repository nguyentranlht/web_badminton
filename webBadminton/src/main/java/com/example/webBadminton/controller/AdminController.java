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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.List;

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
    public String adminhome() {
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

    @GetMapping("/badmintons/delete/{id}")
    public String deleteBadminton(@PathVariable("id") Long id) {
        badmintonService.deleteBadminton(id);
        return "redirect:/admin/badmintons";
    }

    @GetMapping("/courts/add")
    public String showAddCourtForm(Model model) {
        model.addAttribute("court", new Court());
        model.addAttribute("badmintons", badmintonService.getAllBadmintons());
        return "/admin/court/add";
    }


    @GetMapping("/courts/delete/{badmintonId}/{courtId}")
    public String deleteCourt(@PathVariable("badmintonId") Long badmintonId, @PathVariable("courtId") Long courtId) {
        CourtId courtIdObject = new CourtId(badmintonId, courtId);
        courtService.deleteCourt(courtIdObject);
        return "redirect:/admin/courts";
    }
}
