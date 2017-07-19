package com.ase.chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ase.ContactBean;

import com.ase.R;

import java.util.ArrayList;

/**
 * Created by dinesh on 5/9/2016.
 */
public class BuddyAddAdapter extends ArrayAdapter<ContactBean> {

    ArrayList<ContactBean> arrayBuddyList;
    LayoutInflater inflater = null;
    Context adapContext;

    public BuddyAddAdapter(Context context,
                             ArrayList<ContactBean> buddyList1) {
        super(context, R.layout.buddy_adapter_row, buddyList1);
        // TODO Auto-generated constructor stub
        arrayBuddyList = buddyList1;
        adapContext = context;
    }

    public View getView(int pos, View conView, ViewGroup group) {
        try {
            if (conView == null) {
                inflater = (LayoutInflater) adapContext
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                conView = inflater.inflate(R.layout.buddy_adapter_row, null,
                        false);
            }
            ContactBean map = arrayBuddyList.get(pos);
            String name = map.getUsername();
//            name = name.substring(4);
//            name = name.split("@")[0];
//            String status = map.get("status");
                String status="online";
//            ContactBean contactBean = arrayBuddyList.get(pos);
//            TextView userName = (TextView) conView.findViewById(R.id.username);
//            if(contactBean.getFirstname()!=null && contactBean.getLastname()!=null){
//                userName.setText(contactBean.getFirstname()+" "+contactBean.getLastname());
//            }
            TextView userName = (TextView) conView.findViewById(R.id.username);
            userName.setText(name);
            TextView userStatus = (TextView) conView.findViewById(R.id.status);
            userStatus.setText(status);
            ImageView status_icon = (ImageView) conView.findViewById(R.id.status_icon);
            if (status != null && status.equalsIgnoreCase("online")) {
                status_icon.setBackgroundResource(R.drawable.online);

            } else {
                status_icon.setBackgroundResource(R.drawable.off_line);

            }
//            if (map.get("selected").equals("unselected")) {
//                userName.setTextColor(Color.parseColor("#000000"));
//            } else {
//                userName.setTextColor(Color.parseColor("#FEC35C"));
//            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return conView;

    }
}
