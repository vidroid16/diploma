package com.shalya.diploma.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoodInShopListDto {
    private Long id;
    private Long goodsListsId;
    private String name;
    private Integer amount;
    private Double price;
    private String unit;
    private Boolean isChecked;
    private String categoryName;
    private Long categoryId;
}
