package com.urbanservices.backend.controller;

import com.urbanservices.backend.dto.AddressCreateRequest;
import com.urbanservices.backend.dto.AddressDTO;
import com.urbanservices.backend.dto.CustomerDTO;
import com.urbanservices.backend.service.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
@CrossOrigin(origins = "*")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public ResponseEntity<List<CustomerDTO>> getAllCustomers() {
        return ResponseEntity.ok(customerService.getAllCustomers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDTO> getCustomerById(@PathVariable Long id) {
        return ResponseEntity.ok(customerService.getCustomerById(id));
    }

    @GetMapping("/{id}/addresses")
    public ResponseEntity<List<AddressDTO>> getCustomerAddresses(@PathVariable Long id) {
        return ResponseEntity.ok(customerService.getCustomerAddresses(id));
    }

    @PostMapping("/{id}/addresses")
    public ResponseEntity<AddressDTO> addCustomerAddress(
            @PathVariable Long id,
            @RequestBody AddressCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(customerService.addCustomerAddress(id, request));
    }
}
