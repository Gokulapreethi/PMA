package com.ase;

/**
 * Created by thirumal on 25-11-2016.
 */
public class SwipeBean {
    public int getAdapterValue() {
        return AdapterValue;
    }

    public void setAdapterValue(int adapterValue) {
        AdapterValue = adapterValue;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    int AdapterValue;
    boolean isSelected;
}
