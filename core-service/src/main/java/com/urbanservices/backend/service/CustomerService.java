package com.urbanservices.backend.service;

import com.urbanservices.backend.dto.AddressCreateRequest;
import com.urbanservices.backend.dto.AddressDTO;
import com.urbanservices.backend.dto.CustomerDTO;

import java.util.List;

public interface CustomerService {
    List<CustomerDTO> getAllCustomers();
    CustomerDTO getCustomerById(Long id);
    List<AddressDTO> getCustomerAddresses(Long customerId);
    AddressDTO addCustomerAddress(Long customerId, AddressCreateRequest request);
}
