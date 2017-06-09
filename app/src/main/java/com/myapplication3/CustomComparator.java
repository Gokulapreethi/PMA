package com.myapplication3;

import java.util.Comparator;

/**
 * Created by thirumal on 11-07-2016.
 */
public class CustomComparator implements Comparator<ContactBean> {
    @Override
    public int compare(ContactBean p1, ContactBean p2) {
        if(p1!=null && p1.getFirstname()!=null && p2!=null && p2.getFirstname()!=null)
        return p1.getFirstname().compareToIgnoreCase(p2.getFirstname());
        else
            return 0;
    }
}



