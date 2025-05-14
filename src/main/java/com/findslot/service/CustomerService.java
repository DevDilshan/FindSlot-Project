// File: src/main/java/com/parking/service/CustomerService.java
package com.findslot.service;

import com.findslot.model.Customer;
import com.findslot.model.Vehicle;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// This service manages customer information
@Service
public class CustomerService {

    // Map to store customers (key: phone number, value: customer object)
    private Map<String, Customer> customers;

    // Constructor
    public CustomerService() {
        this.customers = new HashMap<>();
    }

    // Register a customer with a vehicle
    public void registerCustomer(String phone, Vehicle vehicle) {
        // Check if customer already exists
        if (customers.containsKey(phone)) {
            // Update existing customer
            Customer customer = customers.get(phone);
            customer.addVehicle(vehicle);
            customer.incrementVisitCount();
        } else {
            // Create new customer
            Customer customer = new Customer(phone);
            customer.addVehicle(vehicle);
            customer.incrementVisitCount();
            customers.put(phone, customer);
        }
    }

    // Get customer details
    public Customer getCustomer(String phone) {
        return customers.get(phone);
    }

    // Get all customer phone numbers
    public List<String> getAllCustomers() {
        return new ArrayList<>(customers.keySet());
    }

    // Get phone numbers of frequent customers (more than 3 visits)
    public List<String> getFrequentCustomers() {
        return customers.entrySet().stream()
                .filter(entry -> entry.getValue().getVisitCount() > 3)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }
}
