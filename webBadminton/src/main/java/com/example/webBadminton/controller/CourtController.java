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

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/admin/courts")
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
        List<Court> courts = courtService.getAllCourts();
        model.addAttribute("courts", courts);
        return "/admin/court/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("court", new Court());
        model.addAttribute("badmintons", badmintonService.getAllBadmintons());
        return "/admin/court/add";
    }

    @PostMapping("/add")
    public String addBadminton(@Valid Badminton badminton, BindingResult result){
        if(result.hasErrors()){
            return "/admin/court/add";
        }
        badmintonService.addBadminton(badminton);
        return "redirect:/admin/courts";
    }
}
