package com.tar.dominoPlusMinus.model;

import java.io.Serializable;
import java.util.List;

/**
 * User: goblin72
 * Date: 11.02.2015
 * Time: 13:26
 */
public class ListOfInteger implements Serializable{
    Integer[] integers;

    public ListOfInteger(List<Integer> integers) {
        this.integers = new Integer[integers.size()];
        int j=0;
        for (Integer i:integers) {
            this.integers[j]=i;
            j++;
        }

    }

    public Integer[] getIntegers() {
        return integers;
    }
}
