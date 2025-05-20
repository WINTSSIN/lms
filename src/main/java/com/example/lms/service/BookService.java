package com.example.lms.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.lms.model.Book;
import com.example.lms.model.Genre;
import com.example.lms.repository.BookRepository;

@Service
public class BookService {
    
    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }
    
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }

    public List<Book> searchBooks(String title, String author) {
        return bookRepository.findBooksByTitleOrAuthor(title, author);  // Call the repository method
    }

    public List<Book> getAllBooks(Sort sort) {
        return bookRepository.findAll(sort);
    }

    public List<Book> getAllBooksSorted(Sort sort) {
        return bookRepository.findAll(sort);
    }

    // public List<Book> searchBooks(String title, String author, Genre genre, Boolean available) {
    //     return books.stream()
    //             .filter(book -> (title == null || book.getTitle().toLowerCase().contains(title.toLowerCase())))
    //             .filter(book -> (author == null || book.getAuthor().toLowerCase().contains(author.toLowerCase())))
    //             .filter(book -> (genre == null || book.getGenre() == genre))
    //             .filter(book -> (available == null || book.isAvailable() == available))
    //             .collect(Collectors.toList());
    // }

    public List<Book> searchBooks(String title, String author, Genre genre, Boolean available) {
        // Fetch all books from the database
        List<Book> books = bookRepository.findAll();
        
        return books.stream()
                .filter(book -> (title == null || book.getTitle().toLowerCase().contains(title.toLowerCase())))
                .filter(book -> (author == null || book.getAuthor().toLowerCase().contains(author.toLowerCase())))
                .filter(book -> (genre == null || book.getGenre() == genre))
                .filter(book -> (available == null || book.isAvailable() == available))  // Check if available matches
                .collect(Collectors.toList());
    }

    public Book saveBook(Book book) {
        if (book.getCreated() == null) {
            book.setCreated(LocalDateTime.now());  // Set current date and time if not already set
        }
        return bookRepository.save(book);
    }

    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }

    public Book getBookInfoById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Book not found with id: " + id));
    }


}
