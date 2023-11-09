package com.pw.mwo.services;

import com.pw.mwo.domain.Order;
import com.pw.mwo.domain.Product;
import com.pw.mwo.exceptions.ClientNotRegisteredException;
import com.pw.mwo.exceptions.EntityNotFoundException;
import com.pw.mwo.repositories.ClientRepository;
import com.pw.mwo.repositories.OrderRepository;
import com.pw.mwo.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final ClientRepository clientRepository;
    private final ProductRepository productRepository;

    @Transactional
    public Order createOrder(Order order) {
        if (!clientRepository.existsById(order.getClient().getId())) {
            throw new ClientNotRegisteredException();
        }

        List<Product> products = productRepository
                .findAllById(order.getProductList().stream().map(Product::getId).collect(Collectors.toList()));

        if (products.size() != order.getProductList().size()) {
            throw new EntityNotFoundException();
        } else {
            for (Product product : products) {
               if (product.getAvailability() <= 0) {
                   throw new EntityNotFoundException();
               }
            }
        }

        order.setId(null);
        return orderRepository.save(order);
    }

    @Transactional
    public void deleteOrder(long id) {
        if (!orderRepository.existsById(id)) {
            throw new EntityNotFoundException();
        }

        Order order = getOrder(id);
        List<Product> products = order.getProductList();
        Map<Long, Long> map = countProducts(products);

        for (Long ID : map.keySet()) {
            Product product = productRepository.findById(ID).orElseThrow(EntityNotFoundException::new);
            product.setAvailability(product.getAvailability() + map.get(ID));
            productRepository.save(product);
        }

        orderRepository.deleteById(id);
    }

    @Transactional
    public Order updateOrder(ChangeOrderStatusRequest orderStatusRequest) {
        if (!orderRepository.existsById(orderStatusRequest.id())) {
            throw new EntityNotFoundException();
        }

        Order order = getOrder(orderStatusRequest.id());
        order.setOrderStatus(orderStatusRequest.orderStatus());
        return orderRepository.save(order);
    }

    @Transactional(readOnly = true)
    public Order getOrder(long id) {
        return orderRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    private Map<Long, Long> countProducts(List<Product> products) {
        return products.stream().collect(groupingBy(Product::getId, counting()));
    }

    @Transactional(readOnly = true)
    public List<Order> getAll() {
        return orderRepository.findAll();
    }
}
