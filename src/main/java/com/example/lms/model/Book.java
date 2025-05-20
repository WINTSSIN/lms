package com.example.lms.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity

public class Book {
    @Id
    @GeneratedValue

    private long id;
    private String title;
    private String author;
    private String isbn;
    @Enumerated(EnumType.STRING) 
    private Genre genre;
    private int quantity;
    // private int availableqty;
    private double price;
    private LocalDate published;
    private boolean available  = true;  // true if the book is available, false if issued
    private String imagePath;
    private String imageName;
    private String creater;
    private String updater;
    private LocalDateTime created;
    private LocalDateTime updated;

    // @OneToMany(mappedBy = "book")
    // private List<BookIssue> bookIssues;  // List of book issues (貸出)

    // @OneToMany(mappedBy = "book")
    // private List<Reservation> reservations;  // List of reservations
    
    public Book() {}

    // public Book(long id, String title, String author, String isbn, Genre genre, int quantity, int availableqty, double price,
    public Book(long id, String title, String author, String isbn, Genre genre, int quantity, double price,
                LocalDate published,  boolean available, String imagePath, String imageName, String creater, String updater, LocalDateTime created, LocalDateTime updated) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.quantity = quantity;
        // this.availableqty = availableqty;
        this.genre = genre;
        this.price = price;
        this.published = published;
        this.creater = creater;
        this.updater = updater;
        this.created = created;
        this.updated = updated;
        this.available = available;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // public int getAvailableqty() {
    //     return availableqty;
    // }

    // public void setAvailableqty(int availableqty) {
    //     this.availableqty = availableqty;
    // }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public LocalDate getPublished() {
        return published;
    }

    public void setPublished(LocalDate published) {
        this.published = published;
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }

    public String getUpdater() {
        return updater;
    }

    public void setUpdater(String updater) {
        this.updater = updater;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public LocalDateTime getUpdated() {
        return updated;
    }

    public void setUpdated(LocalDateTime updated) {
        this.updated = updated;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    // public List<BookIssue> getBookIssues() {
    //     return bookIssues;
    // }

    // public void setBookIssues(List<BookIssue> bookIssues) {
    //     this.bookIssues = bookIssues;
    // }

    // public List<Reservation> getReservations() {
    //     return reservations;
    // }

    // public void setReservations(List<Reservation> reservations) {
    //     this.reservations = reservations;
    // }

    public Genre getGenre() {
        return genre;
    }

    public void setCategory(Genre genre) {
        this.genre = genre;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

}
