package com.kushagra.hotel.guest;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class GuestService {

    private final GuestRepository repository;

    public GuestService(GuestRepository repository) {
        this.repository = repository;
    }

    public List<Guest> findAll() { return repository.findAll(); }

    public Guest findById(Long id) {
        return repository.findById(id).orElseThrow(() ->
            new ResponseStatusException(HttpStatus.NOT_FOUND, "Guest " + id + " not found"));
    }

    public Guest create(Guest guest) {
        if (repository.existsByEmail(guest.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already registered");
        }
        return repository.save(guest);
    }

    public Guest update(Long id, Guest updated) {
        Guest guest = findById(id);
        if (!guest.getEmail().equals(updated.getEmail())
                && repository.existsByEmail(updated.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already registered");
        }
        guest.setName(updated.getName());
        guest.setEmail(updated.getEmail());
        guest.setPhone(updated.getPhone());
        return repository.save(guest);
    }

    public void delete(Long id) {
        findById(id);
        repository.deleteById(id);
    }
}