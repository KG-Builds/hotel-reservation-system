package com.kushagra.hotel.room;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RoomRepository extends JpaRepository<Room, Long> {

    @Query("select r from Room r where r.id not in (select b.room.id from Booking b "
         + "where b.status = com.kushagra.hotel.booking.BookingStatus.CONFIRMED "
         + "and b.checkIn < :checkOut and b.checkOut > :checkIn)")
    List<Room> findAvailable(@Param("checkIn") LocalDate checkIn,
                             @Param("checkOut") LocalDate checkOut);
}