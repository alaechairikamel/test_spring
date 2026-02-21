package com.bookshop.api.repository;

import com.bookshop.api.entity.AppUser;
import com.bookshop.api.entity.Book;
import com.bookshop.api.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByUser(AppUser user);
    Optional<CartItem> findByUserAndBook(AppUser user, Book book);
}
