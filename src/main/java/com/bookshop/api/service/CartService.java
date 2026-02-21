package com.bookshop.api.service;

import com.bookshop.api.dto.AddCartItemRequest;
import com.bookshop.api.dto.UpdateCartItemRequest;
import com.bookshop.api.entity.AppUser;
import com.bookshop.api.entity.CartItem;
import com.bookshop.api.exception.ApiException;
import com.bookshop.api.repository.AppUserRepository;
import com.bookshop.api.repository.BookRepository;
import com.bookshop.api.repository.CartItemRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
public class CartService {

    private final AppUserRepository appUserRepository;
    private final BookRepository bookRepository;
    private final CartItemRepository cartItemRepository;

    public CartService(AppUserRepository appUserRepository, BookRepository bookRepository, CartItemRepository cartItemRepository) {
        this.appUserRepository = appUserRepository;
        this.bookRepository = bookRepository;
        this.cartItemRepository = cartItemRepository;
    }

    public Map<String, Object> getCart(String email) {
        AppUser user = getUserByEmail(email);
        List<CartItem> items = cartItemRepository.findByUser(user);
        var mapped = items.stream().map(i -> Map.of(
                "itemId", i.getId(),
                "bookId", i.getBook().getId(),
                "title", i.getBook().getTitle(),
                "quantity", i.getQuantity(),
                "unitPrice", i.getUnitPrice(),
                "lineTotal", i.getUnitPrice().multiply(BigDecimal.valueOf(i.getQuantity()))
        )).toList();
        BigDecimal total = items.stream()
                .map(i -> i.getUnitPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return Map.of("items", mapped, "total", total);
    }

    public void addItem(String email, AddCartItemRequest request) {
        AppUser user = getUserByEmail(email);
        var book = bookRepository.findById(request.bookId()).orElseThrow(() -> new ApiException("Book not found"));
        if (book.getStock() < request.quantity()) {
            throw new ApiException("Not enough stock");
        }

        CartItem item = cartItemRepository.findByUserAndBook(user, book).orElse(new CartItem());
        item.setUser(user);
        item.setBook(book);
        item.setQuantity((item.getQuantity() == null ? 0 : item.getQuantity()) + request.quantity());
        item.setUnitPrice(book.getPrice());
        cartItemRepository.save(item);
    }

    public void updateItem(String email, Long itemId, UpdateCartItemRequest request) {
        CartItem item = getOwnedItem(email, itemId);
        if (item.getBook().getStock() < request.quantity()) {
            throw new ApiException("Not enough stock");
        }
        item.setQuantity(request.quantity());
        cartItemRepository.save(item);
    }

    public void deleteItem(String email, Long itemId) {
        CartItem item = getOwnedItem(email, itemId);
        cartItemRepository.delete(item);
    }

    private CartItem getOwnedItem(String email, Long itemId) {
        AppUser user = getUserByEmail(email);
        CartItem item = cartItemRepository.findById(itemId).orElseThrow(() -> new ApiException("Cart item not found"));
        if (!item.getUser().getId().equals(user.getId())) {
            throw new ApiException("Forbidden cart access");
        }
        return item;
    }

    private AppUser getUserByEmail(String email) {
        return appUserRepository.findByEmail(email).orElseThrow(() -> new ApiException("User not found"));
    }
}
