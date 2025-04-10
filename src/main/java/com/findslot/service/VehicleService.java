package com.findslot.service;

import com.findslot.model.Vehicle;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

@Service
public class VehicleService {

    private final Stack<Vehicle> vehicleStack = new Stack<>();
    private final int TOTAL_CAPACITY = 50;

    // Add a vehicle to the stack
    public Vehicle addVehicle(Vehicle vehicle) {
        if (vehicleStack.size() >= TOTAL_CAPACITY) {
            throw new RuntimeException("Parking is full");
        }

        // Check if vehicle already exists
        if (vehicleStack.stream().anyMatch(v -> v.getNumber().equals(vehicle.getNumber()))) {
            throw new RuntimeException("Vehicle already parked");
        }

        vehicleStack.push(vehicle);
        return vehicle;
    }

    // Get all vehicles (convert stack to list)
    public List<Vehicle> getAllVehicles() {
        List<Vehicle> vehicles = new ArrayList<>(vehicleStack);
        return vehicles;
    }

    // Get vehicle by number
    public Vehicle getVehicleByNumber(String number) {
        return vehicleStack.stream()
                .filter(v -> v.getNumber().equals(number))
                .findFirst()
                .orElse(null);
    }

    // Remove vehicle by number
    public Vehicle removeVehicle(String number) {
        Vehicle vehicle = getVehicleByNumber(number);
        if (vehicle == null) {
            throw new RuntimeException("Vehicle not found");
        }

        // Create a temporary stack to remove the vehicle
        Stack<Vehicle> tempStack = new Stack<>();
        Vehicle removedVehicle = null;

        while (!vehicleStack.isEmpty()) {
            Vehicle v = vehicleStack.pop();
            if (v.getNumber().equals(number)) {
                removedVehicle = v;
            } else {
                tempStack.push(v);
            }
        }

        // Restore the stack without the removed vehicle
        while (!tempStack.isEmpty()) {
            vehicleStack.push(tempStack.pop());
        }

        return removedVehicle;
    }

    // Get available spaces
    public int getAvailableSpaces() {
        return TOTAL_CAPACITY - vehicleStack.size();
    }

    // Get total capacity
    public int getTotalCapacity() {
        return TOTAL_CAPACITY;
    }

    // Sort vehicles using Quick Sort (by vehicle number)
    public List<Vehicle> getSortedVehicles() {
        List<Vehicle> vehicles = new ArrayList<>(vehicleStack);
        return quickSort(vehicles, Comparator.comparing(Vehicle::getNumber));
    }

    // Quick Sort implementation
    private <T> List<T> quickSort(List<T> list, Comparator<T> comparator) {
        if (list.size() <= 1) {
            return list;
        }

        List<T> sorted = new ArrayList<>(list);
        quickSort(sorted, 0, sorted.size() - 1, comparator);
        return sorted;
    }

    private <T> void quickSort(List<T> list, int low, int high, Comparator<T> comparator) {
        if (low < high) {
            int partitionIndex = partition(list, low, high, comparator);

            quickSort(list, low, partitionIndex - 1, comparator);
            quickSort(list, partitionIndex + 1, high, comparator);
        }
    }

    private <T> int partition(List<T> list, int low, int high, Comparator<T> comparator) {
        T pivot = list.get(high);
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (comparator.compare(list.get(j), pivot) <= 0) {
                i++;

                // Swap elements
                T temp = list.get(i);
                list.set(i, list.get(j));
                list.set(j, temp);
            }
        }

        // Swap pivot element
        T temp = list.get(i + 1);
        list.set(i + 1, list.get(high));
        list.set(high, temp);

        return i + 1;
    }

    // Get recent vehicles (last 5)
    public List<Vehicle> getRecentVehicles() {
        List<Vehicle> vehicles = new ArrayList<>(vehicleStack);
        return vehicles.stream()
                .sorted(Comparator.comparing(Vehicle::getEntryTime).reversed())
                .limit(5)
                .collect(Collectors.toList());
    }
}