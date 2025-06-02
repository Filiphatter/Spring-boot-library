package com.example.librarysystem.controller;

import com.example.librarysystem.dto.LoanDTO;
import com.example.librarysystem.dto.LoanRequestDTO;
import com.example.librarysystem.service.LoanService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/loans")
public class LoanController {
    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<LoanDTO>> getAllLoans() {
        return ResponseEntity.ok(loanService.getAllLoans());
    }

    @GetMapping("/{loanId}")
    public ResponseEntity<LoanDTO> getLoanById(@PathVariable Long loanId) {
        return ResponseEntity.ok(loanService.getLoanById(loanId));
    }

    /*
    {
    "userId": 1,
    "bookId": 5
    }
     */
    @PostMapping("/add")
    public ResponseEntity<LoanDTO> borrowBook(@RequestBody LoanRequestDTO dto) {
        return ResponseEntity.ok(loanService.borrowBook(dto));
    }
    // http://localhost:8080/loans/x(loanid)/return
    @PutMapping("/{loanId}/return")
    public ResponseEntity<LoanDTO> returnBook(@PathVariable Long loanId) {
        return ResponseEntity.ok(loanService.returnBook(loanId));
    }

    //add extend loan
    //http://localhost:8080/loans/51/extend exempelvis
    @PutMapping("/{loanId}/extend")
    public ResponseEntity<LoanDTO> extendLoan(@PathVariable Long loanId) {
        return ResponseEntity.ok(loanService.extendLoan(loanId));
    }

}
