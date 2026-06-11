package org.example.badminton.controller;

import lombok.RequiredArgsConstructor;
import org.example.badminton.model.dto.response.ApiDataResponse;
import org.example.badminton.model.dto.response.BookingResponse;
import org.example.badminton.service.BookingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/manager/bookings")
@RequiredArgsConstructor
public class ManagerBookingController {

    private final BookingService bookingService;

    @PatchMapping("/{id}/approve")
    public ResponseEntity<ApiDataResponse<BookingResponse>> approveBooking(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                new ApiDataResponse<>(
                        true,
                        "Phê duyệt lịch đặt sân thành công",
                        bookingService.approveBooking(id),
                        HttpStatus.OK
                )
        );
    }

    @PatchMapping("/{id}/reject")
    public ResponseEntity<ApiDataResponse<BookingResponse>> rejectBooking(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                new ApiDataResponse<>(
                        true,
                        "Từ chối lịch đặt sân thành công",
                        bookingService.rejectBooking(id),
                        HttpStatus.OK
                )
        );
    }
}