package com.example.librarysystem.dto;

public class BookRequestDTO {
    private String title;
    private Integer publicationYear;
    private Integer availableCopies;
    private Integer totalCopies;
    private Long authorId;

    // Standardkonstruktor
    public BookRequestDTO() {}

    // Fullständig konstruktor
    public BookRequestDTO(String title, Integer publicationYear, Integer availableCopies, Integer totalCopies, Long authorId) {
        this.title = title;
        this.publicationYear = publicationYear;
        this.availableCopies = availableCopies;
        this.totalCopies = totalCopies;
        this.authorId = authorId;
    }

    // Getters och Setters
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

    public Integer getAvailableCopies() {
        return availableCopies;
    }

    public void setAvailableCopies(Integer availableCopies) {
        this.availableCopies = availableCopies;
    }

    public Integer getTotalCopies() {
        return totalCopies;
    }

    public void setTotalCopies(Integer totalCopies) {
        this.totalCopies = totalCopies;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }
}