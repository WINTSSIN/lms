package com.example.lms.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.lms.model.Gender;
import com.example.lms.model.Member;
import com.example.lms.model.Role;
import com.example.lms.repository.MemberRepository;
import com.example.lms.service.MemberService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/member")
public class MemberController {
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberService memberService;

    // @Autowired
    // private PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String loginPage() {
        return "index";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // Invalidate the session to log out
        return "redirect:/index?logout=true"; // Redirect to login page after logout
    }

    // @GetMapping("/list")
    // public String showMemberList(Model model) {
    //     List<Member> members = new ArrayList<>();
    //     members = memberService.getAllMembers();
    //     model.addAttribute("gender", Gender.values());
    //     model.addAttribute("role", Role.values());
    //     model.addAttribute("members", members);

    //     return "member/list";
    // }

    @GetMapping("/list")
    public String getAllMembers(
        @RequestParam(required = false) String sortField,
        @RequestParam(required = false) String sortDir,
        Model model) {

        if (sortField == null || sortField.isEmpty()) sortField = "name";
        if (sortDir == null || sortDir.isEmpty()) sortDir = "asc";

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                    Sort.by(sortField).ascending() :
                    Sort.by(sortField).descending();

        List<Member> members = memberRepository.findAll(sort);

        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

        model.addAttribute("gender", Gender.values());
        model.addAttribute("role", Role.values());
        model.addAttribute("members", members);

        return "member/list";
    }


    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("gender", Gender.values());
        model.addAttribute("role", Role.values());
        model.addAttribute("member", new Member());
        return "member/register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute Member member,  Model model) {
        if (memberRepository.findByName(member.getName()) != null) {
            model.addAttribute("error", "Name is already exists!");
            return "member/register";
        }
        if (memberRepository.findByEmail(member.getEmail()) != null) {
            model.addAttribute("error", "Email is already exists!");
            return "member/register";
        }
        // Encrypt password before saving
        // member.setPassword(passwordEncoder.encode(member.getPassword()));

        if (member.getRole() == null) {
            member.setRole(Role.USER);
          }

        memberRepository.save(member);
        model.addAttribute("error", "Member registered successfully");
        return "redirect:/member/list";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        try {
            // Attempt to fetch the member from the repository
            Member member = memberRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid Member ID: " + id));

            model.addAttribute("member", member);
            model.addAttribute("gender", Gender.values());
            return "member/edit";
        } catch (IllegalArgumentException ex) {
            return "redirect:/member/list";
        } catch (Exception ex) {
            return "error";
        }
    }

    @PostMapping("/update/{id}")
    public String updateMember(@PathVariable Long id, @ModelAttribute Member member) {
        member.setId(id);
        memberService.saveMember(member);
        return "redirect:/member/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id);
        return "redirect:/member/list";
    }

    @GetMapping("/user-edit")
    public String showUserEditForm(Model model, HttpSession session) {
        String username = (String) session.getAttribute("username");
        String role = (String) session.getAttribute("role");
        String memberId = (String) session.getAttribute("memberId");

        Optional<Member> memberOpt = memberService.getMemberInfoByMemberId(memberId);
        if (memberOpt.isPresent()) {
            Member member = memberOpt.get();
            model.addAttribute("name", username);
            model.addAttribute("role", role);
            model.addAttribute("memberId", memberId);
            model.addAttribute("member", member);
            return "member/user-edit";
        } else {
            return "redirect:/auth/profile";
        }
    }

    @PostMapping("/user-edit/{id}")
    public String updateUserInfo(@PathVariable Long id, @RequestParam("originalPassword") String originalPassword, @ModelAttribute Member member) {
        Optional<Member> existingOpt = memberService.getMemberById(id);
        if (existingOpt.isPresent()) {
            Member existing = existingOpt.get();
    
            existing.setName(member.getName());
            existing.setEmail(member.getEmail());
            existing.setFatherName(member.getFatherName());
            existing.setContactNumber(member.getContactNumber());
            existing.setAddress(member.getAddress());
            existing.setGender(member.getGender());

            if (existing.getPassword() == null || existing.getPassword().isBlank()) {
                existing.setPassword(originalPassword);
            } else {
                existing.setPassword(member.getPassword());
            }
    
            memberService.saveMember(existing);
            return "member/user-edit";
        } else {
            return "redirect:/auth/profile";
        }
    }
}