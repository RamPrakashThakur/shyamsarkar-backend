package com.shyamsarkar.buildingmaterials.controller;

import com.shyamsarkar.buildingmaterials.dto.UserResponseDto;
import com.shyamsarkar.buildingmaterials.entity.Order;
import com.shyamsarkar.buildingmaterials.entity.OrderStatus;
import com.shyamsarkar.buildingmaterials.entity.Role;
import com.shyamsarkar.buildingmaterials.entity.User;
import com.shyamsarkar.buildingmaterials.repository.OrderRepository;
import com.shyamsarkar.buildingmaterials.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    @PutMapping("/orders/{orderId}/assign-driver/{driverId}")
    public Order assignDriver(
            @PathVariable Long orderId,
            @PathVariable Long driverId
        ) {
        Order order = orderRepository.findById(orderId).orElseThrow();
        User driver = userRepository.findById(driverId).orElseThrow();

        order.setDriver(driver);
        if (order.getStatus() == OrderStatus.PLACED) {
            order.setStatus(OrderStatus.CONFIRMED);
        }

        return orderRepository.save(order);
    }

    @GetMapping("/drivers")
public List<UserResponseDto> getAllDrivers() {

    System.out.println("===== ADMIN: FETCHING ALL DRIVERS =====");

    List<User> drivers = userRepository.findByRole(Role.DRIVER);

    System.out.println("Drivers count = " + drivers.size());

    drivers.forEach(d ->
            System.out.println("Driver -> ID: " + d.getId()
                    + ", Name: " + d.getName()
                    + ", Role: " + d.getRole())
    );

    return drivers.stream()
            .map(UserResponseDto::from)
            .toList();
}

}
