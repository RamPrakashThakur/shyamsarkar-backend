package com.shyamsarkar.buildingmaterials.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.shyamsarkar.buildingmaterials.dto.CustomerRequestDto;
import com.shyamsarkar.buildingmaterials.entity.Customer;
import com.shyamsarkar.buildingmaterials.entity.User;
import com.shyamsarkar.buildingmaterials.exception.BadRequestException;
import com.shyamsarkar.buildingmaterials.exception.ResourceNotFoundException;
import com.shyamsarkar.buildingmaterials.repository.CustomerRepository;
import com.shyamsarkar.buildingmaterials.repository.UserRepository;
import com.shyamsarkar.buildingmaterials.service.CustomerService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final UserRepository userRepository; 

    @Override
    
    public Customer create(CustomerRequestDto dto) {

        if (customerRepository.existsByPhone(dto.getPhone())) {
            throw new BadRequestException(
                "Customer already exists with phone " + dto.getPhone()
            );
        }

        // 🔥 USER MUST EXIST
        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() ->
                    new RuntimeException("User not found with email " + dto.getEmail())
                );

        Customer customer = new Customer();
        customer.setName(dto.getName());
        customer.setPhone(dto.getPhone());
        customer.setAddress(dto.getAddress());
        customer.setEmail(dto.getEmail());

        // ✅ LINK CUSTOMER WITH USER
        customer.setUser(user);

        return customerRepository.save(customer); // ✅ HERE
    }


    @Override
    public Customer getById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Customer not found with id " + id)
                );
    }

    @Override
    public List<Customer> getAll() {
        return customerRepository.findAll();
    }

    @Override
    public Customer update(Long id, CustomerRequestDto dto) {

        Customer customer = getById(id);

        customer.setName(dto.getName());
        customer.setAddress(dto.getAddress());

        return customerRepository.save(customer);
    }

    @Override
    public void delete(Long id) {

        if (!customerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Customer not found with id " + id);
        }

        customerRepository.deleteById(id);
    }

    public Optional<Customer> getByEmail(String email) {
        return customerRepository.findByEmail(email);
    }

    @Override
    public Customer save(Customer customer) {
        return customerRepository.save(customer);
    }

    public void updateFcmToken(String email, String token) {

    Customer customer = getByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));

    customer.setFcmToken(token);
    customerRepository.save(customer);
}
}
