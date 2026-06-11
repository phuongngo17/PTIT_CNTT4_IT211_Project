package org.example.badminton.controller;

import lombok.RequiredArgsConstructor;
import org.example.badminton.model.dto.response.ApiDataResponse;
import org.example.badminton.service.CourtImageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/courts")
@RequiredArgsConstructor
public class CourtImageController {

    private final CourtImageService courtImageService;

    @PostMapping("/{courtId}/images")
    public ResponseEntity<ApiDataResponse<List<String>>> uploadImages(
            @PathVariable Long courtId,
            @RequestParam("files")
            MultipartFile[] files) {

        return ResponseEntity.ok(
                new ApiDataResponse<>(
                        true,
                        "Tải ảnh sân thành công",
                        courtImageService.uploadImages(
                                courtId,
                                files
                        ),
                        HttpStatus.OK
                )
        );
    }
}