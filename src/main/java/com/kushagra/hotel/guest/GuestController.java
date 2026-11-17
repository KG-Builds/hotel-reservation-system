package com.kushagra.hotel.guest;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/guests")
public class GuestController {

    private final GuestService service;

    public GuestController(GuestService service) { this.service = service; }

    @GetMapping
    public List<Guest> all() { return service.findAll(); }

    @GetMapping("/{id}")
    public Guest one(@PathVariable Long id) { return service.findById(id); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Guest create(@Valid @RequestBody Guest guest) { return service.create(guest); }

    @PutMapping("/{id}")
    public Guest update(@PathVariable Long id, @Valid @RequestBody Guest guest) {
        return service.update(id, guest);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) { service.delete(id); }
}