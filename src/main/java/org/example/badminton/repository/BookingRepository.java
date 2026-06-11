package org.example.badminton.repository;

import org.example.badminton.model.constants.BookingStatus;
import org.example.badminton.model.entity.Booking;
import org.example.badminton.model.entity.Court;
import org.example.badminton.model.entity.Timeslot;
import org.example.badminton.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Query("""
            SELECT CASE WHEN COUNT(b) > 0 THEN true ELSE false END
            FROM Booking b
            WHERE b.court = :court
            AND b.booking_date = :bookingDate
            AND b.timeslot = :timeslot
            AND b.status IN :statuses
            """)
    boolean existsByCourtAndBooking_dateAndTimeslotAndStatusIn(
            @Param("court") Court court,
            @Param("bookingDate") LocalDate bookingDate,
            @Param("timeslot") Timeslot timeslot,
            @Param("statuses") List<BookingStatus> statuses
    );

    @Query("""
            SELECT b FROM Booking b
            JOIN FETCH b.court
            JOIN FETCH b.timeslot
            JOIN FETCH b.user
            WHERE b.user.id = :userId
            ORDER BY b.booking_date DESC
            """)
    List<Booking> findByUserIdWithDetails(@Param("userId") Long userId);
}
