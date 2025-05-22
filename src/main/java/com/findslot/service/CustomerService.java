// File: src/main/java/com/parking/service/CustomerService.java
package com.findslot.service;

import com.findslot.model.Customer;
import com.findslot.model.Vehicle;
import com.findslot.util.FileStorageUtil;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CustomerService {
    private static final String CUSTOMERS_FILE = "data/customers.txt";

    private Map<String, Customer> customers;

    public CustomerService() {
        this.customers = new HashMap<>();
    }

    @PostConstruct
    public void init() {
        loadCustomersFromFile();
    }

    private void loadCustomersFromFile() {
        List<Customer> customerList = FileStorageUtil.readFromFile(CUSTOMERS_FILE);
        customers.clear();
        for (Customer customer : customerList) {
            customers.put(customer.getPhone(), customer);
        }
    }

    private void saveCustomersToFile() {
        List<Customer> customerList = new ArrayList<>(customers.values());
        FileStorageUtil.writeToFile(CUSTOMERS_FILE, customerList);
    }

    public void registerCustomer(String phone, Vehicle vehicle) {
        if (customers.containsKey(phone)) {
            Customer customer = customers.get(phone);
            customer.addVehicle(vehicle);
            customer.incrementVisitCount();
        } else {
            Customer customer = new Customer(phone);
            customer.addVehicle(vehicle);
            customer.incrementVisitCount();
            customers.put(phone, customer);
        }

        saveCustomersToFile();
    }

    public Customer getCustomer(String phone) {
        return customers.get(phone);
    }

    public List<String> getAllCustomers() {
        return new ArrayList<>(customers.keySet());
    }

    public List<String> getFrequentCustomers() {
        return customers.entrySet().stream()
                .filter(entry -> entry.getValue().getVisitCount() > 1)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }
}