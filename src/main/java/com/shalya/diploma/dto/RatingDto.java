package com.shalya.diploma.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RatingDto {
    Long userId;
    Long goodId;
    Double rating;
}
