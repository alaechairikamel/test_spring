package com.bookshop.api.controller;

import com.bookshop.api.dto.AddCartItemRequest;
import com.bookshop.api.dto.UpdateCartItemRequest;
import com.bookshop.api.service.CartService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public Map<String, Object> getCart(Authentication auth) {
        return cartService.getCart(auth.getName());
    }

    @PostMapping("/items")
    public void addItem(Authentication auth, @Valid @RequestBody AddCartItemRequest request) {
        cartService.addItem(auth.getName(), request);
    }

    @PutMapping("/items/{itemId}")
    public void updateItem(Authentication auth, @PathVariable Long itemId, @Valid @RequestBody UpdateCartItemRequest request) {
        cartService.updateItem(auth.getName(), itemId, request);
    }

    @DeleteMapping("/items/{itemId}")
    public void deleteItem(Authentication auth, @PathVariable Long itemId) {
        cartService.deleteItem(auth.getName(), itemId);
    }
}
