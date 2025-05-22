// File: src/main/java/com/parking/util/QuickSort.java
package com.findslot.util;

import com.findslot.model.ParkingRecord;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;

@Component
public class QuickSort implements Serializable {
    private static final long serialVersionUID = 1L;

    public void sort(List<ParkingRecord> records) {
        if (records == null || records.size() <= 1) {
            return;
        }
        quickSort(records, 0, records.size() - 1);
    }

    private int partition(List<ParkingRecord> records, int low, int high) {
        ParkingRecord pivot = records.get(high);
        int i = (low - 1);

        for (int j = low; j < high; j++) {
            if (records.get(j).getExitTime().compareTo(pivot.getExitTime()) < 0) {
                i++;

                ParkingRecord temp = records.get(i);
                records.set(i, records.get(j));
                records.set(j, temp);
            }
        }

        ParkingRecord temp = records.get(i + 1);
        records.set(i + 1, records.get(high));
        records.set(high, temp);

        return i + 1;
    }

    private void quickSort(List<ParkingRecord> records, int low, int high) {
        if (low < high) {
            int pi = partition(records, low, high);

            quickSort(records, low, pi - 1);
            quickSort(records, pi + 1, high);
        }
    }
}