package com.shalya.diploma.knapsack;

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
    public static int getIndexOf2Largest( int[] array )
    {
        if ( array == null || array.length == 0 ) return -1; // null or empty

        int largest = 0;
        int largest2 = 0;
        for ( int i = 1; i < array.length; i++ )
        {
            if ( array[i] > array[largest] ) {
                largest2 = largest;
                largest = i;
            }
        }
        return largest2; // position of the first largest found
    }
}
