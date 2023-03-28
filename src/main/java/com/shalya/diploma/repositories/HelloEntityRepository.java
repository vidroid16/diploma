package com.shalya.diploma.repositories;

import com.shalya.diploma.models.HelloEntity;
import org.springframework.data.repository.CrudRepository;

public interface HelloEntityRepository extends CrudRepository<HelloEntity,Long> {
}
