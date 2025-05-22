// File: src/main/java/com/parking/controller/VehicleController.java
package com.findslot.controller;

import com.findslot.model.ParkingRecord;
import com.findslot.model.Vehicle;
import com.findslot.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {

    @Autowired
    private VehicleService vehicleService;

    // Get details of a specific vehicle
    @GetMapping("/{number}")
    public ResponseEntity<?> getVehicle(@PathVariable String number) {
        try {
            Vehicle vehicle = vehicleService.getVehicle(number);
            return ResponseEntity.ok(vehicle);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Remove a vehicle (exit)
    @DeleteMapping("/{number}")
    public ResponseEntity<?> removeVehicle(@PathVariable String number) {
        try {
            ParkingRecord record = vehicleService.removeVehicle(number);
            return ResponseEntity.ok(record);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}