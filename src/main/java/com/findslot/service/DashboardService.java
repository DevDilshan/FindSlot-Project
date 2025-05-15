package com.findslot.service;

import com.findslot.model.ParkingStats;
import com.findslot.model.Vehicle;
import com.findslot.util.FileStorageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DashboardService {
    private static final String VEHICLES_FILE = "data/vehicles.txt";
    private static final int TOTAL_CAPACITY = 50;

    private Map<String, Vehicle> parkedVehicles;

    @Autowired
    private ParkingSlotService slotService;

    @Autowired
    private CustomerService customerService;

    public DashboardService() {
        this.parkedVehicles = new HashMap<>();
    }

    @PostConstruct
    public void init() {
        loadVehiclesFromFile();
    }

    private void loadVehiclesFromFile() {
        List<Vehicle> vehicles = FileStorageUtil.readFromFile(VEHICLES_FILE);
        parkedVehicles.clear();
        for (Vehicle vehicle : vehicles) {
            parkedVehicles.put(vehicle.getNumber(), vehicle);
        }
    }

    private void saveVehiclesToFile() {
        List<Vehicle> vehicles = new ArrayList<>(parkedVehicles.values());
        FileStorageUtil.writeToFile(VEHICLES_FILE, vehicles);
    }

    public Vehicle parkVehicle(Vehicle vehicle) throws Exception {
        if (parkedVehicles.containsKey(vehicle.getNumber())) {
            throw new Exception("Vehicle is already parked");
        }

        if (parkedVehicles.size() >= TOTAL_CAPACITY) {
            throw new Exception("Parking is full");
        }

        int slotNumber = slotService.assignSlot(vehicle.getNumber());
        vehicle.setSlotNumber(slotNumber);
        vehicle.setEntryTime(new Date());

        parkedVehicles.put(vehicle.getNumber(), vehicle);
        saveVehiclesToFile();

        if (vehicle.getPhone() != null && !vehicle.getPhone().isEmpty()) {
            customerService.registerCustomer(vehicle.getPhone(), vehicle);
        }

        return vehicle;
    }

    public List<Vehicle> getAllVehicles() {
        return new ArrayList<>(parkedVehicles.values());
    }

    public List<Vehicle> getRecentVehicles(int count) {
        List<Vehicle> allVehicles = getAllVehicles();
        allVehicles.sort((v1, v2) -> v2.getEntryTime().compareTo(v1.getEntryTime()));

        if (allVehicles.size() <= count) {
            return allVehicles;
        } else {
            return allVehicles.subList(0, count);
        }
    }

    public ParkingStats getStats() {
        return new ParkingStats(TOTAL_CAPACITY, parkedVehicles.size());
    }

    public Vehicle getVehicle(String vehicleNumber) throws Exception {
        if (!parkedVehicles.containsKey(vehicleNumber)) {
            throw new Exception("Vehicle not found");
        }
        return parkedVehicles.get(vehicleNumber);
    }

    public void removeVehicleFromMap(String vehicleNumber) {
        parkedVehicles.remove(vehicleNumber);
        saveVehiclesToFile();
    }
}