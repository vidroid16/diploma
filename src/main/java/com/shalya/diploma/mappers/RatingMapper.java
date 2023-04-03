package com.shalya.diploma.mappers;

import com.shalya.diploma.dto.RatingDto;
import com.shalya.diploma.models.Rating;

public class RatingMapper {
    public static RatingDto ratingToRatingDto(Rating rating){
        RatingDto dto = new RatingDto();
        dto.setRating(rating.getRating());
        dto.setUserId(rating.getUser().getId());
        dto.setGoodId(rating.getGood().getId());
        return dto;
    }
}
