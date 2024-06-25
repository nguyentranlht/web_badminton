package com.example.webBadminton.controller;
import com.example.webBadminton.model.court.Badminton;
import com.example.webBadminton.modelView.SearchCriteria;
import com.example.webBadminton.service.BadmintonService;
import com.example.webBadminton.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/badmintons")
public class BadmintonController {
    @Autowired
    private BadmintonService badmintonService;

    @Autowired
    private SearchService searchService;

    @GetMapping()
    public String getAllBadmintons(Model model){
        List<Badminton> badmintons = badmintonService.getAllBadmintons();
        model.addAttribute("badmintons", badmintons);
        return "/user/badminton/list";
    }

    @PostMapping("/search")
    public ResponseEntity<?> performSearch(@RequestBody SearchCriteria criteria) {
        // Process the search criteria
        List<Badminton> results = searchService.search(criteria);

        return ResponseEntity.ok(results);
    }


//    @GetMapping("/add")
//    public String showAddForm(Model model){
//        model.addAttribute("badminton", new Badminton());
//        return "/badminton/add";
//    }
//
//    @PostMapping("/add")
//    public String addBadminton(@Valid Badminton badminton, BindingResult result){
//        if(result.hasErrors()){
//            return "/badminton/add";
//        }
//        badmintonService.addBadminton(badminton);
//        return "redirect:/badmintons";
//    }

}
