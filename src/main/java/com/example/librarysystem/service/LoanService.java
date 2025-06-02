package com.example.librarysystem.service;

import com.example.librarysystem.dto.LoanDTO;
import com.example.librarysystem.dto.LoanRequestDTO;
import com.example.librarysystem.entity.Book;
import com.example.librarysystem.entity.Loan;
import com.example.librarysystem.entity.User;
import com.example.librarysystem.repository.BookRepository;
import com.example.librarysystem.repository.LoanRepository;
import com.example.librarysystem.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LoanService {
    private final LoanRepository loanRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    public LoanService(LoanRepository loanRepository, BookRepository bookRepository, UserRepository userRepository) {
        this.loanRepository = loanRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    public LoanDTO borrowBook(LoanRequestDTO dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("user not found"));
        Book book = bookRepository.findById(dto.getBookId())
                .orElseThrow(() -> new RuntimeException("Book not found"));

        if (book.getAvailableCopies() <= 0) {
            throw new RuntimeException("No copies available for this book");
        }

        Loan loan = new Loan();
        loan.setUser(user);
        loan.setBook(book);
        loan.setBorrowedDate(LocalDate.now());
        loan.setDueDate(LocalDate.now().plusDays(14));
        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookRepository.save(book);

        return mapToDTO(loanRepository.save(loan));
    }

    public LoanDTO returnBook(Long loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Loan not found"));

        if (loan.getReturnedDate() != null) {
            throw new RuntimeException("Book already returned");
        }

    loan.setReturnedDate(LocalDate.now());
    Book book = loan.getBook();
    book.setAvailableCopies(book.getAvailableCopies() + 1);
    bookRepository.save(book);

    return mapToDTO(loanRepository.save(loan));
    }

    public List<LoanDTO> getAllLoans() {
        List<Loan> loans = loanRepository.findAll();

        if (loans.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Inga lån hittades.");
        }

        return loans.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public LoanDTO getLoanById(Long loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Lån med id " + loanId + " hittades inte."));
                return mapToDTO(loan);
    }

    public LoanDTO extendLoan(Long loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Loan not found"));

        if (loan.getReturnedDate() != null) {
            throw new RuntimeException("Cannot extend a returned Loan");
        }

        loan.setDueDate(loan.getDueDate().plusDays(14));
        Loan updated = loanRepository.save(loan);
        return mapToDTO(updated);
    }

    private LoanDTO mapToDTO(Loan loan) {
        LoanDTO dto = new LoanDTO();
        dto.setLoanId(loan.getLoanId());
        dto.setBorrowedDate(loan.getBorrowedDate());
        dto.setDueDate(loan.getDueDate());
        dto.setReturnedDate(loan.getReturnedDate());
        dto.setUserName(loan.getUser().getFirstName() + " " + loan.getUser().getLastName());
        dto.setBookTitle(loan.getBook().getTitle());
        return dto;
    }


}

