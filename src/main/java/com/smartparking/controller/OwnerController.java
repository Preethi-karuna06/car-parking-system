package com.smartparking.controller;

import com.smartparking.security.CustomUserDetailsService.UserPrincipal;
import com.smartparking.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.math.BigDecimal;

@Controller
@RequestMapping("/owner")
public class OwnerController {

    @Autowired private ParkingAreaService parkingService;
    @Autowired private BookingService bookingService;
    @Autowired private ReviewService reviewService;
    @Autowired private ImageUploadService imageUploadService;

    @GetMapping("/dashboard")
    public String dashboard(@AuthenticationPrincipal UserPrincipal p, Model model) {
        var areas = parkingService.getByOwner(p.getUser());
        BigDecimal revenue = bookingService.getOwnerRevenue(p.getUser().getId());
        model.addAttribute("user", p.getUser());
        model.addAttribute("areas", areas);
        model.addAttribute("revenue", revenue != null ? revenue : BigDecimal.ZERO);
        model.addAttribute("totalSlots", areas.stream().mapToInt(a -> a.getTotalSlots()).sum());
        model.addAttribute("availableSlots", areas.stream().mapToInt(a -> a.getAvailableSlots()).sum());
        return "owner/dashboard";
    }

    @GetMapping("/parking")
    public String myParking(@AuthenticationPrincipal UserPrincipal p, Model model) {
        model.addAttribute("areas", parkingService.getByOwner(p.getUser()));
        return "owner/parking";
    }

    @GetMapping("/parking/add")
    public String addForm() { return "owner/add-parking"; }

    @PostMapping("/parking/add")
    public String addParking(@RequestParam String name, @RequestParam String address,
                              @RequestParam String city, @RequestParam String state,
                              @RequestParam(required=false,defaultValue="") String pincode,
                              @RequestParam(required=false) Double latitude,
                              @RequestParam(required=false) Double longitude,
                              @RequestParam Integer totalSlots,
                              @RequestParam BigDecimal pricePerHour,
                              @RequestParam String supportedVehicleTypes,
                              @RequestParam(required=false) String description,
                              @RequestParam(required=false) String facilities,
                              @RequestParam(required=false) MultipartFile imageFile,
                              @RequestParam(required=false) String imageUrl,
                              @AuthenticationPrincipal UserPrincipal p, RedirectAttributes ra) {
        try {
            String fileName = null;
            if (imageFile != null && !imageFile.isEmpty())
                fileName = imageUploadService.save(imageFile);
            String urlFinal = (imageUrl != null && !imageUrl.isBlank()) ? imageUrl : null;
            parkingService.add(name, address, city, state, pincode, latitude, longitude,
                    totalSlots, pricePerHour, supportedVehicleTypes, description, facilities,
                    fileName, urlFinal, p.getUser());
            ra.addFlashAttribute("success", "Parking area submitted for admin approval!");
        } catch (Exception e) { ra.addFlashAttribute("error", e.getMessage()); }
        return "redirect:/owner/parking";
    }

    @PostMapping("/parking/{id}/toggle")
    public String toggle(@PathVariable Long id, @AuthenticationPrincipal UserPrincipal p, RedirectAttributes ra) {
        try { parkingService.toggleStatus(id, p.getUser()); ra.addFlashAttribute("success", "Status updated."); }
        catch (Exception e) { ra.addFlashAttribute("error", e.getMessage()); }
        return "redirect:/owner/parking";
    }

    @GetMapping("/bookings")
    public String bookings(@AuthenticationPrincipal UserPrincipal p, Model model) {
        var bookings = parkingService.getByOwner(p.getUser()).stream()
                .flatMap(a -> bookingService.getParkingBookings(a).stream()).toList();
        model.addAttribute("bookings", bookings);
        return "owner/bookings";
    }

    @PostMapping("/bookings/{id}/confirm")
    public String confirm(@PathVariable Long id, RedirectAttributes ra) {
        try { bookingService.confirm(id); ra.addFlashAttribute("success", "Booking confirmed."); }
        catch (Exception e) { ra.addFlashAttribute("error", e.getMessage()); }
        return "redirect:/owner/bookings";
    }

    @PostMapping("/bookings/{id}/reject")
    public String reject(@PathVariable Long id, @RequestParam String reason, RedirectAttributes ra) {
        try { bookingService.reject(id, reason); ra.addFlashAttribute("success", "Booking rejected."); }
        catch (Exception e) { ra.addFlashAttribute("error", e.getMessage()); }
        return "redirect:/owner/bookings";
    }

    @PostMapping("/bookings/{id}/entry")
    public String entry(@PathVariable Long id, RedirectAttributes ra) {
        try { bookingService.recordEntry(id); ra.addFlashAttribute("success", "Entry recorded."); }
        catch (Exception e) { ra.addFlashAttribute("error", e.getMessage()); }
        return "redirect:/owner/bookings";
    }

    @PostMapping("/bookings/{id}/exit")
    public String exit(@PathVariable Long id, RedirectAttributes ra) {
        try { bookingService.recordExit(id); ra.addFlashAttribute("success", "Exit recorded. Bill calculated."); }
        catch (Exception e) { ra.addFlashAttribute("error", e.getMessage()); }
        return "redirect:/owner/bookings";
    }

    @GetMapping("/parking/{id}/reviews")
    public String reviews(@PathVariable Long id, Model model) {
        model.addAttribute("area", parkingService.findById(id));
        model.addAttribute("reviews", reviewService.getByParking(id));
        model.addAttribute("avgRating", reviewService.getAvgRating(id));
        return "owner/reviews";
    }
}
