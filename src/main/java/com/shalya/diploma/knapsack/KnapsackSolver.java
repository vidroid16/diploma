package com.shalya.diploma.knapsack;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.security.core.parameters.P;

import java.util.*;

import static java.lang.Math.*;

@Data
@Slf4j
public class KnapsackSolver {
    List<List<Packable>> items;
    int[][] matrix;
    int maxWeight;

    public KnapsackSolver(List<List<Packable>> items, int maxWeight) {
        this.items = items;
        this.matrix = new int[items.size()][maxWeight+1];
        this.maxWeight = maxWeight;
    }

    public String solve(){
        int[] last = new int[maxWeight+1];
        for (int i = 0; i < maxWeight+1; i++) {
            last[i] = -1;
        }
        for (int i = 0; i < items.get(0).size(); ++i) { //Идем по массиву 1 категории
            if (items.get(0).get(i).getWeight() < maxWeight){
                last[items.get(0).get(i).getWeight()] = max(last[items.get(0).get(i).getWeight()], items.get(0).get(i).getValue());
            }
        }
        matrix[0] = ArrayUtils.clone(last);

        int[] current = new int[maxWeight+1];

        for (int i = 1; i < items.size(); ++i) { // Проходочка по всему
            for (int j = 0; j < maxWeight+1; j++) {
                current[i] = -1;
            }// Заполнить массиыы с начала до конца -1
            for (int j = 0; j < items.get(i).size(); ++j) {
                for (int k = items.get(i).get(j).getWeight(); k <= maxWeight; ++k) {
                    if (last[k - items.get(i).get(j).getWeight()] > 0){
                        current[k] = max(current[k],
                                last[k - items.get(i).get(j).getWeight()] + items.get(i).get(j).getValue());
                    }
                }
            }
            matrix[i] = ArrayUtils.clone(current);
            last = ArrayUtils.clone(current);
        }
        Arrays.sort(current);
        return current[current.length-1]+"!";
    }

    public List<Packable> restoreItems(){
        ArrayList<Packable> result = new ArrayList<>();
        int startValueIndex = PairArrayUtils.getIndexOfLargest(matrix[matrix.length-1]);
        int startValue = matrix[matrix.length-1][startValueIndex];
        if (startValue == -1)
            return null;
        for (int i = matrix.length-2; i >= 0; i--) {
            for (int j = 0; j < items.get(i+1).size(); j++) {
                if (startValueIndex - items.get(i+1).get(j).getWeight()<0)
                    continue;
                if ((startValue-items.get(i+1).get(j).getValue()) == matrix[i][startValueIndex - items.get(i+1).get(j).getWeight()]){
                    startValueIndex = startValueIndex - items.get(i+1).get(j).getWeight();
                    startValue = matrix[i][startValueIndex];
                    result.add(items.get(i+1).get(j));
                    break;
                }
            }
        }
        for (int i = 0; i <items.get(0).size(); i++) {
            if(items.get(0).get(i).getWeight() == startValueIndex && items.get(0).get(i).getValue()==startValue){
                result.add(items.get(0).get(i));
                break;
            }

        }
        return result;
    }
    public List<Packable> restoreItemsTop(int top){
        ArrayList<Packable> result = new ArrayList<>();
        int startValueIndex = PairArrayUtils.indexesOfTopElements(matrix[matrix.length-1], top)[0];
        int startValue = matrix[matrix.length-1][startValueIndex];
        if (startValue == -1)
            return null;
        for (int i = matrix.length-2; i >= 0; i--) {
            for (int j = 0; j < items.get(i+1).size(); j++) {
                if (startValueIndex - items.get(i+1).get(j).getWeight()<0)
                    continue;
                if ((startValue-items.get(i+1).get(j).getValue()) == matrix[i][startValueIndex - items.get(i+1).get(j).getWeight()]){
                    startValueIndex = startValueIndex - items.get(i+1).get(j).getWeight();
                    startValue = matrix[i][startValueIndex];
                    result.add(items.get(i+1).get(j));
                    break;
                }
            }
        }
        for (int i = 0; i <items.get(0).size(); i++) {
            if(items.get(0).get(i).getWeight() == startValueIndex && items.get(0).get(i).getValue()==startValue){
                result.add(items.get(0).get(i));
                break;
            }

        }
        return result;
    }

}
