package com.findslot.service;


import com.findslot.model.ParkingRecord;
import com.findslot.util.FileStorageUtil;
import com.findslot.util.QuickSort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ParkingHistoryService {
    private static final String HISTORY_FILE = "data/history.txt";

    private List<ParkingRecord> parkingRecords;

    @Autowired
    private QuickSort quickSort;

    public ParkingHistoryService() {
        this.parkingRecords = new ArrayList<>();
    }

    @PostConstruct
    public void init() {
        loadHistoryFromFile();
    }

    private void loadHistoryFromFile() {
        List<ParkingRecord> records = FileStorageUtil.readFromFile(HISTORY_FILE);
        if (records != null && !records.isEmpty()) {
            parkingRecords = records;
        }
    }

    private void saveHistoryToFile() {
        FileStorageUtil.writeToFile(HISTORY_FILE, parkingRecords);
    }

    public void addRecord(ParkingRecord record) {
        parkingRecords.add(record);

        quickSort.sort(parkingRecords);

        saveHistoryToFile();
    }

    public List<ParkingRecord> getAllRecords() {
        return parkingRecords;
    }

    public List<ParkingRecord> getRecordsByVehicle(String vehicleNumber) {
        return parkingRecords.stream()
                .filter(record -> record.getVehicleNumber().equals(vehicleNumber))
                .collect(Collectors.toList());
    }
}