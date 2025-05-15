package com.findslot.model;

import java.io.Serializable;

public class ParkingStats implements Serializable {
    private static final long serialVersionUID = 1L;

    private int totalCapacity;
    private int parkedCount;
    private int availableCount;

    public ParkingStats() {
    }

    public ParkingStats(int totalCapacity, int parkedCount) {
        this.totalCapacity = totalCapacity;
        this.parkedCount = parkedCount;
        this.availableCount = totalCapacity - parkedCount;
    }

    // Getters and setters
    public int getTotalCapacity() { return totalCapacity; }
    public void setTotalCapacity(int totalCapacity) { this.totalCapacity = totalCapacity; }

    public int getParkedCount() { return parkedCount; }
    public void setParkedCount(int parkedCount) {
        this.parkedCount = parkedCount;
        this.availableCount = this.totalCapacity - parkedCount;
    }

    public int getAvailableCount() { return availableCount; }
    public void setAvailableCount(int availableCount) { this.availableCount = availableCount; }
}