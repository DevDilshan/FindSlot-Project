package com.findslot.controller;

import com.findslot.service.ParkingSlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/slots")
public class ParkingSlotController {

    @Autowired
    private ParkingSlotService slotService;

    // Get all parking slots
    @GetMapping
    public ResponseEntity<List<ParkingSlot>> getAllSlots() {
        List<ParkingSlot> slots = slotService.getAllSlots();
        return ResponseEntity.ok(slots);
    }

    // Get available parking slots
    @GetMapping("/available")
    public ResponseEntity<List<ParkingSlot>> getAvailableSlots() {
        List<ParkingSlot> slots = slotService.getAvailableSlots();
        return ResponseEntity.ok(slots);
    }

    // Get occupied parking slots
    @GetMapping("/occupied")
    public ResponseEntity<List<ParkingSlot>> getOccupiedSlots() {
        List<ParkingSlot> slots = slotService.getOccupiedSlots();
        return ResponseEntity.ok(slots);
    }
}
