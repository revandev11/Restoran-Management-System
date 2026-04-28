package com.ironhack.restoranmanagementsystem.service;

import com.ironhack.restoranmanagementsystem.dto.request.OrderCreateRequest;
import com.ironhack.restoranmanagementsystem.dto.request.OrderItemAddRequest;
import com.ironhack.restoranmanagementsystem.dto.response.OrderResponse;
import com.ironhack.restoranmanagementsystem.dto.response.OrderSummary;
import com.ironhack.restoranmanagementsystem.entity.Order;
import com.ironhack.restoranmanagementsystem.entity.OrderItem;
import com.ironhack.restoranmanagementsystem.entity.Product;
import com.ironhack.restoranmanagementsystem.entity.User;
import com.ironhack.restoranmanagementsystem.enums.OrderStatus;
import com.ironhack.restoranmanagementsystem.exception.BadRequestException;
import com.ironhack.restoranmanagementsystem.exception.ForbiddenException;
import com.ironhack.restoranmanagementsystem.exception.ResourceNotFoundException;
import com.ironhack.restoranmanagementsystem.mapper.OrderMapper;
import com.ironhack.restoranmanagementsystem.repository.OrderItemRepository;
import com.ironhack.restoranmanagementsystem.repository.OrderRepository;
import com.ironhack.restoranmanagementsystem.repository.ProductRepository;
import com.ironhack.restoranmanagementsystem.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public OrderService(OrderRepository orderRepository,
                        OrderItemRepository orderItemRepository,
                        ProductRepository productRepository,
                        UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public OrderResponse createOrder(OrderCreateRequest request, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Order order = new Order(LocalDateTime.now(), BigDecimal.ZERO, OrderStatus.PENDING, user);
        order.setOrderItems(new ArrayList<>());
        Order savedOrder = orderRepository.save(order);

        for (OrderItemAddRequest itemRequest : request.getItems()) {
            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + itemRequest.getProductId()));

            if (!product.getAvailable()) {
                throw new BadRequestException("Product is not available: " + product.getName());
            }

            OrderItem item = new OrderItem(itemRequest.getQuantity(), product.getPrice(), savedOrder, product);
            orderItemRepository.save(item);
        }

        recalculateTotalPrice(savedOrder);
        return OrderMapper.toResponse(orderRepository.findById(savedOrder.getId()).get());
    }

    @Transactional
    public OrderResponse addItem(Long orderId, OrderItemAddRequest request, String email) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found: " + orderId));

        if (!order.getUser().getEmail().equals(email)) {
            throw new ForbiddenException("You can only modify your own orders");
        }

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new BadRequestException("Cannot modify order with status: " + order.getStatus());
        }

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + request.getProductId()));

        if (!product.getAvailable()) {
            throw new BadRequestException("Product is not available: " + product.getName());
        }

        OrderItem item = new OrderItem(request.getQuantity(), product.getPrice(), order, product);
        orderItemRepository.save(item);
        recalculateTotalPrice(order);

        return OrderMapper.toResponse(orderRepository.findById(orderId).get());
    }

    @Transactional
    public OrderResponse updateStatus(Long orderId, OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found: " + orderId));
        order.setStatus(newStatus);
        return OrderMapper.toResponse(orderRepository.save(order));
    }

    public List<OrderSummary> getAll() {
        return OrderMapper.toSummaryList(orderRepository.findAll());
    }

    public OrderResponse getById(Long orderId, String email, boolean isAdmin) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found: " + orderId));

        if (!isAdmin && !order.getUser().getEmail().equals(email)) {
            throw new ForbiddenException("Access denied");
        }
        return OrderMapper.toResponse(order);
    }

    private void recalculateTotalPrice(Order order) {
        List<OrderItem> items = orderItemRepository.findByOrderId(order.getId());
        BigDecimal total = items.stream()
                .map(i -> i.getUnitPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setTotalPrice(total);
        orderRepository.save(order);
    }
}