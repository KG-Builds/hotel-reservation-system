package com.kushagra.hotel.booking;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService service;

    public BookingController(BookingService service) { this.service = service; }

    @GetMapping
    public List<Booking> all() { return service.findAll(); }

    @GetMapping("/{id}")
    public Booking one(@PathVariable Long id) { return service.findById(id); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Booking create(@Valid @RequestBody BookingRequest request) {
        return service.create(request);
    }

    @PutMapping("/{id}/cancel")
    public Booking cancel(@PathVariable Long id) { return service.cancel(id); }
}