package com.myapplication3;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;

/**
 * Created by dinesh on 5/26/2016.
 */
public class ChangeHostAdapter extends ArrayAdapter<CallerDetailsBean> {

    private ArrayList<CallerDetailsBean> listData;

    private LayoutInflater layoutInflater;

    public ChangeHostAdapter(Context context,
                                   ArrayList<CallerDetailsBean> listData) {

        super(context, R.layout.changehostdialogrow, listData);

        this.listData = listData;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public CallerDetailsBean getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        Log.i("swn", "came to getview 123");

        View rowView = convertView;
        try {
            if (rowView == null)
                rowView = layoutInflater.inflate(R.layout.changehostdialogrow,
                        parent, false);

            final CallerDetailsBean callerBean = (CallerDetailsBean) listData.get(position);

            TextView userName_tv = (TextView) rowView
                    .findViewById(R.id.username);
            String username = callerBean.getUser_name();
            userName_tv.setText(username);

        } catch (Exception e) {
            e.printStackTrace();

        }
        return rowView;
    }

}
