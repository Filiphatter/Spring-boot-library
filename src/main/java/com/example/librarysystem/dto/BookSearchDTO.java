package com.example.librarysystem.dto;

public class BookSearchDTO {
    private Long bookId;
    private String title;
    private Integer publicationYear;
    private String authorFullName;

    public BookSearchDTO() {}

    public BookSearchDTO(Long bookId, String title, Integer publicationYear, String authorFullName) {
        this.bookId = bookId;
        this.title = title;
        this.publicationYear = publicationYear;
        this.authorFullName = authorFullName;
    }
    // Getters och Setters
    public Long getBookId() {
        return bookId;
    }
    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public Integer getPublicationYear() {
        return publicationYear;
    }
    public void setPublicationYear(Integer publicationYear) {
        this.publicationYear = publicationYear;
    }
    public String getAuthorFullName() {
        return authorFullName;
    }
    public void setAuthorFullName(String authorFullName) {
        this.authorFullName = authorFullName;
    }
}

