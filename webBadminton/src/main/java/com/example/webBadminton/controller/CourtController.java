package com.example.webBadminton.controller;

import com.example.webBadminton.model.Badminton;
import com.example.webBadminton.model.Court;
import com.example.webBadminton.service.BadmintonService;

import com.example.webBadminton.service.CourtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/courts")
public class CourtController {
    @Autowired
    private CourtService courtService;

    @Autowired
    private BadmintonService badmintonService; // Đảm bảo bạn đã inject

    // Đường dẫn thư mục để lưu trữ hình ảnh
    private final String uploadDir = "src/main/resources/static/img/";

    // Display a list of all products
    @GetMapping()
    public String showCourtList(Model model) {
        List<Court> courts = courtService.getAllCourts()
                .stream()
                //.sorted(Comparator.comparingDouble(Court::getPrice))
                .collect(Collectors.toList());
        model.addAttribute("courts", courts);
        return "/courts/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("court", new Court());
        model.addAttribute("badmintons", badmintonService.getAllBadmintons());
        return "/courts/add";
    }

    @PostMapping("/add")
    public String addCourt(@Valid Court court, BindingResult result, @RequestParam("image") MultipartFile image) {
        if (result.hasErrors()) {
            return "/courts/add";
        }
        try {
            courtService.addCourt(court, image);
        } catch (Exception e) {
            // Handle image upload exception
            e.printStackTrace();
            return "/courts/add";
        }
        return "redirect:/courts";
    }
}
