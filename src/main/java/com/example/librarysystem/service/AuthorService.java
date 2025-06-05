package com.example.librarysystem.service;

import com.example.librarysystem.dto.AuthorSearchDTO;
import com.example.librarysystem.dto.AuthorWithDetailsDTO;
import com.example.librarysystem.dto.BookSearchDTO;

import com.example.librarysystem.entity.Author;
import com.example.librarysystem.repository.AuthorRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthorService {
    private final AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public List<AuthorWithDetailsDTO> getAllAuthors() {
        List<Author> authors = authorRepository.findAll();

        if (authors.isEmpty()) {
            throw new RuntimeException("Inga författare hittades.");
        }

        return authors.stream()
                .map(this::mapToAuthorWithDetailsDTO)
                .collect(Collectors.toList());
    }

    public List<AuthorSearchDTO> searchAuthorLastName(String lastName) {
        if (!StringUtils.hasText(lastName)) {
            throw new IllegalArgumentException("Efternamn får inte vara tomt vid sökning");
        }

        List<Author> authors = authorRepository.findByLastName(lastName);

        if (authors.isEmpty()) {
            throw new RuntimeException("Ingen författare hittades med efternamnet: " + lastName);
        }

        return authors.stream()
                .map(this::mapToAuthorSearchDTO)
                .collect(Collectors.toList());
    }

    public Author createAuthor(Author author) {
        if (!isValidAuthor(author)) {
            throw new IllegalArgumentException("Ogiltiga författardata. Förnamn, efternamn och nationalitet krävs");
        }
        return authorRepository.save(author);
    }

    // Kontrollera att nödvändiga fält är ifyllda
    private boolean isValidAuthor(Author author) {
        return author != null &&
                StringUtils.hasText(author.getFirstName()) &&
                StringUtils.hasText(author.getLastName()) &&
                StringUtils.hasText(author.getNationality());
    }

    //mappers
    private AuthorSearchDTO mapToAuthorSearchDTO(Author author) {
        String fullName = author.getFirstName() + " " + author.getLastName();
        return new AuthorSearchDTO(author.getAuthorId(), fullName, author.getNationality());
    }

    private AuthorWithDetailsDTO mapToAuthorWithDetailsDTO(Author author) {
        List<BookSearchDTO> books = author.getBooks().stream()
                .map(book -> new BookSearchDTO(
                        book.getBookId(),
                        book.getTitle(),
                        book.getPublicationYear(),
                        author.getFirstName() + " " + author.getLastName()
                )).collect(Collectors.toList());

        return new AuthorWithDetailsDTO(
                author.getAuthorId(),
                author.getFirstName(),
                author.getLastName(),
                author.getBirthYear(),
                author.getNationality(),
                books
        );
    }
}
