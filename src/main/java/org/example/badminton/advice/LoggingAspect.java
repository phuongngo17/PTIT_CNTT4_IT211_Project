package org.example.badminton.advice;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.example.badminton.model.dto.request.BookingCreateRequest;
import org.example.badminton.model.dto.response.BookingResponse;
import org.example.badminton.security.principal.CustomUserDetails;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Pointcut("execution(* org.example.badminton.service.BookingService.createBooking(..))")
    public void bookingPointcut() {
    }

    @AfterReturning(pointcut = "bookingPointcut()", returning = "result")
    public void logBookingSuccess(JoinPoint joinPoint, Object result) {
        if (result instanceof BookingResponse booking) {
            String customerName = getCurrentUsername();
            log.info("[AUDIT - SUCCESS] Khách hàng {} đặt thành công Sân {} vào ngày {}, Khung giờ {}",
                    customerName,
                    booking.getCourtName(),
                    booking.getBookingDate(),
                    booking.getTimeSlotLabel());
        }
    }

    @AfterThrowing(pointcut = "bookingPointcut()", throwing = "ex")
    public void logBookingFailure(JoinPoint joinPoint, Throwable ex) {
        String customerName = getCurrentUsername();
        BookingCreateRequest request = extractBookingRequest(joinPoint);
        if (request != null) {
            log.warn("[AUDIT - FAILED] Khách hàng {} cố gắng đặt Sân id {} nhưng thất bại do: {}",
                    customerName,
                    request.getCourtId(),
                    ex.getMessage());
        } else {
            log.warn("[AUDIT - FAILED] Khách hàng {} đặt sân thất bại do: {}", customerName, ex.getMessage());
        }
    }

    private String getCurrentUsername() {
        try {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal instanceof CustomUserDetails userDetails) {
                return userDetails.getFullName();
            }
            return "Unknown";
        } catch (Exception e) {
            return "Unknown";
        }
    }

    private BookingCreateRequest extractBookingRequest(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        if (args.length > 0 && args[0] instanceof BookingCreateRequest request) {
            return request;
        }
        return null;
    }
}
