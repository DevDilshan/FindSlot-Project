package com.findslot.model;

import java.time.LocalDateTime;

public class Vehicle {
    private String number;
    private String type;
    private String owner;
    private String phone;
    private LocalDateTime entryTime;

    public Vehicle() {
        this.entryTime = LocalDateTime.now();
    }

    public Vehicle(String number, String type, String owner, String phone) {
        this.number = number;
        this.type = type;
        this.owner = owner;
        this.phone = phone;
        this.entryTime = LocalDateTime.now();
    }

    // Getters and setters
    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalDateTime getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(LocalDateTime entryTime) {
        this.entryTime = entryTime;
    }
}