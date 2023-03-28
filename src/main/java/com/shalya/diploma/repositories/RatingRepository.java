package com.shalya.diploma.repositories;

import com.shalya.diploma.models.Rating;
import com.shalya.diploma.models.User;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;
import java.util.List;

public interface RatingRepository extends CrudRepository<Rating,Long> {
    List<Rating> getAllByUser(User user);
}
