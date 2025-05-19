// File: src/main/java/com/parking/model/ParkingRecord.java
package com.findslot.model;

import java.io.Serializable;
import java.util.Date;

public class ParkingRecord implements Serializable {
    private static final long serialVersionUID = 1L;

    private String vehicleNumber;
    private String vehicleType;
    private Date entryTime;
    private Date exitTime;
    private double fee;
    private int slotNumber;

    public ParkingRecord() {
    }

    public ParkingRecord(String vehicleNumber, String vehicleType, Date entryTime,
                         Date exitTime, double fee, int slotNumber) {
        this.vehicleNumber = vehicleNumber;
        this.vehicleType = vehicleType;
        this.entryTime = entryTime;
        this.exitTime = exitTime;
        this.fee = fee;
        this.slotNumber = slotNumber;
    }

    // Getters and setters
    public String getVehicleNumber() { return vehicleNumber; }
    public void setVehicleNumber(String vehicleNumber) { this.vehicleNumber = vehicleNumber; }

    public String getVehicleType() { return vehicleType; }
    public void setVehicleType(String vehicleType) { this.vehicleType = vehicleType; }

    public Date getEntryTime() { return entryTime; }
    public void setEntryTime(Date entryTime) { this.entryTime = entryTime; }

    public Date getExitTime() { return exitTime; }
    public void setExitTime(Date exitTime) { this.exitTime = exitTime; }

    public double getFee() { return fee; }
    public void setFee(double fee) { this.fee = fee; }

    public int getSlotNumber() { return slotNumber; }
    public void setSlotNumber(int slotNumber) { this.slotNumber = slotNumber; }
}