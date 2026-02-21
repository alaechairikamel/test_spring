package com.bookshop.api.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record CreateBookRequest(
        @NotBlank String title,
        @NotBlank String author,
        @NotNull @DecimalMin("0.0") BigDecimal price,
        @NotNull @Min(0) Integer stock,
        String description,
        @NotNull Long categoryId
) {}
