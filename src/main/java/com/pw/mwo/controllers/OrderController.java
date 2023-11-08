package com.pw.mwo.controllers;

import com.pw.mwo.domain.Client;
import com.pw.mwo.domain.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable long id) {
        return null;
    }

    @PostMapping
    public ResponseEntity<Void> createOrder() {
        return null;
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Order> updateOrder(@PathVariable long id) {
        return null;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable long id) {
        return null;
    }
}
