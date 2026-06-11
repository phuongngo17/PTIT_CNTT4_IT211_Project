package org.example.badminton.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.badminton.model.dto.request.BookingCreateRequest;
import org.example.badminton.model.dto.response.ApiDataResponse;
import org.example.badminton.model.dto.response.BookingResponse;
import org.example.badminton.service.BookingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customer/bookings")
@RequiredArgsConstructor
public class CustomerBookingController {
    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<ApiDataResponse<BookingResponse>> createBooking(
            @Valid @RequestBody BookingCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiDataResponse<>(
                true,
                "Đặt lịch thành công",
                bookingService.createBooking(request),
                HttpStatus.CREATED
        ));
    }

    @GetMapping
    public ResponseEntity<ApiDataResponse<List<BookingResponse>>> getMyBookings() {
        return ResponseEntity.ok(new ApiDataResponse<>(
                true,
                "Lấy lịch sử đặt sân thành công",
                bookingService.getMyBookings(),
                HttpStatus.OK
        ));
    }
}
