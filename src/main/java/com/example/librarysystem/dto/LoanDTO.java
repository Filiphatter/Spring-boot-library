package com.example.librarysystem.dto;

import java.time.LocalDate;

public class LoanDTO {
    private Long loanId;
    private LocalDate borrowedDate;
    private LocalDate dueDate;
    private LocalDate returnedDate;
    private String userName;
    private String bookTitle;

    // Getters och Setters
    public Long getLoanId() { return loanId; }
    public void setLoanId(Long loanId) { this.loanId = loanId; }

    public LocalDate getBorrowedDate() { return borrowedDate; }
    public void setBorrowedDate(LocalDate borrowedDate) {
        this.borrowedDate = borrowedDate; }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate; }

    public LocalDate getReturnedDate() { return returnedDate; }
    public void setReturnedDate(LocalDate returnedDate) {
        this.returnedDate = returnedDate; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) {
        this.userName = userName; }

    public String getBookTitle() { return bookTitle; }
    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle; }
}

