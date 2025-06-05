package com.example.librarysystem.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
@Table(name = "books")
public class Book {


    // ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")  // Måste matcha kolumnnamnet i DB
    private Long bookId;

    //info
    @Column(name = "title")
    private String title;

    @Column(name = "publication_year")
    private Integer publicationYear;

    @Column(name = "available_copies")
    private Integer availableCopies;

    @Column(name = "total_copies")
    private Integer totalCopies;

    //relationer
    @ManyToOne
    @JoinColumn(name = "author_id")
    @JsonBackReference
    private Author author;

    // Default constructor krävs av JPA
    public Book() {}

    public Book(String title, Integer publicationYear, Integer availableCopies, Integer totalCopies, Author author) {
        this.title = title;
        this.publicationYear = publicationYear;
        this.availableCopies = availableCopies;
        this.totalCopies = totalCopies;
        this.author = author;
    }

    // Getters och setters
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
    public Author getAuthor() {
        return author;
    }
    public void setAuthor(Author authorId) {
        this.author = authorId;
    }
}