package com.example.lms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import com.example.lms.model.Faq;
import com.example.lms.service.FaqService;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequestMapping("/info")
public class FaqController {

    private final FaqService faqService;

    public FaqController(FaqService faqService) {
        this.faqService = faqService;
    }

    @GetMapping("/faq")
    public String showFAQ(Model model) {
        return loadFaqList(model, "info/faq");
    }

    @GetMapping("/list")
    public String showAllFAQs(Model model) {
        return loadFaqList(model, "info/list");
    }

    @GetMapping("/new")
    public String showAddForm(Model model) {
        model.addAttribute("faq", new Faq());
        return "info/new";
    }

    @PostMapping("/save")
    public String saveFaq(@ModelAttribute Faq faq) {
        faqService.saveFaq(faq);
        return "redirect:/info/list";
    }

    @GetMapping("/edit/{id}")
    public String showEditFaqForm(@PathVariable Long id, Model model) {
        return faqService.getFaqById(id)
                .map(faq -> {
                    model.addAttribute("faq", faq);
                    return "info/edit";
                })
                .orElse("redirect:/info/list");
    }

    @PostMapping("/update/{id}")
    public String updateFaq(@PathVariable Long id, @ModelAttribute Faq faq) {
        faq.setId(id);
        faqService.saveFaq(faq);
        return "redirect:/info/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteFaq(@PathVariable Long id) {
        faqService.deleteFaq(id);
        return "redirect:/info/list";
    }

    private String loadFaqList(Model model, String viewName) {
        model.addAttribute("faqs", faqService.getAllFaqs());
        return viewName;
    }
}
