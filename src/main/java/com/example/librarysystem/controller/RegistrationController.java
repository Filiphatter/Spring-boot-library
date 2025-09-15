package com.example.librarysystem.controller;

import com.example.librarysystem.dto.RegistrationRequestDTO;
import com.example.librarysystem.dto.UserDTO;
import com.example.librarysystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class RegistrationController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegistrationRequestDTO dto) {
        try {
            UserDTO registeredUser = userService.registerUser(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(registeredUser);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Registrering misslyckades: " + e.getMessage());
        }
    }
}

//varför en till? olika säkerhets "regler" /auth/register är permit all, medans users/add kräver admin
//även registrationrequestDTO för publik och userRequestDTO för admin skapande
//även lite mer struktur för admin v användare