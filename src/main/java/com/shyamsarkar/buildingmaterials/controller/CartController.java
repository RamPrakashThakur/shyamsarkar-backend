package com.shyamsarkar.buildingmaterials.controller;



import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shyamsarkar.buildingmaterials.dto.AddToCartRequestDto;
import com.shyamsarkar.buildingmaterials.dto.CartResponseDto;
import com.shyamsarkar.buildingmaterials.dto.DeliveryLocationDto;
import com.shyamsarkar.buildingmaterials.dto.OrderRequestDto;
import com.shyamsarkar.buildingmaterials.dto.OrderResponseDto;
import com.shyamsarkar.buildingmaterials.entity.Cart;
import com.shyamsarkar.buildingmaterials.entity.Customer;

import com.shyamsarkar.buildingmaterials.service.CartService;
import com.shyamsarkar.buildingmaterials.service.CustomerService;


import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    // ✅ ADD TO CART (NO ADDRESS)
    @PostMapping("/add")
    public ResponseEntity<Cart> addToCart(
            @RequestBody AddToCartRequestDto request,
            Authentication authentication
    ) {

        Cart cart = cartService.addItem(
                authentication.getName(),
                request.getProductId(),
                request.getQuantity()
        );

        return ResponseEntity.ok(cart);
    }

    // ✅ REMOVE ITEM
    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<Cart> removeFromCart(
            @PathVariable Long productId,
            Authentication authentication
    ) {

        Cart cart = cartService.removeItem(
                authentication.getName(),
                productId
        );

        return ResponseEntity.ok(cart);
    }

    // ✅ VIEW CART
    @GetMapping
    public ResponseEntity<Cart> viewCart(Authentication authentication) {

        Cart cart = cartService.getCart(authentication.getName());
        return ResponseEntity.ok(cart);
    }

    // ✅ UPDATE QUANTITY
    @PutMapping("/update")
    public ResponseEntity<Cart> updateItemQuantity(
            @RequestParam Long productId,
            @RequestParam int quantity,
            Authentication authentication
    ) {

        Cart cart = cartService.updateCartItem(
                authentication.getName(),
                productId,
                quantity
        );

        return ResponseEntity.ok(cart);
    }

    // ✅ PREVIEW (FOR TRANSPORT COST)
    @PostMapping("/preview")
    public ResponseEntity<CartResponseDto> previewCart(
            @RequestBody DeliveryLocationDto location,
            Authentication authentication
    ) {

        CartResponseDto response = cartService.previewCart(
                authentication.getName(),
                location
        );

        return ResponseEntity.ok(response);
    }

    // ✅ FINAL CHECKOUT (ADDRESS + LAT LNG HERE ONLY)
    @PostMapping("/checkout")
    public ResponseEntity<OrderResponseDto> checkoutCart(
            @RequestBody OrderRequestDto dto,
            Authentication authentication
    ) {

        OrderResponseDto order = cartService.checkoutCart(
                authentication.getName(),
                dto
        );

        return ResponseEntity.ok(order);
    }
}