package com.example.webBadminton.controller;
import com.example.webBadminton.service.BadmintonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/badmintons")
public class BadmintonController {
    @Autowired
    private BadmintonService badmintonService;

    @GetMapping()
    public String getAllBadmintons(Model model){
        List<Badminton> badmintons = badmintonService.getAllBadmintons();
        model.addAttribute("badmintons", badmintons);
        return "/user/badminton/list";
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
