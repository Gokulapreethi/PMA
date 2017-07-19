package com.ase;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;

/**
 * Created by Rathika on 2/9/2016.
 */
@SuppressLint("NewApi")
public class Numberpicker extends NumberPicker {

    Typeface type;

    public Numberpicker(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    @Override
    public void addView(View child) {
        super.addView(child);
        updateView(child);
    }

    @Override
    public void addView(View child, int index,
                        android.view.ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
//        type = Typeface.createFromAsset(getContext().getAssets(),
//                "fonts/HelveticaNeue.ttf");
        updateView(child);
    }

    @Override
    public void addView(View child, android.view.ViewGroup.LayoutParams params) {
        super.addView(child, params);

//        type = Typeface.createFromAsset(getContext().getAssets(),
//                "fonts/HelveticaNeue.ttf");
        updateView(child);
    }

    private void updateView(View view) {

        if (view instanceof EditText) {
            ((EditText) view).setTypeface(type);
            ((EditText) view).setTextSize(20);

        }

    }

}