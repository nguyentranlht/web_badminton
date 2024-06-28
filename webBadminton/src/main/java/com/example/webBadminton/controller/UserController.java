package com.example.webBadminton.controller;

import com.example.webBadminton.model.User;
import com.example.webBadminton.service.UserService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Map;
import java.util.Optional;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String login() {
        return "account/login";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "account/register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            bindingResult.getFieldErrors().forEach(error -> model.addAttribute(error.getField()+"_error", error.getDefaultMessage()));
            return "account/register";
        }
        // Save the user
        userService.save(user);

        return "redirect:/login";
    }

    @GetMapping("/confirm")
    public String currentUser(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();

        if (oauth2User == null) {
            // Redirect to login or handle the case where the user is not authenticated
            return "redirect:/login";
        }

        Map<String, Object> attributes = oauth2User.getAttributes();
        User savedUser =  userService.getUserByEmail((String) attributes.get("email")).orElseThrow(
                () -> new IllegalArgumentException("Invalid user with email:" + attributes.get("email")));
        model.addAttribute("user", savedUser);
        return "account/confirm";
    }

    @PostMapping("/confirm")
    public String updateUser(@Valid @ModelAttribute("user") User user, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            bindingResult.getFieldErrors().forEach(error -> model.addAttribute(error.getField()+"_error", error.getDefaultMessage()));
            return "account/register";
        }
        // Save the user
        userService.updateUser(user);

        return "redirect:/";
    }

}
