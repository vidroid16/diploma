package com.shalya.diploma.repositories;

import com.shalya.diploma.models.Good;
import com.shalya.diploma.models.ListsGoods;
import com.shalya.diploma.models.ShopList;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface ListsGoodsRepository extends CrudRepository<ListsGoods,Long> {
    List<ListsGoods> getAllByShopList(ShopList shopList);
    List<ListsGoods> getByGoodAndShopList(Good good, ShopList shopList);
    List<ListsGoods> getByGoodIdAndShopListId(Long good_id, Long shopList_id);
    @Transactional
    void deleteById(Long id);
    @Transactional
    void deleteByGoodIdAndShopListId(Long good_id, Long shopList_id);
}
