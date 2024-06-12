package com.example.webBadminton.controller;
//import com.example.webBadminton.model.Badminton;
import com.example.webBadminton.service.BadmintonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/courts")
public class BadmintonController {
    @GetMapping
    public String home() {
        return "court/list";
    }

//    @Autowired
//    private BadmintonService badmintonService;
//
//    // Đường dẫn thư mục để lưu trữ hình ảnh
//    private final String uploadDir = "src/main/resources/static/img/";

}
