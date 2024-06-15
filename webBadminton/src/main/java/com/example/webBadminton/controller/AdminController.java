package com.example.webBadminton.controller;

import com.example.webBadminton.model.Badminton;
import com.example.webBadminton.model.Court;
import com.example.webBadminton.service.BadmintonService;
import com.example.webBadminton.service.CourtService;
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

    @PostMapping("/courts/add")
    public String addCourt(@Valid Court court, BindingResult result){
        if(result.hasErrors()){
            return "/admin/court/add";
        }
        courtService.addCourt(court);
        return "redirect:/admin/courts";
    }

    @GetMapping("/courts/delete/{id}")
    public String deleteCourt(@PathVariable("id") Long id) {
        courtService.deleteCourt(id);
        return "redirect:/admin/courts";
    }
}
