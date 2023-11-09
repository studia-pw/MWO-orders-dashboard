package com.pw.mwo;

import com.pw.mwo.domain.Product;

import java.math.BigDecimal;

public class ProductFactory {

    static Product makeProductCase1() {
        return Product.builder()
                .name("Apples")
                .price(new BigDecimal("1.99"))
                .availability(100L)
                .build();
    }

    static Product makeProductCase2() {
        return Product.builder()
                .name("Milk")
                .price(new BigDecimal("2.49"))
                .availability(50L)
                .build();
    }

    static Product makeProductCase3() {
        return Product.builder()
                .name("Bread")
                .price(new BigDecimal("1.29"))
                .availability(75L)
                .build();
    }}
