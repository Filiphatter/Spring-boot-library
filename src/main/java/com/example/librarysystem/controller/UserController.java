package com.example.librarysystem.controller;

import com.example.librarysystem.dto.UserDTO;
import com.example.librarysystem.dto.UserRequestDTO;
import com.example.librarysystem.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
    public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
             this.userService = userService;
         }

    @GetMapping("/email/{email}")
    public ResponseEntity<UserDTO> getUserByEmail(@PathVariable String email) {
        return userService.getUserByEmail(email)
                .map(ResponseEntity::ok) //om användaren hittas returneras 200 o json
                .orElse(ResponseEntity.notFound().build()); //om användaren inte hittas dvs optional är tom så returneras 404
    }

    /* exempeldata
    {
    "firstName": "Alice",
    "lastName": "petersson",
    "email": "alice@mail.com",
    "password": "secure123"
    }
     */
    @PostMapping("/add")
    public ResponseEntity<UserDTO> createUser(@RequestBody UserRequestDTO dto) {
        UserDTO created = userService.createUser(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }
}
