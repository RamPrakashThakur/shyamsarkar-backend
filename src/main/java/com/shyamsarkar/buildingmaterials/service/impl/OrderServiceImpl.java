package com.shyamsarkar.buildingmaterials.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shyamsarkar.buildingmaterials.dto.DistanceResult;
import com.shyamsarkar.buildingmaterials.dto.OrderRequestDto;
import com.shyamsarkar.buildingmaterials.dto.OrderResponseDto;
import com.shyamsarkar.buildingmaterials.entity.Cart;
import com.shyamsarkar.buildingmaterials.entity.CartItem;
import com.shyamsarkar.buildingmaterials.entity.Customer;
import com.shyamsarkar.buildingmaterials.entity.Order;
import com.shyamsarkar.buildingmaterials.entity.OrderItem;
import com.shyamsarkar.buildingmaterials.entity.OrderStatus;
import com.shyamsarkar.buildingmaterials.entity.Product;
import com.shyamsarkar.buildingmaterials.exception.BadRequestException;
import com.shyamsarkar.buildingmaterials.exception.ResourceNotFoundException;
import com.shyamsarkar.buildingmaterials.repository.OrderRepository;
import com.shyamsarkar.buildingmaterials.repository.ProductRepository;
import com.shyamsarkar.buildingmaterials.service.CartService;
import com.shyamsarkar.buildingmaterials.service.CustomerService;
import com.shyamsarkar.buildingmaterials.service.GoogleMapsService;
import com.shyamsarkar.buildingmaterials.service.InventoryLogService;
import com.shyamsarkar.buildingmaterials.service.NotificationService;
import com.shyamsarkar.buildingmaterials.service.OrderService;



import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

    private final NotificationService notificationService;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final InventoryLogService inventoryLogService;
    private final CustomerService customerService;
    private final CartService cartService;
    private final GoogleMapsService googleMapsService;
    private static final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);


    @Value("${warehouse.lat}")
    private double warehouseLat;

    @Value("${warehouse.lng}")
    private double warehouseLng;
    

    // ================= CREATE ORDER =================
    @Override
@Transactional
public Order createOrder(OrderRequestDto dto, String email) {

    Customer customer = customerService.getByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

    // ✅ GET CART
    Cart cart = cartService.getCart(email);

    if (cart.getItems().isEmpty()) {
        throw new BadRequestException("Cart is empty");
    }

    // ✅ GET DISTANCE FROM GOOGLE MAPS
    DistanceResult distanceResult = googleMapsService.calculateDistance(
            warehouseLat,
            warehouseLng,
            dto.getDeliveryLat(),
            dto.getDeliveryLng()
    );

    double distanceKm = distanceResult.getDistanceKm();

    Order order = new Order();
    order.setCustomer(customer);
    order.setDeliveryAddress(dto.getDeliveryAddress());
    order.setDeliveryDistanceKm(distanceKm);
    order.setCreatedAt(LocalDateTime.now());
    order.setStatus(OrderStatus.CREATED);

    double itemsTotal = 0;
    double deliveryCharge = 0;
    List<OrderItem> orderItems = new ArrayList<>();

    // 🔥 CART → ORDER + TRANSPORT COST
    for (CartItem cartItem : cart.getItems()) {

        Product product = cartItem.getProduct();
        int quantity = cartItem.getQuantity();

            log.info("Product = {}", product.getName());
            log.info("Price = {}", product.getPrice());
            log.info("Quantity = {}", cartItem.getQuantity());
            log.info("TransportCostPerKm = {}", product.getTransportCostPerKm());

        if (product.getQuantity() < quantity) {
            throw new BadRequestException(
                    "Insufficient stock for " + product.getName()
            );
        }

        // ✅ STOCK DEDUCTION
        product.setQuantity(product.getQuantity() - quantity);
        productRepository.save(product);

        inventoryLogService.log(product, -quantity, "ORDER_PLACED");

        // ✅ ORDER ITEM
        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setProduct(product);
        orderItem.setQuantity(quantity);
        orderItem.setPriceAtPurchase(product.getPrice());
        orderItem.setPrice(product.getPrice());
        orderItems.add(orderItem);

        // 💰 ITEM TOTAL
        itemsTotal += product.getPrice() * quantity;

        // 🚚 TRANSPORT COST (KEY LOGIC)
        deliveryCharge += product.getTransportCostPerKm()
                          * quantity
                          * distanceKm;
        log.info("ItemTotal = {}", itemsTotal);
        log.info("Transport = {}", deliveryCharge);
    }

    order.setItems(orderItems);
    order.setItemsAmount(itemsTotal);
    order.setDeliveryCharge(deliveryCharge);
    order.setTotalAmount(itemsTotal + deliveryCharge);

    log.info("ItemsTotal FINAL = {}", itemsTotal);
    log.info("DeliveryCharge FINAL = {}", deliveryCharge);
    log.info("GrandTotal FINAL = {}", (itemsTotal + deliveryCharge));

log.info("========== ORDER DEBUG END ==========");
    order.setStatus(OrderStatus.PLACED);

    Order savedOrder = orderRepository.save(order);

    // ✅ CLEAR CART AFTER SUCCESS
    cartService.clearCart(customer);

    return savedOrder;
}

    // ================= GET ORDER =================
    @Override
    public Order getById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Order not found with id " + id
                        )
                );
    }

    // ================= CANCEL ORDER =================
    @Override
    public Order cancelOrder(Long orderId) {

        Order order = getById(orderId);

        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new BadRequestException("Order already cancelled");
        }

        if (order.getStatus() == OrderStatus.DELIVERED) {
            throw new BadRequestException("Delivered order cannot be cancelled");
        }

        // ✅ RESTORE STOCK
        for (OrderItem item : order.getItems()) {
            Product product = item.getProduct();
            product.setQuantity(product.getQuantity() + item.getQuantity());
            productRepository.save(product);


            inventoryLogService.log(
                product,
                item.getQuantity(),   // ➕ positive = stock added back
                "ORDER_CANCELLED"
            );
        }

        order.setStatus(OrderStatus.CANCELLED);
        return orderRepository.save(order);
    }

    // ================= UPDATE STATUS =================
@Override
@Transactional
public OrderResponseDto updateOrderStatus(Long orderId, OrderStatus newStatus) {

    Order order = getById(orderId);
    OrderStatus currentStatus = order.getStatus();

    if (currentStatus == OrderStatus.CANCELLED) {
        throw new BadRequestException("Cancelled order cannot be updated");
    }

    if (currentStatus == OrderStatus.DELIVERED) {
        throw new BadRequestException("Delivered order cannot be updated");
    }

    // ❌ Prevent backward transitions
    if (currentStatus == OrderStatus.CONFIRMED && newStatus == OrderStatus.PLACED) {
        throw new BadRequestException("Invalid status transition");
    }

    if (currentStatus == OrderStatus.OUT_FOR_DELIVERY &&
        newStatus != OrderStatus.DELIVERED) {
        throw new BadRequestException("Driver can only mark order as DELIVERED");
    }

    if (currentStatus == newStatus) {
        throw new BadRequestException("Order already in status " + newStatus);
    }

    order.setStatus(newStatus);
    return OrderResponseDto.from(orderRepository.save(order));
}

// ================= CUSTOMER ORDERS =================
@Override
public List<Order> getOrdersForCustomer(String email) {

    Customer customer = customerService
            .getByEmail(email)
            .orElseThrow(() ->
                    new ResourceNotFoundException(
                            "Customer not found with email: " + email
                    )
            );

    return orderRepository.findByCustomer(customer);
}

// ================= ADMIN =================

@Override
public List<Order> getAllOrders() {
    return orderRepository.findAll();
}
@Override
public void markDelivered(Long orderId) {

    Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new RuntimeException("Order not found"));

    order.setStatus(OrderStatus.DELIVERED);

    orderRepository.save(order);

    // 🔥 SEND FCM
    notificationService.sendDeliveredNotification(
            order.getCustomer().getFcmToken()
    );
}
}
