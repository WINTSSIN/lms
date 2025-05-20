package com.example.lms.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.lms.model.Book;
import com.example.lms.model.BookIssue;
import com.example.lms.model.Member;
import com.example.lms.repository.BookIssueRepository;

@Service
public class BookIssueService {
    @Autowired
    private BookIssueRepository bookIssueRepository;

    public List<BookIssue> getAllBookIssues() {
        return bookIssueRepository.findAll();
    }

    public BookIssue issueBook(Book book, Member member) {
        BookIssue bookIssue = new BookIssue(book, member, LocalDate.now(), null, null, 0, false);
        return bookIssueRepository.save(bookIssue);
    }

    public BookIssue returnBook(Long issueId) {
        BookIssue bookIssue = bookIssueRepository.findById(issueId).orElse(null);
        if (bookIssue != null) {
            bookIssue.setReturnDate(LocalDate.now());
            return bookIssueRepository.save(bookIssue);
        }
        return null;
    }

    public List<BookIssue> getBooksByMemberId(Long memberId) {
        return bookIssueRepository.findByMemberId(memberId);
    }
}
