package com.ase;

import com.ase.Bean.TaskDetailsBean;

import java.util.Comparator;

/**
 * Created by saravanakumar on 9/26/2016.
 */

    public  class DateCompare implements Comparator<TaskDetailsBean> {


    @Override
    public int compare(TaskDetailsBean lhs, TaskDetailsBean rhs) {
        if (lhs.getDateTime() == null || rhs.getDateTime() == null)
            return 0;
        return lhs.getDateTime().compareTo(rhs.getDateTime());
    }
    }


