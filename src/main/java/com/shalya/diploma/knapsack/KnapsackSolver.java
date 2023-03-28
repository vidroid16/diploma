package com.shalya.diploma.knapsack;

import lombok.Data;

import java.util.*;

import static java.lang.Math.*;

@Data
public class KnapsackSolver {
    List<List<Packable>> items;
    int maxWeight;
    long bestLast;
    long bestCur;
    String res = "";

    public String solve(){
        ArrayList<Pair> last = new ArrayList<Pair>();
        for (int i = 0; i < maxWeight+1; i++) {
            last.add(new Pair(-1));
        }
        for (int i = 0; i < items.get(0).size(); ++i) { //Идем по массиву 1 категории
            if (items.get(0).get(i).getWeight() < maxWeight){
                if (last.get(items.get(0).get(i).getWeight()).getValue() < items.get(0).get(i).getValue())
                    last.get(items.get(0).get(i).getWeight()).addNewPackable(items.get(0).get(i));
                last.get(items.get(0).get(i).getWeight()).value = max(last.get(items.get(0).get(i).getWeight()).getValue(), items.get(0).get(i).getValue());

            }
        }

        res = res.concat("+"+bestLast);

        ArrayList<Pair> current = new ArrayList<Pair>();

        for (int i = 1; i < items.size(); ++i) { // Проходочка по всему
            for (int j = 0; j < maxWeight+1; j++) {
                current.add(new Pair(-1));
            }// Заполнить массиыы с начала до конца -1
            for (int j = 0; j < items.get(i).size(); ++j) {
                for (int k = items.get(i).get(j).getWeight(); k <= maxWeight; ++k) {
                    if (last.get(k - items.get(i).get(j).getWeight()).getValue() > 0){
                        if (current.get(k).value < last.get(k - items.get(i).get(j).getWeight()).value + items.get(i).get(j).getValue()){
                            current.get(k).addNewPackable(items.get(i).get(j));
                            current.get(k).concatIds(last.get(k-items.get(i).get(j).getWeight()));
                        }
                        current.get(k).value = max(current.get(k).value,
                                last.get(k - items.get(i).get(j).getWeight()).value + items.get(i).get(j).getValue());
                    }
                }
            }
            res = res.concat("+"+bestCur);
            PairArrayUtils.copyPairList(current,last);
            //swap(current, last);
            current.clear();
        }
        last.sort(Comparator.comparingInt(Pair::getValue));
        int deb = 0;
        return last.get(last.size()-1).getIds();
    }
    private void swap(List<Pair> list1, List<Pair> list2){
        List<Pair> tmpList = list1;
        list1 = list2;
        list2 = tmpList;
    }
}
