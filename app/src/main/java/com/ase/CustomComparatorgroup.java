package com.ase;


import com.ase.expand.Group;

import java.util.Comparator;

/**
 * Created by thirumal on 11-07-2016.
 */
public class CustomComparatorgroup implements Comparator<Group> {
    @Override
    public int compare(Group p1, Group p2) {
        return p1.getName().compareToIgnoreCase(p2.getName());
    }
}
