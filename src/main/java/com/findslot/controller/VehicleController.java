package com.findslot.controller;

import com.findslot.model.Vehicle;
import com.findslot.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/vehicles")
@CrossOrigin(origins = "*") // Allow requests from any origin for development
public class VehicleController {

    private final VehicleService vehicleService;

    @Autowired
    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @PostMapping
    public ResponseEntity<Vehicle> addVehicle(@RequestBody Vehicle vehicle) {
        try {
            Vehicle addedVehicle = vehicleService.addVehicle(vehicle);
            return new ResponseEntity<>(addedVehicle, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<List<Vehicle>> getAllVehicles() {
        return new ResponseEntity<>(vehicleService.getAllVehicles(), HttpStatus.OK);
    }

    @GetMapping("/sorted")
    public ResponseEntity<List<Vehicle>> getSortedVehicles() {
        return new ResponseEntity<>(vehicleService.getSortedVehicles(), HttpStatus.OK);
    }

    @GetMapping("/recent")
    public ResponseEntity<List<Vehicle>> getRecentVehicles() {
        return new ResponseEntity<>(vehicleService.getRecentVehicles(), HttpStatus.OK);
    }

    @GetMapping("/{number}")
    public ResponseEntity<Vehicle> getVehicleByNumber(@PathVariable String number) {
        Vehicle vehicle = vehicleService.getVehicleByNumber(number);
        if (vehicle != null) {
            return new ResponseEntity<>(vehicle, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{number}")
    public ResponseEntity<Vehicle> removeVehicle(@PathVariable String number) {
        try {
            Vehicle removedVehicle = vehicleService.removeVehicle(number);
            return new ResponseEntity<>(removedVehicle, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Integer>> getParkingStats() {
        Map<String, Integer> stats = new HashMap<>();
        stats.put("totalCapacity", vehicleService.getTotalCapacity());
        stats.put("parkedCount", vehicleService.getTotalCapacity() - vehicleService.getAvailableSpaces());
        stats.put("availableCount", vehicleService.getAvailableSpaces());

        return new ResponseEntity<>(stats, HttpStatus.OK);
    }
}
