package com.shalya.diploma.mappers;

import com.shalya.diploma.dto.GoodInShopListDto;
import com.shalya.diploma.models.Category;
import com.shalya.diploma.models.Good;
import com.shalya.diploma.models.ListsGoods;

public class GoodMapper {
    public static GoodInShopListDto listsGoodsToGoodInShopListDto(ListsGoods entity){
        GoodInShopListDto dto = new GoodInShopListDto();
        dto.setGoodsListsId(entity.getId());
        dto.setId(entity.getGood().getId());
        dto.setUnit(entity.getGood().getUnit());
        dto.setPrice(entity.getGood().getPrice());
        dto.setAmount(entity.getGood().getAmount());
        dto.setName(entity.getGood().getName());
        dto.setCategoryId(entity.getGood().getCategory().getId());
        dto.setCategoryName(entity.getGood().getCategory().getName());
        dto.setIsChecked(entity.getIsChecked());
        return dto;
    }
}
