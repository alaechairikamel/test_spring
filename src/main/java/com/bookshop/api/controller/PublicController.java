package com.bookshop.api.controller;

import com.bookshop.api.exception.ApiException;
import com.bookshop.api.repository.BookRepository;
import com.bookshop.api.repository.CategoryRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/public")
public class PublicController {

    private final CategoryRepository categoryRepository;
    private final BookRepository bookRepository;

    public PublicController(CategoryRepository categoryRepository, BookRepository bookRepository) {
        this.categoryRepository = categoryRepository;
        this.bookRepository = bookRepository;
    }

    @GetMapping("/categories")
    public Object categories() {
        return categoryRepository.findAll();
    }

    @GetMapping("/books")
    public Object books(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return bookRepository.findAll(PageRequest.of(page, size));
    }

    @GetMapping("/books/{id}")
    public Object bookById(@PathVariable Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ApiException("Book not found"));
    }
}
