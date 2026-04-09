package com.shyamsarkar.buildingmaterials.controller;

import com.shyamsarkar.buildingmaterials.dto.OrderResponseDto;
import com.shyamsarkar.buildingmaterials.entity.Order;
import com.shyamsarkar.buildingmaterials.entity.OrderStatus;
import com.shyamsarkar.buildingmaterials.entity.User;
import com.shyamsarkar.buildingmaterials.repository.OrderRepository;
import com.shyamsarkar.buildingmaterials.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/driver")
@RequiredArgsConstructor
@PreAuthorize("hasRole('DRIVER')") // 🔥 DRIVER ONLY (class-level)
public class DriverController {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    // ===============================
    // 1️⃣ DRIVER KE ASSIGNED ORDERS
    // ===============================
    @GetMapping("/orders")
    public List<OrderResponseDto> getMyOrders(
            @AuthenticationPrincipal UserDetails userDetails) {

        User driver = userRepository
                .findByEmail(userDetails.getUsername())
                .orElseThrow();

        return orderRepository.findByDriver(driver)
                .stream()
                .map(OrderResponseDto::from)
                .toList();
    }

    // ===============================
    // 2️⃣ SINGLE ORDER DETAIL
    // ===============================
    @GetMapping("/orders/{orderId}")
    public OrderResponseDto getOrder(@PathVariable Long orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow();

        return OrderResponseDto.from(order);
    }

    // ===============================
    // 3️⃣ START DELIVERY
    // ===============================
    @PostMapping("/orders/{orderId}/start")
public ResponseEntity<?> startDelivery(
        @PathVariable Long orderId,
        @AuthenticationPrincipal UserDetails userDetails
) {

    // 🔹 Logged-in driver
    User driver = userRepository
            .findByEmail(userDetails.getUsername())
            .orElseThrow(() -> new RuntimeException("Driver not found"));

    // 🔹 Order
    Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new RuntimeException("Order not found"));

    // 🔒 Safety check
    if (order.getStatus() != OrderStatus.CONFIRMED) {
        return ResponseEntity
                .badRequest()
                .body("Order not ready. Current status = " + order.getStatus());
    }

    // ✅ SET DRIVER
    order.setDriver(driver);

    // ✅ UPDATE STATUS
    order.setStatus(OrderStatus.OUT_FOR_DELIVERY);

    orderRepository.save(order);

    return ResponseEntity.ok().build();
}
}