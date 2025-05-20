package com.example.lms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.lms.model.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    List findAll();
    Member findByName(String name);
    Member findByEmail(String email);
    Member findByMemberId(String memberId);
}
