package com.kushagra.hotel.booking;

import java.time.LocalDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("select count(b) from Booking b where b.room.id = :roomId "
         + "and b.status = com.kushagra.hotel.booking.BookingStatus.CONFIRMED "
         + "and b.checkIn < :checkOut and b.checkOut > :checkIn")
    long countOverlaps(@Param("roomId") Long roomId,
                       @Param("checkIn") LocalDate checkIn,
                       @Param("checkOut") LocalDate checkOut);
}