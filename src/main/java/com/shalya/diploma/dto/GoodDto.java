package com.shalya.diploma.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoodDto {
    private Long id;
    private String name;
    private String unit;
    private Double price;
    private Integer amount;
}
