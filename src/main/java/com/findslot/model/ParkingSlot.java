package com.findslot.model;

import java.io.Serializable;

public class ParkingSlot implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;

    private boolean occupied;

    private String vehicleNumber;


    public ParkingSlot() {
    }

    public ParkingSlot(int id) {
        this.id = id;
        this.occupied = false;
        this.vehicleNumber = null;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }
}
