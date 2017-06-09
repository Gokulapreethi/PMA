package com.myapplication3;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.myapplication3.DB.VideoCallDataBase;

import java.util.ArrayList;
import java.util.Collections;

import json.ListMember;

/**
 * Created by thirumal on 24-01-2017.
 */
public class Takerslist extends Activity {
    TextView back, done;
    CustomArrayAdapter buddyArrayAdapter;
    Handler handler;
    public String projectid, listMembers, arrayforparenttask;
    ;
    ArrayList<ContactBean> listMembers1, listofProjectMembers, SelectedList;
    ListView observerlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.project_subtask_observer);
        back = (TextView) findViewById(R.id.back);
        done = (TextView) findViewById(R.id.done);
        observerlist = (ListView) findViewById(R.id.takers_list);
        handler = new Handler();
        listMembers1 = new ArrayList<>();
        SelectedList = new ArrayList<>();
        listofProjectMembers = new ArrayList<>();
        projectid = getIntent().getStringExtra("projectid");
        setAdapter();

        buddyArrayAdapter = new CustomArrayAdapter(this, listMembers1);
        observerlist.setAdapter(buddyArrayAdapter);
        buddyArrayAdapter.notifyDataSetChanged();
        handler.post(new Runnable() {
            @Override
            public void run() {
                buddyArrayAdapter.notifyDataSetChanged();
            }
        });
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (SelectedList.size() > 0) {
                        Intent intent = new Intent(Takerslist.this, NewTaskConversation.class);
                        intent.putExtra("taskMemberList", SelectedList);
                        intent.putExtra("task", "newsubtask");
                        intent.putExtra("parenttaskid", arrayforparenttask);
                        intent.putExtra("projectid", projectid);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(Takerslist.this, "Please Select The User", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        observerlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ContactBean item = listMembers1.get(position);
                Log.i("Takerslist", "observerlist ");
                String buddy_uri = item.getUsername();
                if (!item.getIscheck()) {
                    Log.i("Takerslist", "Inside True if " + buddy_uri);
                    item.setIscheck(true);
                    SelectedList.add(item);
                } else {
                    Log.i("Takerslist", "Inside False else " + buddy_uri);
                    if (SelectedList.size() > 0) {
                        ContactBean contact = listMembers1.get(position);
                        if (SelectedList.contains(contact)) {
                            SelectedList.remove(contact);
                        }
                    }
                    item.setIscheck(false);
                }
                Log.i("Takerslist", "Inside True if " + SelectedList.size());
                buddyArrayAdapter.notifyDataSetChanged();
            }
        });
    }

    private void setAdapter() {
        String query1 = "select parentTaskId from projectHistory where projectId='" + projectid + "' order by id desc limit 1";

        arrayforparenttask = VideoCallDataBase.getDB(this).getProjectParentTaskId(query1);
//                String query2 = "select taskId from projectHistory where projectId='" + project_id + "'";
        ArrayList<String> arrayfortaskId = new ArrayList<String>();
        arrayfortaskId = VideoCallDataBase.getDB(this).getProjectTaskId(projectid);
//                for (int i = 0; i < arrayforparenttask.size(); i++) {
        for (int j = 0; j < arrayfortaskId.size(); j++) {
            if (arrayforparenttask.equalsIgnoreCase(arrayfortaskId.get(j))) {
                listMembers = VideoCallDataBase.getDB(this).getProjectListMembers(arrayfortaskId.get(j));
            }
        }

        Log.i("Takerslist", "ListMembers " + listMembers);
//                }
        int counter = 0;
        if (listMembers != null) {
            for (int k = 0; k < listMembers.length(); k++) {
                if (listMembers.charAt(k) == ',') {
                    counter++;
                }
                Log.d("project_details", "Task Mem's counter size is == " + counter);
            }

            for (int j = 0; j < counter + 1; j++) {
                Log.i("project_details", "Task Mem's and position == " + listMembers.split(",")[j] + " " + j);
                ContactBean list_Mems = new ContactBean();
                list_Mems.setUsername(listMembers.split(",")[j]);
                if (!listofProjectMembers.contains(list_Mems))
                    listofProjectMembers.add(list_Mems);

                Log.i("Takerslist", "listofProjectMembers " + listofProjectMembers);
            }
            for (int i = 0; i < listofProjectMembers.size(); i++) {
                String username = listofProjectMembers.get(i).getUsername();
                Log.i("Takerslist", "username " + username);
                if (!listMembers1.contains(VideoCallDataBase.getDB(this).getContactObject(username)))
                    listMembers1.add(VideoCallDataBase.getDB(this).getContactObject(username));
            }
        }
    }

    private class CustomArrayAdapter extends ArrayAdapter<ContactBean> {

        ArrayList<ContactBean> arrayBuddyList;
        LayoutInflater inflater = null;
        Context adapContext;
        ImageLoader imageLoader1;

        public CustomArrayAdapter(Context context, ArrayList<ContactBean> buddyList1) {
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
                ContactBean conBean = arrayBuddyList.get(pos);

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
                imageView.setVisibility(View.GONE);
                count.setVisibility(View.GONE);
                statusIcon.setVisibility(View.GONE);
                int msgCount = conBean.getMsg_count();
                Log.i("Takerslist", "conBean.getFirstname() " + conBean.getFirstname());
                userName.setText(conBean.getFirstname() + " " + conBean.getLastname());
                if (msgCount == 0) {
                    count.setVisibility(View.GONE);
                } else {
                    count.setVisibility(View.VISIBLE);
                    count.setText(String.valueOf(msgCount));
                }
                statusIcon.setVisibility(View.INVISIBLE);

         /*   statusIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(),TaskHistory.class);
                    intent.putExtra("userName",contactBean.getUsername());
                    startActivity(intent);

                }
            });*/


            /*    if (contactBean.getStatus() != null && !contactBean.getStatus().equalsIgnoreCase("")) {
                    buddy_status.setText(contactBean.getStatus());
                    state.setBackground(getResources().getDrawable(R.drawable.online1));
                }else {
                    buddy_status.setText("Offline");
                    state.setBackground(getResources().getDrawable(R.drawable.off_line));
                }*/

                if (conBean.getProfileImage() != null) {
                    // Picasso.with(getContext()).load("http://122.165.92.171:8080/uploads/highmessaging/user/" + contactBean.getProfileImage()).into(imageView);
                    Log.i("profiledownload", "image name " + conBean.getProfileImage());
//                File myFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/High Message/profilePic/" + contactBean.getProfileImage());
                    imageLoader1.DisplayImage(Environment.getExternalStorageDirectory().getAbsolutePath() + "/High Message/profilePic/" + conBean.getProfileImage(), imageView, R.drawable.personimage);

                } else {
                    imageView.setBackgroundResource(R.drawable.personimage);
                }
//                File myFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/High Message/profilePic/" + contactBean.getProfileImage());
                imageLoader1.DisplayImage(Environment.getExternalStorageDirectory().getAbsolutePath() + "/High Message/profilePic/" + conBean.getProfileImage(), imageView, R.drawable.personimage);
//                imageLoader1.DisplayImage(myFile.toString(), imageView,R.id.view4);
                //Picasso.with(getContext()).load("http://122.165.92.171:8080/uploads/highmessaging/user/" + contactBean.getProfileImage()).into(imageView);

                if (conBean.getIscheck()) {
                    userName.setTextColor(Color.BLUE);
                    Log.i("observer", "check" + conBean.getIscheck());
                    imageView1.setBackgroundResource(R.color.red_color);
                    imageView1.setText("Remove");
                } else {
                    userName.setTextColor(Color.BLACK);
                    Log.i("observer", "check" + conBean.getIscheck());
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

}
