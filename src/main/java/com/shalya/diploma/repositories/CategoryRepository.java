package com.shalya.diploma.repositories;

import com.shalya.diploma.models.Category;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends CrudRepository<Category,Long> {
    Category getById(Long id);
    List<Category> findAll();
}
