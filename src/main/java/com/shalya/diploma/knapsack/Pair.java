package com.shalya.diploma.knapsack;

import lombok.Data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

@Data
public class Pair{
    public int value;
    public String ids;
    private HashSet<Long> idSet;
    private ArrayList<Long> lArr;
    private ArrayList<Long> cArr;

    public Pair(int value) {
        this.value = value;
        ids = "";
        idSet = new HashSet<>();
    }

    void addNewPackable(Packable packable, int cat){
        idSet.add(packable.getId());
        if (!ids.equals(""))
            ids = ids.concat("+"+cat+"c"+packable.getId());
        else
            ids = ids.concat(""+packable.getId());
    }
    void concatIds(Pair pair){
        Collections.addAll(idSet,pair.getIdSet().toArray(new Long[0]));
        this.ids = pair.ids.concat("+"+this.ids);
    }
}
