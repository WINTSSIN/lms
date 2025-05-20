package com.example.lms.model;

import java.time.LocalDate;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class BookReturn {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private BookIssue bookIssue;

    private LocalDate returnDate;

    public BookReturn() {}

    public BookReturn(BookIssue bookIssue, LocalDate returnDate) {
        this.bookIssue = bookIssue;
        this.returnDate = returnDate;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public LocalDate getReturnDate() {
        return returnDate;
    }
    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }
    public BookIssue getBookIssue() {
        return bookIssue;
    }
    public void setBookIssue(BookIssue bookIssue) {
        this.bookIssue = bookIssue;
    }
}
