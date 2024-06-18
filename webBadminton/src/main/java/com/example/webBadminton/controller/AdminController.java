package com.example.webBadminton.controller;

import com.example.webBadminton.model.Badminton;
import com.example.webBadminton.model.Court;
import com.example.webBadminton.service.BadmintonService;
import com.example.webBadminton.service.CourtService;
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

    @GetMapping
    public String adminhome() {
        return "admin/home/index";
    }

    @GetMapping("/badmintons")
    public String getAllBadmintonsAdmin(Model model){
        List<Badminton> badmintons = badmintonService.getAllBadmintons();
        model.addAttribute("badmintons", badmintons);
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
        badmintonService.addBadminton(badminton);
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
        model.addAttribute("badmintons", badmintonService.getAllBadmintons());
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

    @PostMapping("/courts/add")
    public String addCourt(@Valid Court court, BindingResult result){
        if(result.hasErrors()){
            return "/admin/court/add";
        }
        courtService.addCourt(court);
        return "redirect:/admin/courts";
    }

    @GetMapping("/courts/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Court court = courtService.getCourtById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid court Id:" + id));
        model.addAttribute("court", court);
        model.addAttribute("badmintons", badmintonService.getAllBadmintons());
        return "/admin/court/update";
    }

    @PostMapping("/courts/edit/{id}")
    public String updateCourt(@PathVariable Long id, @Valid Court court,
                                BindingResult result, Model model) {
        if (result.hasErrors()) {
            court.setId(id);
            return "/admin/court/update";
        }
        courtService.updateCourt(court);
        model.addAttribute("courts", courtService.getAllCourts());
        return "redirect:/admin/courts";
    }

    @GetMapping("/courts/delete/{id}")
    public String deleteCourt(@PathVariable("id") Long id) {
        courtService.deleteCourt(id);
        return "redirect:/admin/courts";
    }

    @GetMapping("/courts/search")
    public String searchByNameCourt(@RequestParam("keyword") String keyword, Model model){
        List<Court> courts = courtService.getAllCourts()
                .stream()
                .filter(p-> p.getCourtName().toLowerCase().contains(keyword.toLowerCase())).collect(Collectors.toList());
        model.addAttribute("courts", courts);
        model.addAttribute("keyword", keyword);
        return "/admin/court/list";
    }
}
