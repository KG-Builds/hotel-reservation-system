package com.kushagra.hotel.booking;

import com.kushagra.hotel.guest.Guest;
import com.kushagra.hotel.guest.GuestService;
import com.kushagra.hotel.room.Room;
import com.kushagra.hotel.room.RoomService;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class BookingService {

    private final BookingRepository bookings;
    private final RoomService rooms;
    private final GuestService guests;

    public BookingService(BookingRepository bookings, RoomService rooms, GuestService guests) {
        this.bookings = bookings;
        this.rooms = rooms;
        this.guests = guests;
    }

    public List<Booking> findAll() { return bookings.findAll(); }

    public Booking findById(Long id) {
        return bookings.findById(id).orElseThrow(() ->
            new ResponseStatusException(HttpStatus.NOT_FOUND, "Booking " + id + " not found"));
    }

    @Transactional
    public Booking create(BookingRequest request) {
        Guest guest = guests.findById(request.guestId());
        Room room = rooms.findById(request.roomId());

        if (request.checkIn().isBefore(LocalDate.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "checkIn cannot be in the past");
        }
        if (!request.checkOut().isAfter(request.checkIn())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "checkOut must be after checkIn");
        }
        if (bookings.countOverlaps(room.getId(), request.checkIn(), request.checkOut()) > 0) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                "Room " + room.getId() + " is not available for those dates");
        }

        long nights = ChronoUnit.DAYS.between(request.checkIn(), request.checkOut());

        Booking booking = new Booking();
        booking.setGuest(guest);
        booking.setRoom(room);
        booking.setCheckIn(request.checkIn());
        booking.setCheckOut(request.checkOut());
        booking.setStatus(BookingStatus.CONFIRMED);
        booking.setTotalPrice(nights * room.getPricePerNight());
        return bookings.save(booking);
    }

    @Transactional
    public Booking cancel(Long id) {
        Booking booking = findById(id);
        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Booking is already cancelled");
        }
        booking.setStatus(BookingStatus.CANCELLED);
        return bookings.save(booking);
    }
}