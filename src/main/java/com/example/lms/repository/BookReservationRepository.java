package com.example.lms.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.lms.model.BookReservation;
import com.example.lms.model.ReservationStatus;

public interface BookReservationRepository extends JpaRepository<BookReservation, Long> {
    List<BookReservation> findByMemberId(Long memberId);
    List<BookReservation> findByStatusNot(ReservationStatus status);
    // List<BookReservation> findByStatusNot(ReservationStatus status, Sort sort);
}
