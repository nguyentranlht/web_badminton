package com.example.webBadminton.controller;

import com.example.webBadminton.model.court.Court;
import com.example.webBadminton.service.BadmintonService;

import com.example.webBadminton.service.CourtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @GetMapping("/{id}")
    public String showCourtList(@PathVariable Long id, Model model) {
        List<Court> courts = courtService.getAllCourtsByIdBadminton(id);
        if (courts.isEmpty()) {
            return "/error";
        }
        model.addAttribute("courts", courts);
        return "/user/court/list";
    }

//    @GetMapping("/add")
//    public String showAddForm(Model model) {
//        model.addAttribute("court", new court());
//        model.addAttribute("badmintons", badmintonService.getAllBadmintons());
//        return "/admin/court/add";
//    }
//
//    @PostMapping("/add")
//    public String addBadminton(@Valid Badminton badminton, BindingResult result){
//        if(result.hasErrors()){
//            return "/admin/court/add";
//        }
//        badmintonService.addBadminton(badminton);
//        return "redirect:/admin/courts";
//    }
}
