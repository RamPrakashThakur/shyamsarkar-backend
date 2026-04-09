package com.shyamsarkar.buildingmaterials.service;

import java.util.List;
import java.util.Optional;

import com.shyamsarkar.buildingmaterials.dto.CustomerRequestDto;
import com.shyamsarkar.buildingmaterials.entity.Customer;

public interface CustomerService {

    Customer create(CustomerRequestDto dto);

    Customer getById(Long id);

    List<Customer> getAll();

    Customer update(Long id, CustomerRequestDto dto);

    void delete(Long id);

    Optional<Customer> getByEmail(String email);

    Customer save(Customer customer);

    void updateFcmToken(String email, String token);
}
