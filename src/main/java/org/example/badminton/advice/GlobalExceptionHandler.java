package org.example.badminton.advice;

import org.example.badminton.exception.HttpBadRequestException;
import org.example.badminton.exception.HttpNotFoundException;
import org.example.badminton.model.dto.response.ApiDataResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiDataResponse<Map<String, String>>> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getFieldErrors().forEach(err -> errors.put(err.getField(), err.getDefaultMessage()));
        return ResponseEntity.badRequest().body(new ApiDataResponse<>(
                false, "Dữ liệu không hợp lệ", errors, HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(HttpBadRequestException.class)
    public ResponseEntity<ApiDataResponse<String>> handleBadRequest(HttpBadRequestException ex) {
        return ResponseEntity.badRequest().body(new ApiDataResponse<>(
                false, ex.getMessage(), null, HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(HttpNotFoundException.class)
    public ResponseEntity<ApiDataResponse<String>> handleNotFound(HttpNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiDataResponse<>(
                false, ex.getMessage(), null, HttpStatus.NOT_FOUND));
    }

    @ExceptionHandler({BadCredentialsException.class, UsernameNotFoundException.class})
    public ResponseEntity<ApiDataResponse<String>> handleUnauthorized(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiDataResponse<>(
                false, "Sai tài khoản hoặc mật khẩu", null, HttpStatus.UNAUTHORIZED));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiDataResponse<String>> handleRuntime(RuntimeException ex) {
        return ResponseEntity.badRequest().body(new ApiDataResponse<>(
                false, ex.getMessage(), null, HttpStatus.BAD_REQUEST));
    }
}
