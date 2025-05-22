// File: src/main/java/com/parking/controller/CustomerController.java
package com.findslot.controller;


import com.findslot.model.Customer;
import com.findslot.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    // Get all customers
    @GetMapping
    public ResponseEntity<List<String>> getAllCustomers() {
        List<String> customers = customerService.getAllCustomers();
        return ResponseEntity.ok(customers);
    }

    // Get details of a specific customer
    @GetMapping("/{phone}")
    public ResponseEntity<?> getCustomer(@PathVariable String phone) {
        Customer customer = customerService.getCustomer(phone);
        if (customer == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer not found");
        }
        return ResponseEntity.ok(customer);
    }

    // Get frequent customers
    @GetMapping("/frequent")
    public ResponseEntity<List<String>> getFrequentCustomers() {
        List<String> customers = customerService.getFrequentCustomers();
        return ResponseEntity.ok(customers);
    }
}