package com.findslot.service;

import com.findslot.model.ParkingSlot;
import com.findslot.util.FileStorageUtil;

import com.findslot.util.ParkingStack;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
com.findslot.util.ParkingStack;

@Service
public class ParkingSlotService {
    private static final String SLOTS_FILE = "data/slots.txt";
    private static final String AVAILABLE_SLOTS_FILE = "data/available_slots.txt";
    private static final int TOTAL_CAPACITY = 50;

    private ParkingStack availableSlots;
    private Map<Integer, ParkingSlot> allSlots;

    public ParkingSlotService() {
        this.availableSlots = new ParkingStack();
        this.allSlots = new HashMap<>();
    }

    @PostConstruct
    public void initialize() {
        loadSlotsFromFile();

        if (allSlots.isEmpty()) {
            availableSlots.initialize(TOTAL_CAPACITY);

            for (int i = 1; i <= TOTAL_CAPACITY; i++) {
                ParkingSlot slot = new ParkingSlot(i);
                allSlots.put(i, slot);
            }

            saveSlotsToFile();
        }
    }

    private void loadSlotsFromFile() {
        List<ParkingSlot> slots = FileStorageUtil.readFromFile(SLOTS_FILE);
        allSlots.clear();
        for (ParkingSlot slot : slots) {
            allSlots.put(slot.getId(), slot);
        }

        ParkingStack stack = FileStorageUtil.readObjectFromFile(AVAILABLE_SLOTS_FILE);
        if (stack != null) {
            availableSlots = stack;
        }
    }

    private void saveSlotsToFile() {
        List<ParkingSlot> slots = new ArrayList<>(allSlots.values());
        FileStorageUtil.writeToFile(SLOTS_FILE, slots);
        FileStorageUtil.writeObjectToFile(AVAILABLE_SLOTS_FILE, availableSlots);
    }

    public int assignSlot(String vehicleNumber) throws Exception {
        if (availableSlots.isEmpty()) {
            throw new Exception("No available slots");
        }

        Integer slotNumber = availableSlots.pop();

        ParkingSlot slot = allSlots.get(slotNumber);
        slot.setOccupied(true);
        slot.setVehicleNumber(vehicleNumber);

        saveSlotsToFile();

        return slotNumber;
    }

    public void releaseSlot(int slotNumber) {
        ParkingSlot slot = allSlots.get(slotNumber);
        slot.setOccupied(false);
        slot.setVehicleNumber(null);

        availableSlots.push(slotNumber);

        saveSlotsToFile();
    }

    public List<ParkingSlot> getAllSlots() {
        return new ArrayList<>(allSlots.values());
    }

    public List<ParkingSlot> getAvailableSlots() {
        return allSlots.values().stream()
                .filter(slot -> !slot.isOccupied())
                .collect(Collectors.toList());
    }

    public List<ParkingSlot> getOccupiedSlots() {
        return allSlots.values().stream()
                .filter(ParkingSlot::isOccupied)
                .collect(Collectors.toList());
    }
}