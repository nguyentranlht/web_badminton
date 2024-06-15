package com.example.webBadminton.controller;

import com.example.webBadminton.model.Badminton;
import com.example.webBadminton.service.BadmintonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
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
    public String showAddForm(Model model){
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
}
