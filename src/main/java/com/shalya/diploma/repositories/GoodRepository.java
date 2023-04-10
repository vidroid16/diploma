package com.shalya.diploma.repositories;

import com.shalya.diploma.dto.GoodInShopListDto;
import com.shalya.diploma.models.Category;
import com.shalya.diploma.models.Good;
import com.shalya.diploma.models.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.function.DoublePredicate;

public interface GoodRepository extends CrudRepository<Good,Long> {
    Optional<Good> getById(Long id);
    Optional<Good> getByName(String name);
    Optional<Good> getByNameAndCategory(String name, Category category);
    List<Good> findAllByCategory(Category category);
    List<Good> findAllByUser(User user);
    List<Good> findAll();
    @Modifying
    @Transactional
    @Query("UPDATE Good SET rating = :rating, price = :price where id = :id")
    void updateGood(Long id, Double price, int rating);
    @Modifying
    @Transactional
    @Query("UPDATE Good SET isActive = false where id is not null AND category.id = :categoryId")
    void setAllGoodsInCategoryIsActiveFalse(Long categoryId);

//    @Query(value = "select g.id as id, g.name as name, g.price as price, g.amount as amount, g.unit as unit, g.category_id as category_id from lists l" +
//            "    inner join lists_goods lg on l.id  = lg.list_id" +
//            "    inner join goods g on g.id = lg.good_id" +
//            "    inner join categories c on c.id = g.category_id" +
//            "    where l.id = :listId")
//    List<Good> getGoodsInShopList(Integer listId);
}
