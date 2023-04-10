package com.shalya.diploma.mappers;

import com.shalya.diploma.dto.GoodDto;
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

    public static Good goodDtoToGood(GoodDto dto){
        Good entity = new Good();
        entity.setIsActive(true);
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setPrice(dto.getPrice());
        entity.setAmount(dto.getAmount());
        entity.setUnit(dto.getUnit());
        return entity;
    }
    public static GoodDto goodToGoodDto(Good entity){
        GoodDto dto = new GoodDto();
        dto.setName(entity.getName());
        dto.setId(entity.getId());
        dto.setAmount(entity.getAmount());
        dto.setUnit(entity.getUnit());
        dto.setPrice(entity.getPrice());
        return dto;
    }
}
