package com.example.librarysystem.service;

import com.example.librarysystem.dto.BookRequestDTO;
import com.example.librarysystem.dto.BookSearchDTO;
import com.example.librarysystem.dto.BookWithDetailsDTO;
import com.example.librarysystem.entity.Author;
import com.example.librarysystem.entity.Book;
import com.example.librarysystem.repository.BookRepository;
import com.example.librarysystem.repository.AuthorRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    public BookService(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    public List<BookWithDetailsDTO> getAllBooks() {
        List<Book> books = bookRepository.findAll();

        if (books.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Inga böcker hittades.");
        }

        return books.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<BookSearchDTO> searchBooks(String query) {
        if (!StringUtils.hasText(query)) {
            throw new IllegalArgumentException("Söksträngen får inte vara tom.");
        }

        List<Book> books = bookRepository.searchBooks(query);

        if (books.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Inga böcker hittades för: " + query);
        }

        return books.stream().map(book -> {
            String fullName = book.getAuthor().getFirstName() + " " + book.getAuthor().getLastName();
            return new BookSearchDTO(
                    book.getBookId(),
                    book.getTitle(),
                    book.getPublicationYear(),
                    fullName
            );
        }).toList();
    }

    public Book createBook(BookRequestDTO dto) {
        validateBookRequest(dto);

        Author author = authorRepository.findById(dto.authorId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Författare med ID " + dto.authorId + " hittades inte."));

        Book book = new Book();
        book.setTitle(dto.title);
        book.setPublicationYear(dto.publicationYear);
        book.setAvailableCopies(dto.availableCopies);
        book.setTotalCopies(dto.totalCopies);
        book.setAuthor(author);

        return bookRepository.save(book);
    }

    // Validering av bokdata innan skapande
    private void validateBookRequest(BookRequestDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Bokdata får inte vara null.");
        }

        if (!StringUtils.hasText(dto.title)) {
            throw new IllegalArgumentException("Titel krävs.");
        }

        if (dto.publicationYear <= 0) {
            throw new IllegalArgumentException("Ogiltigt publiceringsår.");
        }

        if (dto.availableCopies < 0 || dto.totalCopies < 0) {
            throw new IllegalArgumentException("Antalet kopior kan inte vara negativt.");
        }

        if (dto.availableCopies > dto.totalCopies) {
            throw new IllegalArgumentException("Tillgängliga kopior kan inte vara fler än totalt antal.");
        }
    }

    //mapper
    private BookWithDetailsDTO mapToDTO(Book book) {
        BookWithDetailsDTO dto = new BookWithDetailsDTO();
        dto.setBookId(book.getBookId());
        dto.setTitle(book.getTitle());
        dto.setPublicationYear(book.getPublicationYear());
        dto.setAvailableCopies(book.getAvailableCopies());
        dto.setTotalCopies(book.getTotalCopies());

        if (book.getAuthor() != null) {
            dto.setAuthorId(book.getAuthor().getAuthorId());
            dto.setAuthorFirstName(book.getAuthor().getFirstName());
            dto.setAuthorLastName(book.getAuthor().getLastName());
            dto.setAuthorNationality(book.getAuthor().getNationality());
        }
        return dto;
        }


}


