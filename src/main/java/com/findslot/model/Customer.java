package com.findslot.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Customer implements Serializable {
    private static final long serialVersionUID = 1L;

    private String phone;
    private int visitCount;
    private List<Vehicle> vehicles;

    public Customer() {
        this.vehicles = new ArrayList<>();
        this.visitCount = 0;
    }

    public Customer(String phone) {
        this.phone = phone;
        this.vehicles = new ArrayList<>();
        this.visitCount = 0;
    }

    // Getters and setters
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public int getVisitCount() { return visitCount; }
    public void setVisitCount(int visitCount) { this.visitCount = visitCount; }

    public void incrementVisitCount() {
        this.visitCount++;
    }

    public List<Vehicle> getVehicles() { return vehicles; }
    public void setVehicles(List<Vehicle> vehicles) { this.vehicles = vehicles; }

    public void addVehicle(Vehicle vehicle) {
        this.vehicles.add(vehicle);
    }
}