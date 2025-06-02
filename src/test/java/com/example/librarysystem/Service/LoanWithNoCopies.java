package com.example.librarysystem.Service;


import com.example.librarysystem.dto.LoanRequestDTO;
import com.example.librarysystem.entity.Book;
import com.example.librarysystem.entity.Loan;
import com.example.librarysystem.entity.User;
import com.example.librarysystem.repository.BookRepository;
import com.example.librarysystem.repository.LoanRepository;
import com.example.librarysystem.repository.UserRepository;
import com.example.librarysystem.service.LoanService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class LoanWithNoCopies {

    @Mock
    private LoanRepository loanRepository;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private LoanService loanService;

    @BeforeEach
    void setUp() {
        loanRepository = mock(LoanRepository.class);
        bookRepository = mock(BookRepository.class);
        userRepository = mock(UserRepository.class);
        loanService = new LoanService(loanRepository, bookRepository, userRepository);
    }

    @Test
    void BorrowBookWhenNoCopies() {
        //arrange
        Long userId = 1L;
        Long bookId = 2L;

        User user = new User();
        user.setUserID(userId);
        Book book = new Book();
        book.setBookId(bookId);
        book.setAvailableCopies(0); //inga tillg'ngliga b;cker

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

        LoanRequestDTO loanRequestDTO = new LoanRequestDTO();
        loanRequestDTO.setUserId(userId);
        loanRequestDTO.setBookId(bookId);

        //act
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            loanService.borrowBook(loanRequestDTO);
        });

        //assert
        assertEquals("No copies available for this book", exception.getMessage());

        verify(bookRepository, never()).save(any());   // bok ska inte sparas
        verify(loanRepository, never()).save(any());   // lån ska inte sparas
    }
}

/*
Mockar repos för retunera 0 möjliga böcker
skickar loanrequest till borrowbook
Kontrollerar runtimeexception
verifierar att verken sparas i repo
 */
