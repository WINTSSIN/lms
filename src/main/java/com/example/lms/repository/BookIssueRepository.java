package com.example.lms.repository;

import com.example.lms.model.BookIssue;
import com.example.lms.model.Member;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookIssueRepository extends JpaRepository<BookIssue, Long> {
    List<BookIssue> findByMemberAndReturnDateIsNull(Member member);
    List<BookIssue> findByMemberId(Long memberId);
    List<BookIssue> findAll(Sort sort);
}
