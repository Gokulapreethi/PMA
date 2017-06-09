package com.myapplication3;

/**
 * Created by thirumal on 30-11-2016.
 */
public class WeekdayBean {
    String weekday;
    boolean isSelected;
    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getWeekday() {
        return weekday;
    }

    public void setWeekday(String weekday) {
        this.weekday = weekday;
    }
}
