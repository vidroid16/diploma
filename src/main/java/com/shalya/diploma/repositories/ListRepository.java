package com.shalya.diploma.repositories;

import com.shalya.diploma.models.ShopList;
import com.shalya.diploma.models.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ListRepository extends CrudRepository<ShopList,Long> {
    Optional<List<ShopList>> getAllByUser(User user);
    Optional<ShopList> getById(Long id);
}
