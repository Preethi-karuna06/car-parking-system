package com.smartparking.controller;

import com.smartparking.enums.Role;
import com.smartparking.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class HomeController {

    @Autowired private UserService userService;
    @Autowired private ParkingAreaService parkingService;
    @Autowired private ReviewService reviewService;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("parkingAreas", parkingService.getAllActive());
        return "index";
    }

    @GetMapping("/login")
    public String login() { return "auth/login"; }

    @GetMapping("/register")
    public String registerForm() { return "auth/register"; }

    @PostMapping("/register")
    public String register(@RequestParam String fullName, @RequestParam String email,
                           @RequestParam String password, @RequestParam String phone,
                           @RequestParam String role,
                           @RequestParam(required = false) String vehicleNumber,
                           @RequestParam(required = false) String vehicleType,
                           RedirectAttributes ra) {
        try {
            userService.register(fullName, email, password, phone,
                    Role.valueOf(role), vehicleNumber, vehicleType);
            ra.addFlashAttribute("success", "Registration successful! Please login.");
            return "redirect:/login";
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
            return "redirect:/register";
        }
    }

    @GetMapping("/parking/search")
    public String search(@RequestParam(required = false) String city, Model model) {
        model.addAttribute("results", (city != null && !city.isBlank())
                ? parkingService.searchByCity(city) : parkingService.getAllActive());
        model.addAttribute("city", city);
        return "parking/search";
    }

    @GetMapping("/parking/{id}")
    public String detail(@PathVariable Long id, Model model) {
        var area = parkingService.findById(id);
        model.addAttribute("area", area);
        model.addAttribute("reviews", reviewService.getByParking(id));
        model.addAttribute("avgRating", reviewService.getAvgRating(id));
        model.addAttribute("reviewCount", reviewService.getCount(id));
        return "parking/detail";
    }
}
