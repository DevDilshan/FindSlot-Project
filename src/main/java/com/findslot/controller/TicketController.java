package com.findslot.controller;


import com.findslot.model.ParkingTicket;
import com.findslot.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    // Generate a ticket for a vehicle
    @GetMapping("/generate/{vehicleNumber}")
    public ResponseEntity<?> generateTicket(@PathVariable String vehicleNumber) {
        try {
            ParkingTicket ticket = ticketService.generateTicket(vehicleNumber);
            return ResponseEntity.ok(ticket);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
