package com.shalya.diploma.knapsack;

import java.util.List;

public class PairArrayUtils {
    public static List<Pair> copyPairList(List<Pair> from, List<Pair> to){
        for (int i = 0; i < from.size(); i++) {
            int val = to.get(i).getValue();
            String ids = to.get(i).getIds();

            to.get(i).value = from.get(i).value;
            to.get(i).ids = to.get(i).ids.concat("+"+from.get(i).ids);
        }
        return to;
    }
}
