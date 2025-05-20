package com.example.lms.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
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
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.lms.model.Book;
import com.example.lms.model.Genre;
import com.example.lms.repository.BookRepository;
import com.example.lms.service.BookService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/book")
public class BookController {

    @Autowired
    private final BookService bookService;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/list")
    public String showAllBooks(
        @RequestParam(required = false) String sortField,
        @RequestParam(required = false) String sortDir,
        HttpSession session, Model model) {

        if (sortField == null || sortField.isEmpty()) sortField = "title";
        if (sortDir == null || sortDir.isEmpty()) sortDir = "asc";

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                    Sort.by(sortField).ascending() :
                    Sort.by(sortField).descending();

        
        List<Book> books = bookRepository.findAll(sort);

        String role = (String) session.getAttribute("role");
        String username = (String) session.getAttribute("username");

        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        model.addAttribute("genres", Genre.values());
        model.addAttribute("books", books);
        model.addAttribute("role", role);
        model.addAttribute("username", username);
        return "book/list";
    }

    @GetMapping("/list-admin")
    public String getAllBooksSorted(
        @RequestParam(required = false) String sortField,
        @RequestParam(required = false) String sortDir,
        Model model,
        HttpSession session) {

        if (sortField == null || sortField.isEmpty()) sortField = "title";
        if (sortDir == null || sortDir.isEmpty()) sortDir = "asc";

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                    Sort.by(sortField).ascending() :
                    Sort.by(sortField).descending();

        List<Book> books = bookRepository.findAll(sort);
    
        model.addAttribute("books", books);
        model.addAttribute("genres", Genre.values());

        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

        model.addAttribute("role", session.getAttribute("role"));
        model.addAttribute("username", session.getAttribute("username"));

        return "book/list-admin";
    }

    private void listBooks(Model model, String title, String author, Genre genre, Boolean available) {
        model.addAttribute("genres", Genre.values());
        model.addAttribute("books", bookService.searchBooks(title, author, genre, available));
        model.addAttribute("searchTitle", title);
        model.addAttribute("searchAuthor", author);
        model.addAttribute("searchGenre", genre);
        model.addAttribute("searchAvailable", available);
    }

    @PostMapping("/list")
    public String searchBooks(Model model,
                              @RequestParam(required = false) String title,
                              @RequestParam(required = false) String author,
                              @RequestParam(required = false) Genre genre,
                              @RequestParam(required = false) Boolean available) {
        listBooks(model, title, author, genre, available);
        return "book/list";
    }

    @PostMapping("/list-admin")
    public String searchBook(Model model,
                              @RequestParam(required = false) String title,
                              @RequestParam(required = false) String author,
                              @RequestParam(required = false) Genre genre,
                              @RequestParam(required = false) Boolean available) {
        listBooks(model, title, author, genre, available);
        return "book/list-admin";
    }

    @GetMapping("/new")
    public String showAddBookForm(Model model) {
        model.addAttribute("genres", Genre.values());
        model.addAttribute("book", new Book());
        return "book/new";
    }

    @PostMapping("/save")
    public String saveBook(@ModelAttribute Book book, @RequestParam("image") MultipartFile image, RedirectAttributes redirectAttributes) {
        try {
            if (image != null && !image.isEmpty()) {
                String imageName = System.currentTimeMillis() + "_" + image.getOriginalFilename();

                Path uploadDir = Paths.get(System.getProperty("user.dir"), "uploads");
                Path path = uploadDir.resolve(imageName);

                Files.createDirectories(path.getParent());
                image.transferTo(path.toFile());
                book.setImagePath("/uploads/" + imageName);
                book.setImageName(imageName);
            }

            bookService.saveBook(book);
            return "redirect:/book/list-admin";
        } catch (IOException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Error saving book image: " + e.getMessage());
            return "redirect:/error";
        } catch (MaxUploadSizeExceededException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "File size exceeds the allowed limit.");
            return "redirect:/book/new";
        }
    }

    @GetMapping("/edit/{id}")
    public String showEditBookForm(@PathVariable Long id, Model model) {
        Optional<Book> book = bookService.getBookById(id);
        if (book.isPresent()) {
            model.addAttribute("book", book.get());
            model.addAttribute("genres", Genre.values());
            return "book/edit";
        } else {
            return "redirect:/book/list";
        }
    }


    @PostMapping("/update/{id}")
    public String updateBook(@PathVariable Long id,
                            @ModelAttribute Book book,
                            @RequestParam("image") MultipartFile image,
                            RedirectAttributes redirectAttributes) {

        try {
            Book existingBook = bookService.getBookInfoById(id);

            if (image != null && !image.isEmpty()) {
                String imageName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
                Path uploadDir = Paths.get(System.getProperty("user.dir"), "uploads");
                Files.createDirectories(uploadDir);
                Path imagePath = uploadDir.resolve(imageName);
                image.transferTo(imagePath.toFile());

                book.setImagePath("/uploads/" + imageName);
                book.setImageName(imageName);
            } else {
                book.setImagePath(existingBook.getImagePath());
                book.setImageName(existingBook.getImageName());
            }

            book.setCreated(existingBook.getCreated());
            book.setUpdated(LocalDateTime.now());
            bookService.saveBook(book);

            return "redirect:/book/list-admin";

        } catch (IOException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Image upload failed: " + e.getMessage());
            return "redirect:/error";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return "redirect:/book/list-admin";
    }
}
