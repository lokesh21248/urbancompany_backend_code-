package com.urbanservices.backend.service.impl;

import com.urbanservices.backend.domain.entity.Address;
import com.urbanservices.backend.domain.entity.Booking;
import com.urbanservices.backend.domain.entity.User;
import com.urbanservices.backend.domain.enums.Role;
import com.urbanservices.backend.domain.repository.AddressRepository;
import com.urbanservices.backend.domain.repository.BookingRepository;
import com.urbanservices.backend.domain.repository.UserRepository;
import com.urbanservices.backend.dto.AddressCreateRequest;
import com.urbanservices.backend.dto.AddressDTO;
import com.urbanservices.backend.dto.CustomerDTO;
import com.urbanservices.backend.service.CustomerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final BookingRepository bookingRepository;

    public CustomerServiceImpl(UserRepository userRepository,
                               AddressRepository addressRepository,
                               BookingRepository bookingRepository) {
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
        this.bookingRepository = bookingRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerDTO> getAllCustomers() {
        List<User> customers = userRepository.findByRole(Role.CUSTOMER);
        return customers.stream().map(this::mapCustomerToDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerDTO getCustomerById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with ID: " + id));
        return mapCustomerToDTO(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AddressDTO> getCustomerAddresses(Long customerId) {
        return addressRepository.findByUserId(customerId)
                .stream().map(this::mapAddressToDTO).collect(Collectors.toList());
    }

    @Override
    public AddressDTO addCustomerAddress(Long customerId, AddressCreateRequest request) {
        User user = userRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found with ID: " + customerId));

        Address address = new Address();
        address.setUser(user);
        address.setAddressLine1(request.getAddressLine1());
        address.setAddressLine2(request.getAddressLine2());
        address.setCity(request.getCity());
        address.setState(request.getState());
        address.setPostalCode(request.getPostalCode());
        address.setCountry(request.getCountry() != null ? request.getCountry() : "India");
        address.setLatitude(request.getLatitude());
        address.setLongitude(request.getLongitude());
        address.setLabel(request.getLabel() != null ? request.getLabel() : "OTHER");
        address.setIsDefault(request.getIsDefault() != null ? request.getIsDefault() : false);

        Address saved = addressRepository.save(address);
        return mapAddressToDTO(saved);
    }

    private CustomerDTO mapCustomerToDTO(User u) {
        CustomerDTO dto = new CustomerDTO();
        dto.setId(u.getId());
        dto.setFullName(u.getFullName());
        dto.setEmail(u.getEmail());
        dto.setPhoneNumber(u.getPhoneNumber());
        dto.setStatus(u.getStatus() != null ? u.getStatus().name() : "ACTIVE");
        dto.setCreatedAt(u.getCreatedAt());

        List<Booking> bookings = bookingRepository.findByCustomerIdOrderByScheduledTimeDesc(u.getId());
        dto.setTotalBookings(bookings.size());

        BigDecimal totalSpent = bookings.stream()
                .filter(b -> "COMPLETED".equals(b.getStatus() != null ? b.getStatus().name() : ""))
                .map(Booking::getFinalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        dto.setTotalSpent(totalSpent);

        List<Address> addresses = addressRepository.findByUserId(u.getId());
        dto.setAddresses(addresses.stream().map(this::mapAddressToDTO).collect(Collectors.toList()));

        return dto;
    }

    private AddressDTO mapAddressToDTO(Address a) {
        AddressDTO dto = new AddressDTO();
        dto.setId(a.getId());
        if (a.getUser() != null) dto.setUserId(a.getUser().getId());
        dto.setAddressLine1(a.getAddressLine1());
        dto.setAddressLine2(a.getAddressLine2());
        dto.setCity(a.getCity());
        dto.setState(a.getState());
        dto.setPostalCode(a.getPostalCode());
        dto.setCountry(a.getCountry());
        dto.setLatitude(a.getLatitude());
        dto.setLongitude(a.getLongitude());
        dto.setLabel(a.getLabel());
        dto.setIsDefault(a.getIsDefault());
        return dto;
    }
}
