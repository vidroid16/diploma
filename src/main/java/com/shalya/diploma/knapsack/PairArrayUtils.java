package com.shalya.diploma.knapsack;

import java.util.Arrays;
import java.util.List;

public class PairArrayUtils {
    public static List<Pair> copyPairList(List<Pair> from, List<Pair> to){
        for (int i = 0; i < from.size(); i++) {
            int val = to.get(i).getValue();
            String ids = to.get(i).getIds();

            to.get(i).value = from.get(i).value;
            to.get(i).ids = to.get(i).ids.concat("+"+from.get(i).ids);

            to.get(i).setIdSet(from.get(i).getIdSet());
        }
        return to;
    }
    public static int getIndexOfLargest( int[] array )
    {
        if ( array == null || array.length == 0 ) return -1; // null or empty

        int largest = 0;
        for ( int i = 1; i < array.length; i++ )
        {
            if ( array[i] > array[largest] ) largest = i;
        }
        return largest; // position of the first largest found
    }
    static int[] indexesOfTopElements(int[] orig, int nummax) {
        int[] copy = Arrays.copyOf(orig,orig.length);
        Arrays.sort(copy);
        int[] honey = Arrays.copyOfRange(copy,copy.length - nummax, copy.length);
        int[] result = new int[nummax];
        int resultPos = 0;
        for(int i = 0; i < orig.length; i++) {
            int onTrial = orig[i];
            int index = Arrays.binarySearch(honey,onTrial);
            if(index < 0) continue;
            result[resultPos++] = i;
        }
        return result;
    }
}
