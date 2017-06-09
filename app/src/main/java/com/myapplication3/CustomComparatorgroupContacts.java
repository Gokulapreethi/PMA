package com.myapplication3;

import com.myapplication3.expand.Child;
import com.myapplication3.expand.Group;

import java.util.Comparator;

/**
 * Created by thirumal on 12-07-2016.
 */
public class CustomComparatorgroupContacts implements Comparator<Child> {
    @Override
    public int compare(Child p1, Child p2) {
        return p1.getName().compareToIgnoreCase(p2.getName());
    }

}
