package com.company;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

public class StandardDeviation {

    List<Double> list = new ArrayList<Double>();
    int size;

    public StandardDeviation(ArrayList<Double> list, int _size)
    {
        this.list = list;
        this.size = _size;
    }

    double getMean()
    {
        double sum = 0.0;
        for(int i = 0; i < size; i++)
        {
            sum += list.get(i);
        }
        return sum/size;
    }

    double getVariance()
    {
        double mean = getMean();
        double temp = 0;
        double a = 0;
        for(int i = 0; i < size; i++)
        {
            a = list.get(i);
            temp += (a-mean)*(a-mean);
        }
        return temp/(size-1);
    }

    double getStdDev()
    {
        return Math.sqrt(getVariance());
    }
}
