package com.example.librarysystem.controller;

import com.example.librarysystem.dto.BookRequestDTO;
import com.example.librarysystem.dto.BookSearchDTO;
import com.example.librarysystem.dto.BookWithDetailsDTO;
import com.example.librarysystem.entity.Book;
import com.example.librarysystem.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;

    }
    //get
    @GetMapping
    public List<BookWithDetailsDTO> getAllBooks() {
    return bookService.getAllBooks();
    }
    // exemepl sökning http://localhost:8080/books/search?query=the
    @GetMapping("/search")
    public ResponseEntity<List<BookSearchDTO>> searchBooks(@RequestParam("query") String query) {
        List<BookSearchDTO> results = bookService.searchBooks(query);
        return ResponseEntity.ok(results);
    }

    //post http://localhost:8080/books
    /* exempel data
    {
        "title": "The Fellowship of the Ring",
            "publicationYear": 1954,
            "availableCopies": 10,
            "totalCopies": 15,
            "authorId": 1
    }
    */
    @PostMapping("/add")
    public ResponseEntity<Book> createBook(@RequestBody BookRequestDTO dto) {
        Book created = bookService.createBook(dto);
        return new ResponseEntity<Book>(created, HttpStatus.CREATED);
    }
}
