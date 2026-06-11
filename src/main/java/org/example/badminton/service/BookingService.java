package org.example.badminton.service;

import lombok.RequiredArgsConstructor;
import org.example.badminton.exception.HttpBadRequestException;
import org.example.badminton.exception.HttpNotFoundException;
import org.example.badminton.model.constants.BookingStatus;
import org.example.badminton.model.dto.request.BookingCreateRequest;
import org.example.badminton.model.dto.response.BookingResponse;
import org.example.badminton.model.entity.Booking;
import org.example.badminton.model.entity.Court;
import org.example.badminton.model.entity.Timeslot;
import org.example.badminton.model.entity.User;
import org.example.badminton.repository.BookingRepository;
import org.example.badminton.repository.CourtRepository;
import org.example.badminton.repository.TimeslotRepository;
import org.example.badminton.security.principal.CustomUserDetails;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final BookingRepository bookingRepository;
    private final CourtRepository courtRepository;
    private final TimeslotRepository timeslotRepository;

    private static final List<BookingStatus> ACTIVE_STATUSES = List.of(
            BookingStatus.PENDING,
            BookingStatus.CONFIRMED
    );

    @Transactional
    public BookingResponse createBooking(BookingCreateRequest request) {
        CustomUserDetails currentUser = getCurrentUser();

        if (!"CUSTOMER".equals(currentUser.getRole())) {
            throw new HttpBadRequestException("Chỉ khách hàng mới được đặt lịch");
        }

        Court court = courtRepository.findById(request.getCourtId())
                .orElseThrow(() -> new HttpNotFoundException("Không tìm thấy sân với id: " + request.getCourtId()));

        if (!Boolean.TRUE.equals(court.getIs_active())) {
            throw new HttpBadRequestException("Sân hiện không hoạt động");
        }

        Timeslot timeslot = timeslotRepository.findById(request.getTimeSlotId())
                .orElseThrow(() -> new HttpNotFoundException("Không tìm thấy khung giờ với id: " + request.getTimeSlotId()));

        if (request.getBookingDate().isBefore(LocalDate.now())) {
            throw new HttpBadRequestException("Ngày đặt phải từ hôm nay trở đi");
        }

        if (bookingRepository.existsByCourtAndBooking_dateAndTimeslotAndStatusIn(
                court, request.getBookingDate(), timeslot, ACTIVE_STATUSES)) {
            throw new HttpBadRequestException("Khung giờ này đã được đặt, vui lòng chọn khung giờ khác");
        }

        User user = User.builder().id(currentUser.getId()).build();

        Booking booking = Booking.builder()
                .user(user)
                .court(court)
                .timeslot(timeslot)
                .booking_date(request.getBookingDate())
                .status(BookingStatus.PENDING)
                .total_price(court.getPrice())
                .build();

        Booking saved = bookingRepository.save(booking);
        saved.setUser(User.builder()
                .id(currentUser.getId())
                .full_name(currentUser.getFullName())
                .build());
        saved.setCourt(court);
        saved.setTimeslot(timeslot);

        return BookingResponse.fromEntity(saved);
    }

    public List<BookingResponse> getMyBookings() {
        CustomUserDetails currentUser = getCurrentUser();

        return bookingRepository.findByUserIdWithDetails(currentUser.getId()).stream()
                .map(BookingResponse::fromEntity)
                .collect(Collectors.toList());
    }

    private CustomUserDetails getCurrentUser() {
        return (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
    @Transactional
    public BookingResponse approveBooking(Long id) {

        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() ->
                        new HttpNotFoundException(
                                "Không tìm thấy lịch đặt sân với id: " + id));

        if (booking.getStatus() != BookingStatus.PENDING) {
            throw new HttpBadRequestException(
                    "Chỉ được phê duyệt lịch đang chờ xử lý");
        }

        booking.setStatus(BookingStatus.CONFIRMED);

        Booking saved = bookingRepository.save(booking);

        return BookingResponse.fromEntity(saved);
    }
    @Transactional
    public BookingResponse rejectBooking(Long id) {

        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() ->
                        new HttpNotFoundException(
                                "Không tìm thấy lịch đặt sân với id: " + id));

        if (booking.getStatus() != BookingStatus.PENDING) {
            throw new HttpBadRequestException(
                    "Chỉ được từ chối lịch đang chờ xử lý");
        }

        booking.setStatus(BookingStatus.REJECTED);

        Booking saved = bookingRepository.save(booking);

        return BookingResponse.fromEntity(saved);
    }
}
