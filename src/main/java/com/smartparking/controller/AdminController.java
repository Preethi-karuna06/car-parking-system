package com.smartparking.controller;

import com.smartparking.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.math.BigDecimal;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired private UserService userService;
    @Autowired private ParkingAreaService parkingService;
    @Autowired private BookingService bookingService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("totalUsers", userService.getAllUsers().size());
        model.addAttribute("totalOwners", userService.getAllOwners().size());
        model.addAttribute("totalParking", parkingService.getAll().size());
        model.addAttribute("pendingParking", parkingService.getPending().size());
        model.addAttribute("totalBookings", bookingService.getAll().size());
        BigDecimal rev = bookingService.getTotalRevenue();
        model.addAttribute("totalRevenue", rev != null ? rev : BigDecimal.ZERO);
        model.addAttribute("recentBookings", bookingService.getAll().stream()
                .sorted((a, b) -> b.getCreatedAt() != null
                        ? b.getCreatedAt().compareTo(a.getCreatedAt()) : 0)
                .limit(5).toList());
        return "admin/dashboard";
    }

    @GetMapping("/users")
    public String users(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "admin/users";
    }

    @GetMapping("/owners")
    public String owners(Model model) {
        model.addAttribute("owners", userService.getAllOwners());
        return "admin/owners";
    }

    @PostMapping("/users/{id}/toggle")
    public String toggleUser(@PathVariable Long id, RedirectAttributes ra) {
        userService.toggleStatus(id);
        ra.addFlashAttribute("success", "User status updated.");
        return "redirect:/admin/users";
    }

    @GetMapping("/parking")
    public String parking(Model model) {
        model.addAttribute("areas", parkingService.getAll());
        return "admin/parking";
    }

    @PostMapping("/parking/{id}/approve")
    public String approve(@PathVariable Long id, RedirectAttributes ra) {
        parkingService.approve(id);
        ra.addFlashAttribute("success", "Parking area approved!");
        return "redirect:/admin/parking";
    }

    @PostMapping("/parking/{id}/suspend")
    public String suspend(@PathVariable Long id, RedirectAttributes ra) {
        parkingService.suspend(id);
        ra.addFlashAttribute("success", "Parking area suspended.");
        return "redirect:/admin/parking";
    }

    @GetMapping("/bookings")
    public String bookings(Model model) {
        model.addAttribute("bookings", bookingService.getAll());
        return "admin/bookings";
    }
}
