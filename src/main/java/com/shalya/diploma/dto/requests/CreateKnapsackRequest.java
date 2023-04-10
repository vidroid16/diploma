package com.shalya.diploma.dto.requests;

import com.shalya.diploma.models.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateKnapsackRequest {
    List<Long> categories;
    Integer knapsackType;
    Integer budget;
    String name;
}
