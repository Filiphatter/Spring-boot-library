package com.example.librarysystem.repository;
import com.example.librarysystem.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

    @Repository
    public interface BookRepository extends JpaRepository<Book, Long> {
    @Query("SELECT b FROM Book b WHERE LOWER(b.title) LIKE LOWER(CONCAT('%', :query, '%')) " + //hämtar objekt av Book
        "OR LOWER(b.author.firstName) LIKE LOWER(CONCAT('%', :query, '%')) " + //söker på book.author.firstname o lastname o title
        "OR LOWER(b.author.lastName) LIKE LOWER(CONCAT('%', :query, '%'))") //concat gör de icke sensitive till capslock (fick hjälp av ai här)
                    // Sedan % wildcard för ospecifika sökningar (fick lite hjälp med syntax error av ai igen) OR delen gör att de matchar om någon stämmer
    List<Book> searchBooks(@Param("query") String query);
}