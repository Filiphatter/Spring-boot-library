package com.example.librarysystem.controller;


import com.example.librarysystem.dto.AuthorSearchDTO;
import com.example.librarysystem.dto.AuthorWithDetailsDTO;
import com.example.librarysystem.entity.Author;
import com.example.librarysystem.service.AuthorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


    @RestController
    @RequestMapping("/authors")
    public class AuthorController {
        private final AuthorService authorService;

        public AuthorController(AuthorService authorService) {
            this.authorService = authorService;
        }

        // get
        @GetMapping
        public List<AuthorWithDetailsDTO> getAllAuthors() {
            return authorService.getAllAuthors();
        }

        // /authors/search?lastName=Lindgren
        @GetMapping("/search")
        public List<AuthorSearchDTO> searchAuthors(@RequestParam String lastName) {
            return authorService.searchAuthorLastName(lastName);
        }

    /*post + exempel info
    {
    "firstName": "Lars",
    "lastName": "Norén",
    "birthYear": 1944,
    "nationality": "Swedish"
    }
    */
    @PostMapping("/add")
    public ResponseEntity<Author> createAuthor(@RequestBody Author author) {
        Author saved = authorService.createAuthor(author);
        return ResponseEntity.ok(saved);
    }
}
