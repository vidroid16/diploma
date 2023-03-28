package com.shalya.diploma.repositories;

import com.shalya.diploma.dto.GoodInShopListDto;
import com.shalya.diploma.models.Good;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface GoodRepository extends CrudRepository<Good,Long> {
    Optional<Good> getById(Long id);

//    @Query(value = "select g.id as id, g.name as name, g.price as price, g.amount as amount, g.unit as unit, g.category_id as category_id from lists l" +
//            "    inner join lists_goods lg on l.id  = lg.list_id" +
//            "    inner join goods g on g.id = lg.good_id" +
//            "    inner join categories c on c.id = g.category_id" +
//            "    where l.id = :listId")
//    List<Good> getGoodsInShopList(Integer listId);
}