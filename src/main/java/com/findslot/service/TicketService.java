package com.findslot.service;


import com.findslot.model.ParkingTicket;
import com.findslot.model.Vehicle;
import com.findslot.util.FeeCalculator;
import com.findslot.util.FileStorageUtil;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class TicketService {
    private static final String TICKETS_FILE = "data/tickets.txt";

    private List<ParkingTicket> tickets;

    @Autowired
    private DashboardService dashboardService;

    @Autowired
    private FeeCalculator feeCalculator;

    public TicketService() {
        this.tickets = new ArrayList<>();
    }

    @PostConstruct
    public void init() {
        loadTicketsFromFile();
    }

    private void loadTicketsFromFile() {
        List<ParkingTicket> ticketList = FileStorageUtil.readFromFile(TICKETS_FILE);
        if (ticketList != null && !ticketList.isEmpty()) {
            tickets = ticketList;
        }
    }

    private void saveTicketsToFile() {
        FileStorageUtil.writeToFile(TICKETS_FILE, tickets);
    }

    public ParkingTicket generateTicket(String vehicleNumber) throws Exception {
        Vehicle vehicle = dashboardService.getVehicle(vehicleNumber);

        Date exitTime = new Date();
        double fee = feeCalculator.calculateFee(vehicle.getEntryTime(), exitTime, vehicle.getType());


        String ticketNumber = "TKT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        ParkingTicket ticket = new ParkingTicket(
                ticketNumber,
                vehicle.getNumber(),
                vehicle.getType(),
                vehicle.getOwner(),
                vehicle.getPhone(),
                vehicle.getEntryTime(),
                exitTime,
                vehicle.getSlotNumber(),
                fee
        );

        tickets.add(ticket);

        saveTicketsToFile();

        return ticket;
    }
}