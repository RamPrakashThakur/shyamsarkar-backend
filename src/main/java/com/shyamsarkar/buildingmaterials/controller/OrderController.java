package com.shyamsarkar.buildingmaterials.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


import com.shyamsarkar.buildingmaterials.dto.OrderRequestDto;
import com.shyamsarkar.buildingmaterials.dto.OrderResponseDto;
import com.shyamsarkar.buildingmaterials.dto.OrderStatusUpdateDto;
import com.shyamsarkar.buildingmaterials.entity.Order;
import com.shyamsarkar.buildingmaterials.service.OrderService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    // CREATE ORDER
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponseDto createOrder(
        @RequestBody OrderRequestDto dto,
        Authentication authentication
    ) {
            return OrderResponseDto.from(
            orderService.createOrder(dto, authentication.getName())
    );
      }

    // GET ORDER BY ID
    @GetMapping("/{id}")
    public OrderResponseDto getOrder(@PathVariable Long id) {
        return OrderResponseDto.from(orderService.getById(id));
    }

    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<OrderResponseDto> cancelOrder(@PathVariable Long orderId) {

        Order order = orderService.cancelOrder(orderId);
        return ResponseEntity.ok(OrderResponseDto.from(order));
    }



    @PutMapping("/{orderId}/status")
    public OrderResponseDto updateOrderStatus(
            @PathVariable Long orderId,
            @RequestBody OrderStatusUpdateDto dto
    ) {
        return orderService.updateOrderStatus(orderId, dto.getStatus());
    }

    @GetMapping("/my")
public List<OrderResponseDto> getMyOrders(Authentication authentication) {

    return orderService
            .getOrdersForCustomer(authentication.getName())
            .stream()
            .map(OrderResponseDto::from)
            .toList();
}
@GetMapping
@PreAuthorize("hasRole('ADMIN')")
public List<OrderResponseDto> getAllOrders() {

    return orderService.getAllOrders()
            .stream()
            .map(OrderResponseDto::from)
            .toList();
}
    @PutMapping("/{id}/delivered")
@PreAuthorize("hasRole('DRIVER')")
public ResponseEntity<Void> markDelivered(
        @PathVariable Long id
) {
    orderService.markDelivered(id);
    return ResponseEntity.ok().build();
}
}

