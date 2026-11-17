package com.kushagra.hotel.room;

import java.time.LocalDate;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class RoomService {

    private final RoomRepository repository;

    public RoomService(RoomRepository repository) {
        this.repository = repository;
    }

    public List<Room> findAll() { return repository.findAll(); }

    public Room findById(Long id) {
        return repository.findById(id).orElseThrow(() ->
            new ResponseStatusException(HttpStatus.NOT_FOUND, "Room " + id + " not found"));
    }

    public Room create(Room room) { return repository.save(room); }

    public Room update(Long id, Room updated) {
        Room room = findById(id);
        room.setRoomNumber(updated.getRoomNumber());
        room.setType(updated.getType());
        room.setPricePerNight(updated.getPricePerNight());
        room.setCapacity(updated.getCapacity());
        return repository.save(room);
    }

    public void delete(Long id) {
        findById(id);
        repository.deleteById(id);
    }

    public List<Room> findAvailable(LocalDate checkIn, LocalDate checkOut) {
        if (!checkOut.isAfter(checkIn)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "checkOut must be after checkIn");
        }
        return repository.findAvailable(checkIn, checkOut);
    }
}