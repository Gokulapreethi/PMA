package com.myapplication3;

import com.myapplication3.Bean.TaskDetailsBean;

import java.util.Comparator;
import java.util.Date;

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


