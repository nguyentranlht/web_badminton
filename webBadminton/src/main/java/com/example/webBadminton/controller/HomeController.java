package com.example.webBadminton.controller;

import com.example.webBadminton.model.Badminton;
import com.example.webBadminton.service.BadmintonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/")
public class HomeController {
    @Autowired
    private BadmintonService badmintonService;
    @GetMapping
    public String showAllBadmintons(Model model) {
        List<Badminton> badmintons = badmintonService.getAllBadmintons();
        model.addAttribute("badmintons", badmintons);
        return "home/index";
    }
}

