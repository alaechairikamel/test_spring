package com.bookshop.api.controller;

import com.bookshop.api.dto.CreateBookRequest;
import com.bookshop.api.entity.Book;
import com.bookshop.api.exception.ApiException;
import com.bookshop.api.repository.BookRepository;
import com.bookshop.api.repository.CategoryRepository;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;

    public AdminController(BookRepository bookRepository, CategoryRepository categoryRepository) {
        this.bookRepository = bookRepository;
        this.categoryRepository = categoryRepository;
    }

    @PostMapping("/books")
    public Book createBook(@Valid @RequestBody CreateBookRequest request) {
        var category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new ApiException("Category not found"));

        Book book = new Book();
        book.setTitle(request.title());
        book.setAuthor(request.author());
        book.setPrice(request.price());
        book.setStock(request.stock());
        book.setDescription(request.description());
        book.setCategory(category);
        return bookRepository.save(book);
    }

    @DeleteMapping("/books/{id}")
    public void deleteBook(@PathVariable Long id) {
        if (!bookRepository.existsById(id)) {
            throw new ApiException("Book not found");
        }
        bookRepository.deleteById(id);
    }
}
