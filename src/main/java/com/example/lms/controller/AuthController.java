package com.example.lms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.lms.dto.BookDetailsDTO;
import com.example.lms.model.BookIssue;
import com.example.lms.model.Member;
import com.example.lms.service.BookIssueService;
import com.example.lms.service.MemberService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/auth")
public class AuthController {
    
    @Autowired
    private MemberService memberService;

    @Autowired
    private BookIssueService bookIssueService;

    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String memberId, @RequestParam String password, HttpSession session, Model model) {
        Member member = memberService.authenticate(memberId, password);

        if (member != null) {
            session.setAttribute("username", member.getName());
            session.setAttribute("role", member.getRole().name());
            session.setAttribute("memberId", member.getMemberId());
            session.setAttribute("member", member);
            return "redirect:/auth/profile";
        } else {
            model.addAttribute("error", "Invalid Member ID or Password");
            return "auth/login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/auth/login";
    }

    @GetMapping("/profile")
    public String profilePage(HttpSession session, Model model) {
        String username = (String) session.getAttribute("username");
        String role = (String) session.getAttribute("role");
        String memberId = (String) session.getAttribute("memberId");
        Member member = (Member) session.getAttribute("member");

        List<BookIssue> books = bookIssueService.getBooksByMemberId(member.getId());
        List<BookDetailsDTO> bookDetails = memberService.getBookDetailsForMember(member.getId());

        model.addAttribute("memberId", memberId);
        model.addAttribute("username", username);
        model.addAttribute("role", role);
        model.addAttribute("books", books);
        model.addAttribute("bookDetails", bookDetails);
        return "auth/profile";
    }
}
