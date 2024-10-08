package com.nativenavs.review.dto;

import com.nativenavs.tour.dto.TourDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
public class TourReviewDTO {
    private float reviewAverage;
    private int reviewCount;
    private int totalImageCount;
    private List<String> imageUrls;
    private List<ReviewResponseDTO> reviews;

}
