package com.shalya.diploma.repositories;

import com.shalya.diploma.dto.RatingDto;
import com.shalya.diploma.models.Rating;
import com.shalya.diploma.models.RatingId;
import com.shalya.diploma.models.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public interface RatingRepository extends CrudRepository<Rating,Long> {
    List<Rating> getAllByUser(User user);
    Optional<Rating> getById(RatingId id);

    @Query("SELECT new com.shalya.diploma.dto.RatingDto(r.user.id, g.id, r.rating) FROM Good g " +
            "LEFT JOIN Rating r on g.id = r.good.id AND r.user.id = :userId " +
            "ORDER BY g.id")
    List<RatingDto> getUserRatingsVector(Long userId);
}
