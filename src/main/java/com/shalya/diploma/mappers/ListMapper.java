package com.shalya.diploma.mappers;

import com.shalya.diploma.dto.requests.UpdateShopListRequest;
import com.shalya.diploma.models.ShopList;

public class ListMapper {
    public static ShopList updateShopListRequestToShopList(UpdateShopListRequest dto){
        ShopList entity = new ShopList();
        entity.setName(dto.getName());
        entity.setBudget(dto.getBudget());
        return entity;
    }
}
