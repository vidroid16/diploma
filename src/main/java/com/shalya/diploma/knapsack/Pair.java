package com.shalya.diploma.knapsack;

import lombok.Data;

@Data
public class Pair{
    public int value;
    public String ids;

    public Pair(int value) {
        this.value = value;
        ids = "";
    }

    void addNewPackable(Packable packable){
        if (!ids.equals(""))
            ids = ids.concat("+"+packable.getId());
        else
            ids = ids.concat(""+packable.getId());
    }
    void concatIds(Pair pair){
        this.ids = this.ids.concat("+"+pair.ids);
    }
}
