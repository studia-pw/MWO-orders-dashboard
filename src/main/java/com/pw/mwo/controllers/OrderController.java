package com.pw.mwo.controllers;

import com.pw.mwo.domain.Order;
import com.pw.mwo.services.ChangeOrderStatusRequest;
import com.pw.mwo.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrderController {
    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<List<Order>> getOrders() {
        return ResponseEntity.ok(orderService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable long id) {
        return ResponseEntity.ok(orderService.getOrder(id));
    }

    @PostMapping
    public ResponseEntity<Void> createOrder(@RequestBody Order order) {
        Order created = orderService.createOrder(order);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").build(created.getId());
        return ResponseEntity.created(location).build();
    }

    @PatchMapping
    public ResponseEntity<Order> updateOrder(@RequestBody ChangeOrderStatusRequest orderStatusRequest) {
        return ResponseEntity.ok(orderService.updateOrder(orderStatusRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }
}
