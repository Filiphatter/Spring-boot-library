package com.example.librarysystem.dto;

import java.util.List;

public class AuthorWithDetailsDTO {
    private Long authorId;
    private String firstName;
    private String lastName;
    private Integer birthYear;
    private String nationality;
    private List<BookSearchDTO> books;

    public AuthorWithDetailsDTO() {}

    public AuthorWithDetailsDTO(Long authorId, String firstName, String lastName, Integer birthYear, String nationality, List<BookSearchDTO> books) {
        this.authorId = authorId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthYear = birthYear;
        this.nationality = nationality;
        this.books = books;
    }

    // Getters och Setters
    public Long getAuthorId() {
        return authorId;
    }
    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public Integer getBirthYear() {
        return birthYear;
    }
    public void setBirthYear(Integer birthYear) {
        this.birthYear = birthYear;
    }
    public String getNationality() {
        return nationality;
    }
    public void setNationality(String nationality) {
        this.nationality = nationality;
    }
    public List<BookSearchDTO> getBooks() {
        return books;
    }
    public void setBooks(List<BookSearchDTO> books) {
        this.books = books;
    }
}
