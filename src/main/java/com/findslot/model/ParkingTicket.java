package com.findslot.model;

import java.io.Serializable;
import java.util.Date;

public class ParkingTicket implements Serializable {

    private static final long serialVersionUID = 1L;

    private String ticketNumber;

    private String vehicleNumber;

    private String vehicleType;

    private String ownerName;

    private String ownerPhone;

    private Date entryTime;

    private Date issueTime;

    private int slotNumber;


    public ParkingTicket() {
        this.issueTime = new Date(); // Set current time as issue time
    }


    public ParkingTicket(String ticketNumber, String vehicleNumber, String vehicleType,
                         String ownerName, String ownerPhone, Date entryTime, int slotNumber) {
        this.ticketNumber = ticketNumber;
        this.vehicleNumber = vehicleNumber;
        this.vehicleType = vehicleType;
        this.ownerName = ownerName;
        this.ownerPhone = ownerPhone;
        this.entryTime = entryTime;
        this.slotNumber = slotNumber;
        this.issueTime = new Date(); // Set current time as issue time
    }

    public String getTicketNumber() {
        return ticketNumber;
    }

    public void setTicketNumber(String ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getOwnerPhone() {
        return ownerPhone;
    }

    public void setOwnerPhone(String ownerPhone) {
        this.ownerPhone = ownerPhone;
    }

    public Date getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(Date entryTime) {
        this.entryTime = entryTime;
    }

    public Date getIssueTime() {
        return issueTime;
    }

    public void setIssueTime(Date issueTime) {
        this.issueTime = issueTime;
    }

    public int getSlotNumber() {
        return slotNumber;
    }

    public void setSlotNumber(int slotNumber) {
        this.slotNumber = slotNumber;
    }
}