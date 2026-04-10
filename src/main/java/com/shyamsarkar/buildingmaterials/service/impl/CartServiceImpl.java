package com.shyamsarkar.buildingmaterials.service.impl;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shyamsarkar.buildingmaterials.dto.CartItemResponseDto;
import com.shyamsarkar.buildingmaterials.dto.CartResponseDto;
import com.shyamsarkar.buildingmaterials.dto.DeliveryLocationDto;
import com.shyamsarkar.buildingmaterials.dto.OrderRequestDto;
import com.shyamsarkar.buildingmaterials.dto.OrderResponseDto;

import com.shyamsarkar.buildingmaterials.entity.Cart;
import com.shyamsarkar.buildingmaterials.entity.CartItem;
import com.shyamsarkar.buildingmaterials.entity.Customer;
import com.shyamsarkar.buildingmaterials.entity.Order;
import com.shyamsarkar.buildingmaterials.entity.OrderItem;
import com.shyamsarkar.buildingmaterials.entity.OrderStatus;
import com.shyamsarkar.buildingmaterials.entity.Product;

import com.shyamsarkar.buildingmaterials.repository.CartRepository;
import com.shyamsarkar.buildingmaterials.repository.OrderRepository;

import com.shyamsarkar.buildingmaterials.service.CartService;
import com.shyamsarkar.buildingmaterials.service.CustomerService;
import com.shyamsarkar.buildingmaterials.service.GoogleMapsService;
import com.shyamsarkar.buildingmaterials.service.ProductService;

import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
@Transactional
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CustomerService customerService;
    private final ProductService productService;
    private final OrderRepository orderRepository;
    private final GoogleMapsService googleMapsService;

    
    private double WAREHOUSE_LAT=24.491042;

    @Value("${warehouse.lng}")
    private double WAREHOUSE_LNG;

    // ✅ ADD ITEM (NO ADDRESS)
    @Override
    public Cart addItem(String email, Long productId, int quantity) {

        Customer customer = customerService.getByEmail(email)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        Cart cart = cartRepository.findByCustomer(customer)
                .orElseGet(() -> {
                    Cart c = new Cart();
                    c.setCustomer(customer);
                    return cartRepository.save(c);
                });

        Product product = productService.getProductById(productId);

        cart.getItems().stream()
                .filter(i -> i.getProduct().getId().equals(productId))
                .findFirst()
                .ifPresentOrElse(
                        item -> item.setQuantity(item.getQuantity() + quantity),
                        () -> {
                            CartItem item = new CartItem();
                            item.setCart(cart);
                            item.setProduct(product);
                            item.setQuantity(quantity);
                            cart.getItems().add(item);
                        }
                );

        return cartRepository.save(cart);
    }

    // ✅ REMOVE
    @Override
    public Cart removeItem(String email, Long productId) {
        Cart cart = getCart(email);

        cart.getItems().removeIf(item ->
                item.getProduct().getId().equals(productId)
        );

        return cartRepository.save(cart);
    }

    // ✅ GET CART
    @Override
    public Cart getCart(String email) {
        Customer customer = customerService.getByEmail(email)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        return cartRepository.findByCustomer(customer)
                .orElseThrow(() -> new RuntimeException("Cart empty"));
    }

    // ✅ UPDATE
    @Override
    public Cart updateCartItem(String email, Long productId, int quantity) {

        Cart cart = getCart(email);

        CartItem item = cart.getItems().stream()
                .filter(i -> i.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Item not found"));

        if (quantity <= 0) {
            cart.getItems().remove(item);
        } else {
            item.setQuantity(quantity);
        }

        return cartRepository.save(cart);
    }

    // ✅ CLEAR
    @Override
    public void clearCart(Customer customer) {
        cartRepository.findByCustomer(customer)
                .ifPresent(cart -> {
                    cart.getItems().clear();
                    cartRepository.save(cart);
                });
    }

    // ✅ PREVIEW CART (transport calc)
    @Override
    public CartResponseDto previewCart(
            String email,
            DeliveryLocationDto location
    ) {

        Cart cart = getCart(email);

        double distanceKm = googleMapsService.calculateDistance(
                WAREHOUSE_LAT,
                WAREHOUSE_LNG,
                location.getLatitude(),
                location.getLongitude()
        ).getDistanceKm();

        double itemsTotal = 0;
        double transportTotal = 0;

        List<CartItemResponseDto> items = new ArrayList<>();

        for (CartItem item : cart.getItems()) {

            Product p = item.getProduct();

            double itemTotal = p.getPrice() * item.getQuantity();

            double transport =
                    p.getTransportCostPerKm()
                    * distanceKm;

            itemsTotal += itemTotal;
            transportTotal += transport;

            CartItemResponseDto dto = new CartItemResponseDto();
            dto.setProductId(p.getId());
            dto.setProductName(p.getName());
            dto.setQuantity(item.getQuantity());
            dto.setPrice(p.getPrice());
            dto.setItemTotal(itemTotal);
            dto.setTransportCost(transport);
            dto.setImageUrl(p.getImageUrl());

            items.add(dto);
        }

        CartResponseDto res = new CartResponseDto();
        res.setItems(items);
        res.setItemsTotal(itemsTotal);
        res.setTransportationCost(transportTotal);
        res.setGrandTotal(itemsTotal + transportTotal);

        return res;
    }

    // ✅ FINAL CHECKOUT (ADDRESS HERE ONLY)
    @Override
    public OrderResponseDto checkoutCart(String email, OrderRequestDto dto) {

        Customer customer = customerService.getByEmail(email)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        Cart cart = getCart(email);

        double distanceKm = googleMapsService.calculateDistance(
                WAREHOUSE_LAT,
                WAREHOUSE_LNG,
                dto.getDeliveryLat(),
                dto.getDeliveryLng()
        ).getDistanceKm();

        Order order = new Order();
        order.setCustomer(customer);
        order.setDeliveryAddress(dto.getDeliveryAddress());
        order.setCreatedAt(java.time.LocalDateTime.now());
        order.setStatus(OrderStatus.PLACED);

        double itemsTotal = 0;
        double transportTotal = 0;

        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItem c : cart.getItems()) {

            Product p = c.getProduct();

            if (p.getQuantity() < c.getQuantity()) {
        throw new RuntimeException(
                "Not enough stock for " + p.getName()
        );
    }

    // ✅ STOCK DEDUCT
    p.setQuantity(p.getQuantity() - c.getQuantity());

    // ✅ SAVE PRODUCT (IMPORTANT)
    productService.save(p); // 👈 ADD THIS METHOD (see below)

            OrderItem oi = new OrderItem();
            oi.setOrder(order);
            oi.setProduct(p);
            oi.setQuantity(c.getQuantity());
            oi.setPrice(p.getPrice());

            orderItems.add(oi);

            itemsTotal += p.getPrice() * c.getQuantity();
            transportTotal += p.getTransportCostPerKm()
                              * distanceKm;
        }

        order.setItems(orderItems);
        order.setTotalAmount(itemsTotal + transportTotal);

        Order saved = orderRepository.save(order);

        cart.getItems().clear();
        cartRepository.save(cart);

        return OrderResponseDto.from(saved);
    }
}