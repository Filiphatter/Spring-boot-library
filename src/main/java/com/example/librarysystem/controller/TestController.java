package com.example.librarysystem.controller;  // ← Lägg till denna rad

import com.example.librarysystem.entity.Role;
import com.example.librarysystem.entity.User;
import com.example.librarysystem.repository.RoleRepository;
import com.example.librarysystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class TestController {

    @Autowired
    private UserRepository userRepository;
    private RoleRepository roleRepository;

    @GetMapping("/test/user-roles")
    public String testUserRoles() {
        return "User roles test endpoint";
    }

    @GetMapping("/test/db-check")
    public String checkDatabase() {
        List<User> users = userRepository.findAll();
        StringBuilder result = new StringBuilder();

        for (User user : users) {
            result.append("User: ").append(user.getEmail())
                    .append(", Roles: ").append(user.getRoles().size())
                    .append("\n");

            for (Role role : user.getRoles()) {
                result.append("  - Role: ").append(role.getName()).append("\n");
            }
        }

        return result.toString();
    }

    @GetMapping("/test/roles-check")
    public String checkRoles() {
        try {
            List<Role> existingRoles = roleRepository.findAll();
            return "Existing roles: " + existingRoles.size() +
                    " roles found: " + existingRoles.stream()
                    .map(Role::getName)
                    .collect(Collectors.joining(", "));
        } catch (Exception e) {
            return "Error checking roles: " + e.getMessage();
        }
    }
}