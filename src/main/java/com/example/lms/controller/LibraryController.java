package com.example.lms.controller;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.lms.model.Book;
import com.example.lms.model.BookIssue;
import com.example.lms.model.BookReservation;
import com.example.lms.model.Member;
import com.example.lms.service.BookIssueService;
import com.example.lms.service.BookService;
import com.example.lms.service.LibraryService;
import com.example.lms.service.MemberService;

@Controller
@RequestMapping("/library")
public class LibraryController {
    @Autowired
    private LibraryService libraryService;

    @Autowired
    private BookService bookService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private BookIssueService bookIssueService;

    @GetMapping("/control")
    public String showTransactionForm(Model model) {
        List<Book> books = new ArrayList<>();
        books = bookService.getAllBooks();
        List<Member> members = new ArrayList<>();
        members = memberService.getAllMembers();
        List<BookIssue> bookIssues = new ArrayList<>();
        bookIssues = bookIssueService.getAllBookIssues();
        model.addAttribute("books", books);
        model.addAttribute("members", members);
        model.addAttribute("bookIssues", bookIssues);
        return "library/control";
    }

    @PostMapping("/controlBook")
    public String controlBook(@RequestParam Long bookId, @RequestParam Long memberId, @RequestParam String action,  Model model) {
        
        String message = "";
        try {
            switch(action) {
                case "issue":
                    message = libraryService.issueBook(bookId, memberId);
                    break;
                case "return":
                    message = libraryService.returnBook(bookId, memberId);
                    break;
                case "reserve":
                    message = libraryService.reserveBook(bookId, memberId);
                    break;
                default:
                    message = "Invalid action!";
            }
        } catch (RuntimeException e) {
            message = "Error: " + e.getMessage();
        }
        model.addAttribute("message", message);
        return "library/control";
    }

    @GetMapping("/issuedBooksList")
    public String getIssuedBooks(
        @RequestParam(required = false) String sortField,
        @RequestParam(required = false) String sortDir,
        Model model) {

        if (sortField == null || sortField.isEmpty()) sortField = "book.title";
        if (sortDir == null || sortDir.isEmpty()) sortDir = "asc";

        // Fix for nested property
        Sort sort = sortDir.equalsIgnoreCase("asc") ?
            Sort.by(sortField) :
            Sort.by(sortField).descending();

        List<BookIssue> issuedBooks = libraryService.getIssuedBooks(sort);

        model.addAttribute("issuedBooks", issuedBooks);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("issueSortDir", sortDir.equals("asc") ? "desc" : "asc");

        return "library/issuedBooksList";
    }



    @GetMapping("/reservedBooksList")
    public String getReservedBooks(
        @RequestParam(required = false) String sortField,
        @RequestParam(required = false) String sortDir,
        Model model) {
    
        if (sortField == null || sortField.isEmpty()) sortField = "title";
        if (sortDir == null || sortDir.isEmpty()) sortDir = "asc";
    
        Sort sort = sortDir.equalsIgnoreCase("asc") ?
            Sort.by(sortField).ascending() :
            Sort.by(sortField).descending();
    
        List<BookReservation> reservedBooks = libraryService.getReservedBooks(sort);
    
        model.addAttribute("reservedBooks", reservedBooks);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
    
        return "library/reservedBooksList";
    }
    
}