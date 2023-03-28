package com.shalya.diploma.dto.requests;

import com.shalya.diploma.dto.GoodInShopListDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateShopListRequest {
    private String name;
    private Integer budget;
    private Double totalPrice;
    private List<GoodInShopListDto> goods;
}
