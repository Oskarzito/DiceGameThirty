package com.bignerdranch.android.dicegamethirty;

import java.util.Comparator;

/**
 * Created by Oskar Emilsson
 */

//Sort all played rounds by rule name/sum to aim for (Low, 4, 5, 6, 7 ...)
public class ResultComparator implements Comparator<Round> {

    @Override
    public int compare(Round r1, Round r2){
        return r1.getSumToReach() - r2.getSumToReach();
    }

}
