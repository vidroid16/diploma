package com.shalya.diploma.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class RatingId implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "userId")
    Long userId;

    @Column(name = "goodId")
    Long goodId;

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((goodId == null) ? 0 : goodId.hashCode());
        result = prime * result
                + ((userId == null) ? 0 : userId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        RatingId other = (RatingId) obj;
        return Objects.equals(getGoodId(), other.getGoodId()) && Objects.equals(getUserId(), other.getUserId());
    }
}
