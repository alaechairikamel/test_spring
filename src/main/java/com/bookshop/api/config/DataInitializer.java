package com.bookshop.api.config;

import com.bookshop.api.entity.*;
import com.bookshop.api.repository.AppUserRepository;
import com.bookshop.api.repository.BookRepository;
import com.bookshop.api.repository.CategoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner seedData(CategoryRepository categoryRepository,
                               BookRepository bookRepository,
                               AppUserRepository appUserRepository,
                               PasswordEncoder encoder) {
        return args -> {
            if (categoryRepository.count() == 0) {
                Category dev = new Category();
                dev.setName("Développement");
                Category ops = new Category();
                ops.setName("DevOps");
                categoryRepository.save(dev);
                categoryRepository.save(ops);

                Book b1 = new Book();
                b1.setTitle("Spring in Action");
                b1.setAuthor("Craig Walls");
                b1.setPrice(new BigDecimal("49.90"));
                b1.setStock(20);
                b1.setDescription("Spring Boot fundamentals");
                b1.setCategory(dev);

                Book b2 = new Book();
                b2.setTitle("The DevOps Handbook");
                b2.setAuthor("Gene Kim");
                b2.setPrice(new BigDecimal("39.00"));
                b2.setStock(15);
                b2.setDescription("Pratiques DevOps modernes");
                b2.setCategory(ops);

                bookRepository.save(b1);
                bookRepository.save(b2);
            }

            if (appUserRepository.count() == 0) {
                AppUser admin = new AppUser();
                admin.setEmail("admin@bookshop.com");
                admin.setPasswordHash(encoder.encode("admin123"));
                admin.setRole(Role.ROLE_ADMIN);

                AppUser user = new AppUser();
                user.setEmail("user@bookshop.com");
                user.setPasswordHash(encoder.encode("user123"));
                user.setRole(Role.ROLE_USER);

                appUserRepository.save(admin);
                appUserRepository.save(user);
            }
        };
    }
}
