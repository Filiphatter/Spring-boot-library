package com.example.librarysystem.Service;

import com.example.librarysystem.dto.LoanDTO;
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

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class LoanServiceTest {

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
    void borrowBookCorrectDueDate() {
        //arrange
        LoanRequestDTO dto = new LoanRequestDTO();
        dto.setUserId(1L);
        dto.setBookId(1L);

        User user = new User();
        user.setUserID(1L);
        user.setFirstName("Test");
        user.setLastName("User");

        Book book = new Book();
        book.setBookId(1L);
        book.setTitle("Test book");
        book.setAvailableCopies(2);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(loanRepository.save(any(Loan.class))).thenAnswer(invocation -> invocation.getArgument(0));

        //act
        LoanDTO result = loanService.borrowBook(dto);

        //assert
        assertEquals(LocalDate.now().plusDays(14), result.getDueDate());
        assertEquals(LocalDate.now(), result.getBorrowedDate());
    }
}


/* summering av detta
    först mockar jag repos
    skapar loanrequestdto som input
    mockar repository svaren med When
    kör sedan LoanService.borrowbook
    och asserterar due o borrow date

    resultatet var 1 av 1 test klara
    varav simulationen kollat att borrowdate och duedate funkar.
 */