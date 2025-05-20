package com.example.lms.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


import com.example.lms.model.BookReservation;
import com.example.lms.model.Member;
import com.example.lms.model.ReservationStatus;
import com.example.lms.repository.BookReservationRepository;
import com.example.lms.service.BookReservationService;

@RestController
@RequestMapping("/bookReservation")
public class BookReservationController {
    private final BookReservationService bookReservationService;
    private final JavaMailSender mailSender;
    private final BookReservationRepository bookReservationRepository;

    public BookReservationController(BookReservationService bookReservationService,
                                     BookReservationRepository bookReservationRepository,
                                     JavaMailSender mailSender) {
        this.bookReservationService = bookReservationService;
        this.bookReservationRepository = bookReservationRepository;
        this.mailSender = mailSender;
    }

    @PostMapping("/updateStatus")
    @ResponseBody
    public ResponseEntity<?> updateReservationStatus(Long reservationId, ReservationStatus status) {
        bookReservationRepository.findById(reservationId).ifPresent(reservation -> {
            reservation.setStatus(status);
            bookReservationRepository.save(reservation);

            if (status == ReservationStatus.APPROVED || status == ReservationStatus.CANCELLED) {
                sendStatusUpdateEmail(reservation);
            }
        });
        return ResponseEntity.ok().build();
    }

    private void sendStatusUpdateEmail(BookReservation reservation) {
        Member member = reservation.getMember();
        String toEmail = member.getEmail();
        String subject = "予約状況が更新されました";
        String message = "こんにちは " + member.getName() + " 様,\n\n" +
                 "ご予約の本『" + reservation.getBook().getTitle() + "』のステータスが " +
                 reservation.getStatus() + " に更新されました。\n\nありがとうございます。";

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(toEmail);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);

        mailSender.send(mailMessage);
    }

    @GetMapping("/library/reservedBooksList")
    public String showReservedBooksList(Model model) {
        model.addAttribute("reservations", bookReservationRepository.findAll());
        return "library/reservedBooksList";
    }

}

