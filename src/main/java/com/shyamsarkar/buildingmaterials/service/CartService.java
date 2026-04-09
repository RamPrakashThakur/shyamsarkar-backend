package com.shyamsarkar.buildingmaterials.service;

import com.shyamsarkar.buildingmaterials.dto.CartResponseDto;
import com.shyamsarkar.buildingmaterials.dto.DeliveryLocationDto;
import com.shyamsarkar.buildingmaterials.dto.OrderRequestDto;
import com.shyamsarkar.buildingmaterials.dto.OrderResponseDto;
import com.shyamsarkar.buildingmaterials.entity.Cart;
import com.shyamsarkar.buildingmaterials.entity.Customer;
public interface CartService {

    Cart addItem(String email, Long productId, int quantity);

    Cart removeItem(String email, Long productId);

    Cart getCart(String email);

    Cart updateCartItem(String email, Long productId, int quantity);

    void clearCart(Customer customer);

    CartResponseDto previewCart(String email, DeliveryLocationDto location);

    OrderResponseDto checkoutCart(String email, OrderRequestDto dto);
}