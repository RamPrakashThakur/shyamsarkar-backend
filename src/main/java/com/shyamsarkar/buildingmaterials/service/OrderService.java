package com.shyamsarkar.buildingmaterials.service;

import java.util.List;

import com.shyamsarkar.buildingmaterials.dto.OrderResponseDto;
import com.shyamsarkar.buildingmaterials.entity.Order;
import com.shyamsarkar.buildingmaterials.entity.OrderStatus;

public interface OrderService {
    Order createOrder(com.shyamsarkar.buildingmaterials.dto.OrderRequestDto dto, String email);
    Order getById(Long id);
    Order cancelOrder(Long orderId);
    OrderResponseDto updateOrderStatus(Long orderId, OrderStatus status);
    // CUSTOMER ORDERS
    List<Order> getOrdersForCustomer(String email);
    //ADMIN
    List<Order> getAllOrders();

    void markDelivered(Long orderId);
}

