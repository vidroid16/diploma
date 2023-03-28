package com.shalya.diploma.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetUserShopListDto {
    private Long id;
    private String name;
    private Integer budget;
    private Double totalPrice;
    private List<GoodInShopListDto> goods;
}
