package com.pw.mwo;

import com.pw.mwo.domain.Client;
import com.pw.mwo.domain.Order;
import com.pw.mwo.domain.OrderStatus;
import com.pw.mwo.domain.Product;
import com.pw.mwo.exceptions.ClientNotRegisteredException;
import com.pw.mwo.exceptions.EntityNotFoundException;
import com.pw.mwo.repositories.ClientRepository;
import com.pw.mwo.repositories.OrderRepository;
import com.pw.mwo.repositories.ProductRepository;
import com.pw.mwo.services.ChangeOrderStatusRequest;
import com.pw.mwo.services.OrderService;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private ProductRepository productRepository;
    private OrderService orderService;

    @BeforeEach
    void beforeEach() {
        this.orderService = new OrderService(
            orderRepository,
            clientRepository,
            productRepository
        );
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4})
    void Should_ThrowClientNotRegisteredException_When_CreateOrderInvokedWithOrderClientIdNotInRepository(long id) {
        // arrange
        when(clientRepository.existsById(id)).thenReturn(false);
        Client client = ClientFactory.makeClientCase1();
        client.setId(id);
        Order order = OrderFactory.makeOrderCase1(client, List.of());

        // act // assert
        assertThatExceptionOfType(ClientNotRegisteredException.class)
                .isThrownBy(() -> orderService.createOrder(order));
    }

    @ParameterizedTest
    @ValueSource(ints = {-10, -5, -1, 0})
    void Should_ThrowEntityNotFoundException_When_CreateOrderInvokedWithProductNotAvailable(long quantity) {
        // arrange
        Product product = ProductFactory.makeProductCase3();
        product.setAvailability(quantity);
        Client client = ClientFactory.makeClientCase1();
        when(productRepository.findAllById(any(Iterable.class))).thenReturn(List.of(product));
        when(clientRepository.existsById(client.getId())).thenReturn(true);
        Order order = OrderFactory.makeOrderCase1(client, List.of(product));

        // act // assert
        assertThatExceptionOfType(EntityNotFoundException.class)
                .isThrownBy(() -> orderService.createOrder(order));
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4})
    void Should_CreateNewOrder_When_OrderDetailsAreValid(long number) {
        // arrange
        Client client = ClientFactory.makeClientCase3();
        Product product = ProductFactory.makeProductCase3();
        client.setId(1L);
        product.setId(1L);
        product.setAvailability(number);
        Order order = OrderFactory.makeOrderCase2(client, List.of(product));
        when(clientRepository.existsById(client.getId())).thenReturn(true);
        when(productRepository.findAllById(any(Iterable.class))).thenReturn(List.of(product));

        // act
        orderService.createOrder(order);

        // assert
        verify(orderRepository).save(order);
    }

    @Test
    void Should_CallFindById_When_GetOrderInvoked() {
        // arrange
        long id = 1;
        when(orderRepository.findById(id)).thenReturn(Optional.of(new Order()));

        // act
        orderService.getOrder(id);

        // assert
        verify(orderRepository).findById(id);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3})
    void Should_ThrowEntityNotFound_When_OrderWithIdNotFound(long id) {
        // arrange
        when(orderRepository.findById(id)).thenReturn(Optional.empty());

        // act
        ThrowableAssert.ThrowingCallable throwingCallable = () -> orderService.getOrder(id);

        // assert
        assertThatExceptionOfType(EntityNotFoundException.class).isThrownBy(throwingCallable);
    }

    @ParameterizedTest
    @MethodSource("providerMethod")
    void Should_ThrowEntityNotFound_When_UpdateOrderInvokedWithOrderNotInRepository(ChangeOrderStatusRequest request) {
        // arrange
        when(orderRepository.existsById(request.id())).thenReturn(false);

        // act
        ThrowableAssert.ThrowingCallable callable = () -> orderService.updateOrder(request);

        // assert
        assertThatExceptionOfType(EntityNotFoundException.class).isThrownBy(callable);
    }

    @ParameterizedTest
    @MethodSource("providerMethod")
    void Should_UpdateState_When_UpdateOrderInvokedWithOrderInRepository(ChangeOrderStatusRequest request) {
        // arrange
        when(orderRepository.existsById(request.id())).thenReturn(true);
        when(orderRepository.findById(request.id())).thenReturn(Optional.of(new Order()));

        // act
        orderService.updateOrder(request);

        // assert
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    static Stream<ChangeOrderStatusRequest> providerMethod() {
        return Stream.of(
                new ChangeOrderStatusRequest(1L, OrderStatus.NEW),
                new ChangeOrderStatusRequest(2L, OrderStatus.IN_PROGRESS),
                new ChangeOrderStatusRequest(3L, OrderStatus.COMPLETED)
        );
    }
}
