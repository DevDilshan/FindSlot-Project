package com.findslot.controller;


import com.findslot.model.ParkingRecord;
import com.findslot.service.ParkingHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/history")
public class ParkingHistoryController {

    @Autowired
    private ParkingHistoryService historyService;

    // Get all parking records
    @GetMapping
    public ResponseEntity<List<ParkingRecord>> getAllRecords() {
        List<ParkingRecord> records = historyService.getAllRecords();
        return ResponseEntity.ok(records);
    }

    // Get parking history for a specific vehicle
    @GetMapping("/vehicle/{number}")
    public ResponseEntity<List<ParkingRecord>> getRecordsByVehicle(@PathVariable String number) {
        List<ParkingRecord> records = historyService.getRecordsByVehicle(number);
        return ResponseEntity.ok(records);
    }
}