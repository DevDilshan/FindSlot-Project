// File: src/main/java/com/parking/service/VehicleService.java
package com.findslot.service;

import com.findslot.model.ParkingRecord;
import com.findslot.model.Vehicle;
import com.findslot.util.FeeCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class VehicleService {

    @Autowired
    private DashboardService dashboardService;

    @Autowired
    private ParkingSlotService slotService;

    @Autowired
    private ParkingHistoryService historyService;

    @Autowired
    private FeeCalculator feeCalculator;

    public Vehicle getVehicle(String vehicleNumber) throws Exception {
        return dashboardService.getVehicle(vehicleNumber);
    }

    public ParkingRecord removeVehicle(String vehicleNumber) throws Exception {
        Vehicle vehicle = dashboardService.getVehicle(vehicleNumber);

        Date exitTime = new Date();
        double fee = feeCalculator.calculateFee(vehicle.getEntryTime(), exitTime, vehicle.getType());

        ParkingRecord record = new ParkingRecord(
                vehicle.getNumber(),
                vehicle.getType(),
                vehicle.getEntryTime(),
                exitTime,
                fee,
                vehicle.getSlotNumber()
        );

        slotService.releaseSlot(vehicle.getSlotNumber());
        dashboardService.removeVehicleFromMap(vehicleNumber);
        historyService.addRecord(record);

        return record;
    }
}