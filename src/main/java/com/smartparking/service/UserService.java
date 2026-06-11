package com.smartparking.service;

import com.smartparking.entity.User;
import com.smartparking.enums.Role;
import com.smartparking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User register(String fullName, String email, String password, String phone,
                         Role role, String vehicleNumber, String vehicleType) {
        if (userRepository.existsByEmail(email))
            throw new RuntimeException("Email already registered");
        if (userRepository.existsByPhone(phone))
            throw new RuntimeException("Phone already registered");

        User user = new User();
        user.setFullName(fullName);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setPhone(phone);
        user.setRole(role);
        user.setVehicleNumber(vehicleNumber);
        user.setVehicleType(vehicleType);
        user.setEnabled(true);
        return userRepository.save(user);
    }

    public List<User> getAllUsers()  { return userRepository.findByRole(Role.USER); }
    public List<User> getAllOwners() { return userRepository.findByRole(Role.OWNER); }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found: " + id));
    }

    public User toggleStatus(Long id) {
        User user = findById(id);
        user.setEnabled(!user.isEnabled());
        return userRepository.save(user);
    }
}
