package com.example.lms.service;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.lms.dto.BookDetailsDTO;
import com.example.lms.model.BookIssue;
import com.example.lms.model.BookReservation;
import com.example.lms.model.Member;
import com.example.lms.repository.BookIssueRepository;
import com.example.lms.repository.BookReservationRepository;
import com.example.lms.repository.MemberRepository;

@Service
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BookIssueRepository bookIssueRepository;

    @Autowired
    private BookReservationRepository bookReservationRepository;

    @Autowired
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    public Optional<Member> getMemberById(Long id) {
        return memberRepository.findById(id);
    }

    public Optional<Member> getMemberInfoById(Long id) {
        return memberRepository.findById(id);
    }
    
    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }

    public Member authenticate(String memberId, String password) {
        Member member = memberRepository.findByMemberId(memberId);
        if (member != null && password != null && password.equals(member.getPassword())) {
            return member;
        }
        return null;
    }

    public List<BookDetailsDTO> getBookDetailsForMember(Long memberId) {
        List<BookDetailsDTO> bookDetailsList = new ArrayList<>();
        List<BookIssue> bookIssues = bookIssueRepository.findByMemberId(memberId);
        for (BookIssue bookIssue : bookIssues) {
            BookDetailsDTO bookDetails = new BookDetailsDTO();
            bookDetails.setBookTitle(bookIssue.getBook().getTitle());
            bookDetails.setBookAuthor(bookIssue.getBook().getAuthor());
            bookDetails.setIssueDate(bookIssue.getIssueDate());
            bookDetails.setDueDate(bookIssue.getDueDate());
            bookDetails.setReturnDate(bookIssue.getReturnDate());   
            bookDetailsList.add(bookDetails);
        }
       
        List<BookReservation> bookReservations = bookReservationRepository.findByMemberId(memberId);
        for (BookReservation bookReservation : bookReservations) {
            BookDetailsDTO bookDetails = new BookDetailsDTO();
            bookDetails.setBookTitle(bookReservation.getBook().getTitle());
            bookDetails.setBookAuthor(bookReservation.getBook().getAuthor());
            bookDetails.setReservationDate(bookReservation.getReservationDate());
            bookDetails.setStatus(bookReservation.getStatus());
            bookDetailsList.add(bookDetails);
        }

        return bookDetailsList;
    }

    public Member saveMember(Member member) {
        if (member.getCreated() == null) {
            member.setCreated(LocalDateTime.now());
        }
        return memberRepository.save(member);
    }

    public Optional<Member> getMemberInfoByMemberId(String memberId) {
        return Optional.ofNullable(memberRepository.findByMemberId(memberId));
    }

}
