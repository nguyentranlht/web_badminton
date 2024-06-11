package com.example.webBadminton.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/badmintons")
public class BadmintonController {
    @GetMapping
    public String home() {
        return "badminton/list";
    }
}
