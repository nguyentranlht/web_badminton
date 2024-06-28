package com.example.webBadminton.controller;

import com.example.webBadminton.model.User;
import com.example.webBadminton.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.Map;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private UserDetailsService userDetailsService;

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
            bindingResult.getFieldErrors().forEach(error -> model.addAttribute(error.getField() + "_error", error.getDefaultMessage()));
            return "account/register";
        }
        try {
            // Save the user
            userService.save(user);

            // Automatically log in the user
            UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
            Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(auth);

        } catch (DataIntegrityViolationException e) {
            // Handle duplicate entry exception
            Throwable rootCause = getRootCause(e);
            if (rootCause instanceof java.sql.SQLIntegrityConstraintViolationException) {
                model.addAttribute("username_error", "Username already exists. Please choose another one.");
                return "account/register";
            } else {
                throw e; // Re-throw the exception if it's not an integrity constraint violation
            }
        } catch (UsernameNotFoundException e) {
            e.printStackTrace();
            // Handle error if user not found
            return "redirect:/login";
        }

        return "redirect:/"; // Redirect to the dashboard or home page after login
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
        User savedUser = userService.getUserByEmail((String) attributes.get("email")).orElseThrow(
                () -> new IllegalArgumentException("Invalid user with email:" + attributes.get("email")));
        model.addAttribute("user", savedUser);
        return "account/confirm";
    }

    @PostMapping("/confirm")
    public String updateUser(@Valid @ModelAttribute("user") User user, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            bindingResult.getFieldErrors().forEach(error -> model.addAttribute(error.getField() + "_error", error.getDefaultMessage()));
            return "account/register";
        }
        // Save the user
        try {
            userService.updateUser(user);

        } catch (DataIntegrityViolationException e) {
            // Handle duplicate entry exception
            Throwable rootCause = getRootCause(e);
            if (rootCause instanceof java.sql.SQLIntegrityConstraintViolationException) {
                model.addAttribute("username_error", "Username already exists. Please choose another one.");
                return "account/confirm";
            } else {
                throw e; // Re-throw the exception if it's not an integrity constraint violation
            }
        } catch (UsernameNotFoundException e) {
            e.printStackTrace();
            // Handle error if user not found
            return "redirect:/";
        }

        return "redirect:/";
    }

    private Throwable getRootCause(Throwable throwable) {
        Throwable cause = throwable;
        while (cause.getCause() != null && cause != cause.getCause()) {
            cause = cause.getCause();
        }
        return cause;
    }

}
