package com.shalya.diploma.knapsack;

import com.shalya.diploma.models.Category;
import com.shalya.diploma.models.Good;
import com.shalya.diploma.models.Rating;
import com.shalya.diploma.models.User;

import java.io.BufferedInputStream;
import java.util.Set;

public class KnapsackGood extends Good {

    private int v;

    public KnapsackGood(Good good) {
        super(good.getId(), good.getCategory(), good.getName(), good.getPrice(), good.getPhotoUrl(),
                good.getAmount(),good.getUnit(), good.getRating(), good.getIsActive(), good.getUser(),
                good.getRatings());
    }

    @Override
    public int getValue() {
        return v;
    }

    public void setValue(int value){
        v = value;
    }
}
