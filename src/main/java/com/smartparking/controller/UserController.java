package com.smartparking.controller;

import com.smartparking.security.CustomUserDetailsService.UserPrincipal;
import com.smartparking.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired private BookingService bookingService;
    @Autowired private ParkingAreaService parkingService;
    @Autowired private ReviewService reviewService;

    @GetMapping("/dashboard")
    public String dashboard(@AuthenticationPrincipal UserPrincipal p, Model model) {
        model.addAttribute("user", p.getUser());
        model.addAttribute("bookings", bookingService.getUserBookings(p.getUser()));
        return "user/dashboard";
    }

    @GetMapping("/bookings")
    public String bookings(@AuthenticationPrincipal UserPrincipal p, Model model) {
        model.addAttribute("bookings", bookingService.getUserBookings(p.getUser()));
        return "user/bookings";
    }

    @GetMapping("/book/{parkingId}")
    public String bookForm(@PathVariable Long parkingId,
                           @AuthenticationPrincipal UserPrincipal p, Model model) {
        model.addAttribute("parking", parkingService.findById(parkingId));
        model.addAttribute("user", p.getUser());
        return "user/book";
    }

    @PostMapping("/book")
    public String book(@RequestParam Long parkingAreaId,
                       @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime entryTime,
                       @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime exitTime,
                       @RequestParam(required = false) String vehicleNumber,
                       @RequestParam(required = false) String vehicleType,
                       @AuthenticationPrincipal UserPrincipal p, RedirectAttributes ra) {
        try {
            var b = bookingService.create(p.getUser(), parkingService.findById(parkingAreaId),
                    entryTime, exitTime, vehicleNumber, vehicleType);
            ra.addFlashAttribute("success", "Booking created! Code: " + b.getBookingCode());
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/user/bookings";
    }

    @PostMapping("/bookings/{id}/cancel")
    public String cancel(@PathVariable Long id,
                         @AuthenticationPrincipal UserPrincipal p, RedirectAttributes ra) {
        try { bookingService.cancel(id, p.getUser()); ra.addFlashAttribute("success", "Booking cancelled."); }
        catch (Exception e) { ra.addFlashAttribute("error", e.getMessage()); }
        return "redirect:/user/bookings";
    }

    @PostMapping("/bookings/{id}/pay")
    public String pay(@PathVariable Long id, RedirectAttributes ra) {
        try { bookingService.markPaid(id); ra.addFlashAttribute("success", "Payment successful!"); }
        catch (Exception e) { ra.addFlashAttribute("error", e.getMessage()); }
        return "redirect:/user/bookings";
    }

    @PostMapping("/review")
    public String review(@RequestParam Long parkingAreaId, @RequestParam Integer rating,
                          @RequestParam(required = false) String comment,
                          @AuthenticationPrincipal UserPrincipal p, RedirectAttributes ra) {
        try { reviewService.add(p.getUser(), parkingAreaId, rating, comment);
              ra.addFlashAttribute("success", "Review submitted!"); }
        catch (Exception e) { ra.addFlashAttribute("error", e.getMessage()); }
        return "redirect:/parking/" + parkingAreaId;
    }
}
