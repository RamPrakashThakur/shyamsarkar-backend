package com.shyamsarkar.buildingmaterials.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shyamsarkar.buildingmaterials.entity.Customer;
import com.shyamsarkar.buildingmaterials.entity.Order;
import com.shyamsarkar.buildingmaterials.entity.OrderStatus;
import com.shyamsarkar.buildingmaterials.entity.User;


public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByCustomer(Customer customer);
    List<Order> findByStatus(OrderStatus status);
    List<Order> findByDriver(User driver);
}
