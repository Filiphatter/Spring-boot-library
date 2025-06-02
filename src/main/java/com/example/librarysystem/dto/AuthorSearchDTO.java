package com.example.librarysystem.dto;

public class AuthorSearchDTO {
    private Long authorId;
    private String fullName;
    private String nationality;

    public AuthorSearchDTO() {}

    public AuthorSearchDTO(Long authorId, String fullName, String nationality) {
        this.authorId = authorId;
        this.fullName = fullName;
        this.nationality = nationality;
    }

    // Getters och Setters
    public Long getAuthorId() {
        return authorId;
    }
    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }
    public String getFullName() {
        return fullName;
    }
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    public String getNationality() {
        return nationality;
    }
    public void setNationality(String nationality) {
        this.nationality = nationality;
    }
}
