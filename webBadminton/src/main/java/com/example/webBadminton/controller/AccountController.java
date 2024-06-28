package com.example.webBadminton.controller;

import com.example.webBadminton.model.CustomUserDetail;
import com.example.webBadminton.model.User;
import com.example.webBadminton.model.court.Badminton;
import com.example.webBadminton.service.BadmintonService;
import com.example.webBadminton.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AccountController {

    @Autowired
    private UserService userService;

    @Autowired
    private BadmintonService badmintonService;

    @GetMapping("/manage")
    public String getAllAccountsUser(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetail customUserDetail = (CustomUserDetail) authentication.getPrincipal();
        model.addAttribute("username", customUserDetail.getUsername());
        model.addAttribute("roles", customUserDetail.getAuthorities().stream()
                .map(authority -> authority.getAuthority())
                .collect(Collectors.joining(", ")));

        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "admin/manage/account";
    }

    @GetMapping("/manage/edit/{id}")
    public String showEditAccountUser(@PathVariable("id") Long id, Model model) {
        User user = userService.getUserById(id);
        model.addAttribute("user", user);
        model.addAttribute("roles", userService.getAllRoles());
        model.addAttribute("userRoles", user.getRole().getId());
        return "admin/manage/edit";
    }

    @PostMapping("/manage/edit/{id}")
    public String updateAccountUser(@PathVariable("id") Long id, @RequestParam Long roleId) {
        userService.updateUserRole(id, roleId);
        return "redirect:/admin/manage";
    }

    @GetMapping("/manage/delete/{id}")
    public String deleteAccountUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return "redirect:/admin/manage";
    }
}
