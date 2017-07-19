package com.ase.escalations;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.ase.BuddyArrayAdapter;
import com.ase.ContactBean;
import com.ase.CustomComparator;
import com.ase.DB.VideoCallDataBase;
import com.ase.R;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by prasanth on 3/24/2017.
 */
public class AddEscalation extends Activity implements View.OnClickListener {
    static AddEscalation addEscalation;
    Button submit, cancel;
    ListView listView;
    Context context;
    BuddyArrayAdapter buddyArrayAdapter;
    Handler handler;
    ArrayList<String> usersId, UsersName;
    ArrayList<ContactBean> contactBeans;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_escalationlist);
        context = this;
        addEscalation = this;
        submit = (Button) findViewById(R.id.submit);
        cancel = (Button) findViewById(R.id.cancel);
        listView = (ListView) findViewById(R.id.post_list);
        handler = new Handler();
        contactBeans = new ArrayList<>();

        submit.setOnClickListener(this);
        cancel.setOnClickListener(this);


        final ArrayList<String> contact_list = getIntent().getExtras().getStringArrayList("contactList");
        try {
//            String old_names = getIntent().getExtras().getString("taskId");
            String selectd_names = getIntent().getExtras().getString("listmember");

            Log.i("escalation ", "user list --> : " + contact_list.size());

            String[] observerList = selectd_names.split(",");
//            String projectValue = getIntent().getExtras().getString("projectValue");
//            if (projectValue.equalsIgnoreCase("yes")) {
//                old_names = VideoCallDataBase.getDB(context).getProjectParentTaskId("select taskObservers from projectHistory where taskId='" + old_names + "'");
//            }
//            Log.i("escalation ", "old names --> : " + old_names);
//            Log.i("escalation ", "selectd list --> : " + selectd_names);
//
//            String[] observerList1 = old_names.split(",");
//            if (observerList1 != null) {
//                Log.i("addobserver", "task observer size" + observerList.length);
//                for (String taskDetailsBean2 : observerList1) {
//                    if (contact_list.contains(taskDetailsBean2)) {
//                        Log.i("taskobserver", "equal" + taskDetailsBean2);
//                        contact_list.remove(taskDetailsBean2);
//                    }
//                }
//            }
            if (contact_list.size() > 0) {
                for (int i = 0; i < contact_list.size(); i++) {
                    ContactBean contactBean = new ContactBean();
                    contactBean.setUsername(contact_list.get(i));
                    String firstname = VideoCallDataBase.getDB(context).getProjectParentTaskId("select firstname from contact where username='" + contactBean.getUsername() + "'");
                    String lastname = VideoCallDataBase.getDB(context).getProjectParentTaskId("select lastname from contact where username='" + contactBean.getUsername() + "'");
                    Log.i("escalation", "");
                    contactBean.setFirstname(firstname);
                    contactBean.setLastname(lastname);
                    Log.i("escalation", "username : " + contactBean.getUsername());
                    Log.i("escalation", "firstname : " + contactBean.getFirstname() + "lastname " + contactBean.getLastname());
                    if (observerList != null) {
                        Log.i("addobserver", "task observer size" + observerList.length);
                        for (String taskDetailsBean2 : observerList) {
                            Log.i("taskobserver", contactBean.getUsername() + "is equal ?" + taskDetailsBean2);
                            if (contactBean.getUsername().equalsIgnoreCase(taskDetailsBean2)) {
                                Log.i("taskobserver", contactBean.getUsername() + "equal" + taskDetailsBean2);
                                contactBean.setIscheck(true);
                            }
                        }
                    }
                    contactBeans.add(contactBean);
                }
            }


            Collections.sort(contactBeans, new CustomComparator());
            buddyArrayAdapter = new BuddyArrayAdapter(context, contactBeans);
            listView.setAdapter(buddyArrayAdapter);
//            buddyArrayAdapter.notifyDataSetChanged();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Collections.sort(contactBeans, new CustomComparator());
                    buddyArrayAdapter.notifyDataSetChanged();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ContactBean item = contactBeans.get(position);
                String buddy_uri = item.getUsername();
                if (!item.getIscheck()) {
                    Log.e("sipTest", "Inside True 0");
                    item.setIscheck(true);
                } else {
                    Log.e("sipTest", "Inside False 0");
                    item.setIscheck(false);
                }
                buddyArrayAdapter.notifyDataSetChanged();
            }
        });
    }

    public void submitClickEvent() {
        usersId = new ArrayList<>();
        UsersName = new ArrayList<>();
        for (int position = 0; position < contactBeans.size(); position++) {
            ContactBean item = contactBeans.get(position);
            if (item.getIscheck()) {
                String buddy_uri = String.valueOf(item.getUserid());
                String buddy_Name = item.getUsername();
                usersId.add(buddy_uri);
                UsersName.add(buddy_Name);
                Log.i("escalation", "chat Selected user-->" + buddy_uri);
            }
        }
        Intent i = new Intent();
        i.putStringArrayListExtra("userid", usersId);
        i.putStringArrayListExtra("UsersName", UsersName);
        setResult(RESULT_OK, i);
        finish();
    }

    public void cancelClilckEvent() {
        finish();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.submit) {
            submitClickEvent();
            return;
        }
        if (id == R.id.cancel) {
            cancelClilckEvent();
            return;
        }


    }

}
