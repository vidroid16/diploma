package com.shalya.diploma.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ratings")
@AllArgsConstructor
@Data
@NoArgsConstructor
public class Rating {

    public Rating(User user, Good good, Double rating, Boolean isUserOwned) {
        this.user = user;
        this.good = good;
        this.rating = rating;
        this.isUserOwned = isUserOwned;
    }

    @EmbeddedId
    RatingId id = new RatingId();
    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "userId")
    User user;

    @ManyToOne
    @MapsId("goodId")
    @JoinColumn(name = "goodId")
    Good good;

    @Column(name = "rating")
    Double rating;

    @Column(name = "uOwned")
    Boolean isUserOwned;

}