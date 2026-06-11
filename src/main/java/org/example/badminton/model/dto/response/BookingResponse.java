package org.example.badminton.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.badminton.model.constants.BookingStatus;
import org.example.badminton.model.entity.Booking;

import java.math.BigDecimal;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class BookingResponse {
    private Long id;
    private Long courtId;
    private String courtName;
    private Long timeSlotId;
    private String timeSlotLabel;
    private LocalDate bookingDate;
    private BookingStatus status;
    private BigDecimal totalPrice;
    private String customerName;

    public static BookingResponse fromEntity(Booking booking) {
        return BookingResponse.builder()
                .id(booking.getId())
                .courtId(booking.getCourt().getId())
                .courtName(booking.getCourt().getName())
                .timeSlotId(booking.getTimeslot().getId())
                .timeSlotLabel(booking.getTimeslot().getLabel())
                .bookingDate(booking.getBooking_date())
                .status(booking.getStatus())
                .totalPrice(booking.getTotal_price())
                .customerName(booking.getUser().getFull_name())
                .build();
    }
}
