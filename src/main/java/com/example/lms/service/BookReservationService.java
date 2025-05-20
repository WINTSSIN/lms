package com.example.lms.service;

import java.time.LocalDate;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.lms.model.Book;
import com.example.lms.model.BookReservation;
import com.example.lms.model.Member;
import com.example.lms.model.ReservationStatus;
import com.example.lms.repository.BookReservationRepository;

@Service
public class BookReservationService {

    @Autowired
    private final BookReservationRepository bookReservationRepository;
    
    public BookReservationService(BookReservationRepository bookReservationRepository) {
        this.bookReservationRepository = bookReservationRepository;
    }

    public Optional<BookReservation> getBookReservationById(Long id) {
        return bookReservationRepository.findById(id);
    }

    public void updateReservationStatus(Long reservationId, ReservationStatus status) {
        bookReservationRepository.findById(reservationId).ifPresent(reservation -> {
            reservation.setStatus(status);
            bookReservationRepository.save(reservation);
        });
    }

    // public BookReservation reserveBook(Book book, Member member) {
    //     BookReservation reservation = new BookReservation(book, member, LocalDate.now(), false, false);
    //     return bookReservationRepository.save(reservation);
    // }

}
