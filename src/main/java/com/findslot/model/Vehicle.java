package com.findslot.model;

import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class Vehicle {
    private String vehicleNumber;
    private String vehicleType;
    private LocalTime entryTime;
    private LocalTime exitTime;
    private double fee;
    private int slotNumber;

    public Vehicle() {
    }

    public Vehicle(String vehicleNumber, String vehicleType, LocalTime entryTime, LocalTime exitTime, double fee, int slotNumber) {
        this.vehicleNumber = vehicleNumber;
        this.vehicleType = vehicleType;
        this.entryTime = entryTime;
        this.exitTime = exitTime;
        this.fee = fee;
        this.slotNumber  = slotNumber;
    }

    // Getters and setters
    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleNumber) {
        this.vehicleType = vehicleType;
    }

    public LocalTime getEntryTime () {
        return entryTime;
    }

    public void setEntryTime (LocalTime entryTime) {
        this.entryTime = entryTime;
    }

    public LocalTime getExitTime () {
        return exitTime;
    }

    public void setExitTime (LocalTime exitTime) {
        this.exitTime = exitTime;
    }

    public double getFee () {
        return fee;
    }

    public void setFee (double fee) {
        this.fee = fee;
    }

    public int getSlotNumber () {
        return slotNumber;
    }

    public void setSlotNumber (int slotNumber) {
        this.slotNumber = slotNumber;
    }
}