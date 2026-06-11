package org.example.badminton.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.example.badminton.exception.HttpNotFoundException;
import org.example.badminton.model.entity.Court;
import org.example.badminton.model.entity.CourtImage;
import org.example.badminton.repository.CourtImageRepository;
import org.example.badminton.repository.CourtRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CourtImageService {

    private final CourtRepository courtRepository;
    private final CourtImageRepository courtImageRepository;
    private final Cloudinary cloudinary;

    @Transactional
    public List<String> uploadImages(
            Long courtId,
            MultipartFile[] files) {

        Court court = courtRepository.findById(courtId)
                .orElseThrow(() ->
                        new HttpNotFoundException(
                                "Không tìm thấy sân"));

        List<String> imageUrls = new ArrayList<>();

        try {

            for (MultipartFile file : files) {

                Map uploadResult =
                        cloudinary.uploader().upload(
                                file.getBytes(),
                                ObjectUtils.asMap(
                                        "folder",
                                        "badminton-courts"
                                )
                        );

                String imageUrl =
                        uploadResult.get("secure_url")
                                .toString();

                CourtImage image =
                        CourtImage.builder()
                                .court(court)
                                .imageUrl(imageUrl)
                                .build();

                courtImageRepository.save(image);

                imageUrls.add(imageUrl);
            }

            return imageUrls;

        } catch (Exception e) {
            throw new RuntimeException(
                    "Upload ảnh thất bại: "
                            + e.getMessage()
            );
        }
    }
}