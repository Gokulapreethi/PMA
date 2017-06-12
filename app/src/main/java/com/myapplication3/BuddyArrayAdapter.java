package com.myapplication3;

import android.content.Context;
import android.graphics.Color;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by saravanakumar on 8/8/2016.
 */
public class BuddyArrayAdapter extends ArrayAdapter<ContactBean> {

    ArrayList<ContactBean> arrayBuddyList;
    LayoutInflater inflater = null;
    Context adapContext;
    ImageLoader imageLoader1;
    String OracleMembers;

    public BuddyArrayAdapter(Context context, ArrayList<ContactBean> buddyList1) {
        super(context, R.layout.buddy_adapter_row, buddyList1);
        // TODO Auto-generated constructor stub
        arrayBuddyList = buddyList1;
        adapContext = context;
        imageLoader1 = new ImageLoader(adapContext);
    }

    public View getView(int pos, View conView, ViewGroup group) {
        try {
            if (conView == null) {
                inflater = (LayoutInflater) adapContext
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                conView = inflater.inflate(R.layout.buddy_adapter_row, null,
                        false);
            }
            final ContactBean contactBean = arrayBuddyList.get(pos);

            TextView userName = (TextView) conView.findViewById(R.id.username);
            TextView count = (TextView) conView.findViewById(R.id.item_counter);
            ImageView imageView = (ImageView) conView.findViewById(R.id.view4);
            View statusIcon = (View) conView.findViewById(R.id.status_icon);
//                View arrow_layout=(RelativeLayout)conView.findViewById(R.id.arrow_layout);
            TextView buddy_status = (TextView) conView.findViewById(R.id.status);
            ImageView state = (ImageView) conView.findViewById(R.id.iv_txtstatus1);
            state.setVisibility(View.INVISIBLE);
            TextView imageView1 = (TextView) conView.findViewById(R.id.Addobserverbutton);
            imageView1.setVisibility(View.VISIBLE);

            int msgCount = contactBean.getMsg_count();

            if (msgCount == 0) {
                count.setVisibility(View.GONE);
            } else {
                count.setVisibility(View.VISIBLE);
                count.setText(String.valueOf(msgCount));
            }
            statusIcon.setVisibility(View.INVISIBLE);

            if (contactBean.getProfileImage() != null) {
                // Picasso.with(getContext()).load("http://122.165.92.171:8080/uploads/highmessaging/user/" + contactBean.getProfileImage()).into(imageView);
                Log.i("profiledownload", "image name " + contactBean.getProfileImage());
//                File myFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/High Message/profilePic/" + contactBean.getProfileImage());
                imageLoader1.DisplayImage(Environment.getExternalStorageDirectory().getAbsolutePath() + "/High Message/profilePic/" + contactBean.getProfileImage(), imageView, R.drawable.personimage);

            } else {
                imageView.setBackgroundResource(R.drawable.personimage);
            }
//                File myFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/High Message/profilePic/" + contactBean.getProfileImage());
            imageLoader1.DisplayImage(Environment.getExternalStorageDirectory().getAbsolutePath() + "/High Message/profilePic/" + contactBean.getProfileImage(), imageView, R.drawable.personimage);
//                imageLoader1.DisplayImage(myFile.toString(), imageView,R.id.view4);
            //Picasso.with(getContext()).load("http://122.165.92.171:8080/uploads/highmessaging/user/" + contactBean.getProfileImage()).into(imageView);
//            AddTaskReassign addTaskReassign = (AddTaskReassign) Appreference.context_table.get("AddTaskReassign");
//            if (addTaskReassign != null) {
//                 OracleMembers=addTaskReassign.getOracleProjMembersList();
//            }

            if (contactBean.getUsername().equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                userName.setText("Me");
            } else if (contactBean.getFirstname() != null && contactBean.getLastname() != null ) {
                userName.setText(contactBean.getFirstname() + " " + contactBean.getLastname());
            }

            if (contactBean.getIscheck()) {
                userName.setTextColor(Color.BLUE);
                Log.i("observer", "check if " + contactBean.getIscheck());
                imageView1.setBackgroundResource(R.color.red_color);
                imageView1.setText("Remove");
            } else {
                userName.setTextColor(Color.BLACK);
                Log.i("observer", "check " + contactBean.getIscheck());
                imageView1.setBackgroundResource(R.color.blue);
                imageView1.setText("Add");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return conView;
    }

    @Override
    public Filter getFilter() {
            /*if (filter == null) {
                filter = new ContactsFilter();
            }
            return filter;*/
        return null;
    }
}
