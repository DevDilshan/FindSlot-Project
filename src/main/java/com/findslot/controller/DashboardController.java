package com.findslot.controller;


import com.findslot.model.ParkingStats;
import com.findslot.model.Vehicle;
import com.findslot.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    // Add a new vehicle
    @PostMapping("/vehicles")
    public ResponseEntity<?> addVehicle(@RequestBody Vehicle vehicle) {
        try {
            Vehicle parkedVehicle = dashboardService.parkVehicle(vehicle);
            return ResponseEntity.ok(parkedVehicle);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Get all parked vehicles
    @GetMapping("/vehicles")
    public ResponseEntity<List<Vehicle>> getAllVehicles() {
        List<Vehicle> vehicles = dashboardService.getAllVehicles();
        return ResponseEntity.ok(vehicles);
    }

    // Get recently parked vehicles
    @GetMapping("/vehicles/recent")
    public ResponseEntity<List<Vehicle>> getRecentVehicles() {
        List<Vehicle> vehicles = dashboardService.getRecentVehicles(5);
        return ResponseEntity.ok(vehicles);
    }

    // Get parking statistics
    @GetMapping("/vehicles/stats")
    public ResponseEntity<ParkingStats> getStats() {
        ParkingStats stats = dashboardService.getStats();
        return ResponseEntity.ok(stats);
    }
}