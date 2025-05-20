package com.example.lms.service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.lms.model.Book;
import com.example.lms.model.BookIssue;
import com.example.lms.model.BookReservation;
import com.example.lms.model.Member;
import com.example.lms.model.ReservationStatus;
import com.example.lms.repository.BookIssueRepository;
import com.example.lms.repository.BookRepository;
import com.example.lms.repository.BookReservationRepository;
import com.example.lms.repository.MemberRepository;
import org.springframework.data.domain.Sort;

@Service
public class LibraryService {
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private BookIssueRepository bookIssueRepository;
    @Autowired
    private BookReservationRepository bookReservationRepository;

    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public String issueBook(Long bookId, Long memberId) {
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new RuntimeException("Book not found"));
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new RuntimeException("User not found"));
    
        if (book.getQuantity() <= 0) {
            throw new RuntimeException("Book not available");
        }

        BookIssue bookIssue = new BookIssue();
        bookIssue.setBook(book);
        bookIssue.setMember(member);
        bookIssue.setFine(500);
        bookIssue.setDueDate(LocalDate.now().plusDays(5));
        bookIssue.setIssueDate(LocalDate.now());
    
        book.setQuantity(book.getQuantity() - 1);
        if (book.getQuantity() == 0) {
            book.setAvailable(false);
        }
        bookRepository.save(book);
        bookIssueRepository.save(bookIssue);
        return "Book issued successfully";
    }

    public String reserveBook(Long bookId, Long memberId) {
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new RuntimeException("Book not found"));
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new RuntimeException("Member not found"));

        if (book.getQuantity() <= 0) {
            throw new RuntimeException("Sorry, the book is currently unavailable for reservation.");
        }

        BookReservation reservation = new BookReservation();
        reservation.setBook(book);
        reservation.setMember(member);
        reservation.setReservationDate(LocalDate.now());
        reservation.setStatus(ReservationStatus.PENDING);
  
        book.setQuantity(book.getQuantity() - 1);

        if (book.getQuantity() == 0) {
            book.setAvailable(false);
        }
        
        bookRepository.save(book);
        bookReservationRepository.save(reservation);
        return "This Book reserved successfully";
    }

    public String returnBook(Long bookId, Long memberId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new RuntimeException("Member not found"));

        BookIssue bookIssue = bookIssueRepository.findByMemberAndReturnDateIsNull(memberRepository.findById(memberId).get())
        .stream()
        .filter(b -> b.getBook().getId() == bookId)
        .findFirst()
        .orElseThrow(() -> new RuntimeException("No issued record found for this book"));
        
        bookIssue.setReturnDate(LocalDate.now());
        bookIssue.setIsReturned(true);

        book.setQuantity(book.getQuantity() + 1);
        if (book.getQuantity() > 0) {
            book.setAvailable(true);
        }
        
        bookRepository.save(book);
        bookIssueRepository.save(bookIssue);
        
        return "Book returned successfully.";
    }

    public List<BookIssue> getIssuedBooks(Sort sort) {
        return bookIssueRepository.findAll(sort);
    }

    public List<BookReservation> getReservedBooks(Sort sort) {
        List<BookReservation> list = bookReservationRepository.findByStatusNot(ReservationStatus.CANCELLED);
    
        if (sort.iterator().hasNext()) {
            Sort.Order order = sort.iterator().next();
            String property = order.getProperty();
            boolean ascending = order.isAscending();
    
            Comparator<BookReservation> comparator = switch (property) {
                case "title" -> Comparator.comparing(r -> r.getBook().getTitle(), String.CASE_INSENSITIVE_ORDER);
                case "name" -> Comparator.comparing(r -> r.getMember().getName(), String.CASE_INSENSITIVE_ORDER);
                case "reservationDate" -> Comparator.comparing(BookReservation::getReservationDate);
                case "status" -> Comparator.comparing(r -> r.getStatus().name());
                default -> Comparator.comparing(BookReservation::getId); // fallback
            };
    
            if (!ascending) {
                comparator = comparator.reversed();
            }
    
            list.sort(comparator);
        }
    
        return list;
    }

}