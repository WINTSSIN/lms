package com.example.lms.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.example.lms.model.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findBooksByTitleAndAuthor(String title, String author);

    Book findByTitle(String title);

    @Query("SELECT b FROM Book b WHERE LOWER(b.title) LIKE LOWER(CONCAT('%', :title, '%'))")
    List<Book> findBooksByTitleContaining(@Param("title") String title);

    @Query("SELECT b FROM Book b WHERE LOWER(b.title) LIKE LOWER(CONCAT('%', :title, '%')) OR LOWER(b.author) LIKE LOWER(CONCAT('%', :author, '%'))")
    List<Book> findBooksByTitleOrAuthor(@Param("title") String title, @Param("author") String author);

    List<Book> findByAvailableTrue();
    List<Book> findAll();
    Optional<Book> findById(Long id);

}
