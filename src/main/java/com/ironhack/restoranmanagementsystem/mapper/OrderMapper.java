package com.ironhack.restoranmanagementsystem.mapper;

import com.ironhack.restoranmanagementsystem.dto.response.OrderItemResponse;
import com.ironhack.restoranmanagementsystem.dto.response.OrderResponse;
import com.ironhack.restoranmanagementsystem.dto.response.OrderSummary;
import com.ironhack.restoranmanagementsystem.entity.Order;
import com.ironhack.restoranmanagementsystem.entity.OrderItem;

import java.util.List;

public class OrderMapper {

    public static OrderResponse toResponse(Order order) {
        OrderResponse response = new OrderResponse();
        response.setId(order.getId());
        response.setCreatedAt(order.getCreatedAt());
        response.setTotalPrice(order.getTotalPrice());
        response.setStatus(order.getStatus());
        response.setUserId(order.getUser().getId());
        response.setUserFullName(order.getUser().getFullName());

        List<OrderItemResponse> items = order.getOrderItems() == null
                ? List.of()
                : order.getOrderItems().stream()
                .map(OrderMapper::toItemResponse)
                .toList();
        response.setOrderItems(items);

        return response;
    }

    public static OrderItemResponse toItemResponse(OrderItem item) {
        return new OrderItemResponse(
                item.getId(),
                item.getQuantity(),
                item.getUnitPrice(),
                item.getProduct().getId(),
                item.getProduct().getName()
        );
    }

    public static OrderSummary toSummary(Order order) {
        return new OrderSummary(
                order.getId(),
                order.getCreatedAt(),
                order.getTotalPrice(),
                order.getStatus(),
                order.getUser().getFullName()
        );
    }

    public static List<OrderResponse> toResponseList(List<Order> orders) {
        return orders.stream().map(OrderMapper::toResponse).toList();
    }

    public static List<OrderSummary> toSummaryList(List<Order> orders) {
        return orders.stream().map(OrderMapper::toSummary).toList();
    }
}