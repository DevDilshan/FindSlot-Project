package com.findslot.model;

import java.io.Serializable;
import java.util.Date;

public class Vehicle implements Serializable {
    private static final long serialVersionUID = 1L;

    private String number;
    private String type;
    private String owner;
    private String phone;
    private Date entryTime;
    private int slotNumber;

    public Vehicle() {
        this.entryTime = new Date();
    }

    public Vehicle(String number, String type, String owner, String phone) {
        this.number = number;
        this.type = type;
        this.owner = owner;
        this.phone = phone;
        this.entryTime = new Date();
    }

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

    public Date getEntryTime() {
        return entryTime;
    }
    public void setEntryTime(Date entryTime) {
        this.entryTime = entryTime;
    }

    public int getSlotNumber() {
        return slotNumber;
    }
    public void setSlotNumber(int slotNumber) {
        this.slotNumber = slotNumber;
    }
}