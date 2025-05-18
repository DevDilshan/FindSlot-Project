package com.findslot.util;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
public class ParkingStack implements Serializable{
    private static final long serialVersionUID = 1L;

    private List<Integer> elements;

    public ParkingStack() {
        this.elements = new ArrayList<>();
    }

    public void push(Integer slot) {
        elements.add(slot);
    }

    public Integer pop() {
        if (isEmpty()) {
            return null;
        }
        return elements.remove(elements.size() - 1);
    }

    public boolean isEmpty() {
        return elements.isEmpty();
    }

    public int size() {
        return elements.size();
    }

    public void initialize(int capacity) {
        elements.clear();
        for (int i = capacity; i >= 1; i--) {
            push(i);
        }
    }

}

