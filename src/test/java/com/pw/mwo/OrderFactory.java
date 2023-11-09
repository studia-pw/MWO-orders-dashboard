package com.pw.mwo;

import com.pw.mwo.domain.Client;
import com.pw.mwo.domain.Order;
import com.pw.mwo.domain.OrderStatus;
import com.pw.mwo.domain.Product;

import java.util.List;

public class OrderFactory {
    static Order makeOrderCase1(Client client, List<Product> productList) {
        return Order.builder()
                .client(client)
                .productList(productList)
                .orderStatus(OrderStatus.NEW)
                .build();
    }

    static Order makeOrderCase2(Client client, List<Product> productList) {
        return Order.builder()
                .client(client)
                .productList(productList)
                .orderStatus(OrderStatus.IN_PROGRESS)
                .build();
    }

    static Order makeOrderCase3(Client client, List<Product> productList) {
        return Order.builder()
                .client(client)
                .productList(productList)
                .orderStatus(OrderStatus.COMPLETED)
                .build();
    }
}
