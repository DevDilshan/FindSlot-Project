package com.findslot.util;

import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;
@Component

public class FeeCalculator implements Serializable {
    private static final long serialVersionUID = 1L;

    private static final double CAR_RATE = 100.0;
    private static final double MOTORCYCLE_RATE = 80.0;
    private static final double TRUCK_RATE = 150.0;

    public double calculateFee(Date entryTime, Date exitTime, String vehicleType) {
        long durationMs = exitTime.getTime() - entryTime.getTime();
        double durationHours = Math.ceil(durationMs / (1000.0 * 60 * 60));

        double rate;
        switch (vehicleType.toLowerCase()) {
            case "car":
                rate = CAR_RATE;
                break;
            case "motorcycle":
                rate = MOTORCYCLE_RATE;
                break;
            case "truck":
                rate = TRUCK_RATE;
                break;
            default:
                rate = CAR_RATE;
        }

        return durationHours * rate;
    }
}