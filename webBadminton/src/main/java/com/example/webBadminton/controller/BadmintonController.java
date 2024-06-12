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
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/badmintons")
public class BadmintonController {
    private final BadmintonService badmintonService;

    @GetMapping("/")
    public String getAllBadmintons(Model model){
        List<Badminton> badmintons = badmintonService.getAllBadmintons();
        model.addAttribute("badmintons", badmintons);
        return "/badmintons/list";
    }


    @GetMapping("/add")
    public String showAddForm(Model model){
        model.addAttribute("badminton", new Badminton());
        return "/badminton/add";
    }

    @PostMapping("/add")
    public String addBadminton(@Valid Badminton badminton, BindingResult result){
        if(result.hasErrors()){
            return "/badminton/add";
        }
        badmintonService.addBadminton(badminton);
        return "redirect:/badmintons";
    }

//    @Autowired
//    private BadmintonService badmintonService;
//
//    // Đường dẫn thư mục để lưu trữ hình ảnh
//    private final String uploadDir = "src/main/resources/static/img/";

}
