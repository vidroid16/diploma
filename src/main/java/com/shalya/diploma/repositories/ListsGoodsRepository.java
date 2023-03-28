package com.shalya.diploma.repositories;

import com.shalya.diploma.models.Good;
import com.shalya.diploma.models.ListsGoods;
import com.shalya.diploma.models.ShopList;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ListsGoodsRepository extends CrudRepository<ListsGoods,Long> {
    List<ListsGoods> getAllByShopList(ShopList shopList);
    Optional<ListsGoods> getByGoodAndShopList(Good good, ShopList shopList);
    Optional<ListsGoods> getByGoodIdAndShopListId(Long good_id, Long shopList_id);
    void deleteById(Long id);
    void deleteByGoodIdAndShopListId(Long good_id, Long shopList_id);
}
