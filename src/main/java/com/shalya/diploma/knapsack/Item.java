package com.shalya.diploma.knapsack;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Item implements Packable{
    Long id;
    int weight;
    int value;

    @Override
    public int getWeight() {
        return weight;
    }

    @Override
    public int getValue() {
        return value;
    }

    @Override
    public Long getId() {
        return id;
    }
}
