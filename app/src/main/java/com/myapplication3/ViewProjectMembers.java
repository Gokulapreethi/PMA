package com.myapplication3;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.myapplication3.DB.VideoCallDataBase;

import java.util.ArrayList;

import json.ListMember;

/**
 * Created by vadivel on 03/15/17.
 */
public class ViewProjectMembers extends AppCompatActivity implements View.OnClickListener {
    String projectId, project_Owner, sub_type, isProject;
    ArrayList<ListMember> arrayList, arrayList_1;
    ListView listView;
    TextView back, header_name;
    Button submit, private_cancel;
    LayoutInflater inflater = null;
    Context context;
    private final Handler handler = new Handler();
    ArrayList<String> chatUsers, chatUsersName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_percentage_status);
        context = this;
        Appreference.context_table.put("viewProjectMembers", this);
//        Appreference.is_swipe = true;
        listView = (ListView) findViewById(R.id.list_grp_mems);
        back = (TextView) findViewById(R.id.cancel);
        header_name = (TextView) findViewById(R.id.txtView02);
//        submit = (Button) findViewById(R.id.submit);
        private_cancel = (Button) findViewById(R.id.private_cancel);
        private_cancel.setOnClickListener(this);
        header_name.setText("View Members");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (getIntent() != null) {
            projectId = getIntent().getStringExtra("projectId");
            project_Owner = getIntent().getStringExtra("projectOwner");
//            sub_type = getIntent().getStringExtra("subtype");
//            isProject = getIntent().getStringExtra("isProject");
        }
//        if (sub_type != null && sub_type.equalsIgnoreCase("private")) {
//            submit.setVisibility(View.VISIBLE);
//        }
//        submit.setOnClickListener(this);
        arrayList = new ArrayList<>();
        arrayList_1 = new ArrayList<>();
//        if (isProject.equalsIgnoreCase("yes")) {
        String ListofMem = VideoCallDataBase.getDB(context).getProjectParentTaskId("select taskMemberList from projectHistory where taskId==parentTaskId");
            /*while (ListofMem.contains(",")) {
                ListofMem = ListofMem.substring(",".length());
            }
            if (ListofMem.contains(",")) {
                Log.i("ListMembers", "length is " + ListofMem.split(",").length);
            }*/
        Log.i("viewProjectMembers", "project_Owner is " + project_Owner);
        Log.i("viewProjectMembers", "ListofMem is " + ListofMem);
//        if (!ListofMem.contains(project_Owner)) {
//            Log.i("viewProjectMembers", "ListofMem if " + ListofMem);
//            ListofMem = ListofMem.concat("," + project_Owner);
//        } else {
//            Log.i("viewProjectMembers", "ListofMem else " + ListofMem);
//            ListofMem = ListofMem.concat("," + Appreference.loginuserdetails.getUsername());
//        }
        Log.i("viewProjectMembers", "ListofMem after " + ListofMem);
        int counter = 0;
        for (int i = 0; i < ListofMem.length(); i++) {
            if (ListofMem.charAt(i) == ',') {
                counter++;
            }
            Log.d("viewProjectMembers", "Task Mem's counter size is == " + counter);
        }
        for (int j = 0; j < counter + 1; j++) {
            ListMember listMemberr_1 = new ListMember();
            String Mem_name = ListofMem.split(",")[j];
            Log.i("viewProjectMembers", "Task Mem's and position == " + Mem_name + " " + j);
            if (project_Owner != null && project_Owner.equalsIgnoreCase(Mem_name)) {
                if (Mem_name.equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                    listMemberr_1.setUsername("Me (Giver)");
                } else {
                    listMemberr_1.setUsername(VideoCallDataBase.getDB(context).getname(Mem_name) + " (Giver)");
                }
            } else {
                listMemberr_1.setUsername(VideoCallDataBase.getDB(context).getname(Mem_name));
            }
            arrayList.add(listMemberr_1);
        }
        ArrayList<String> ListofObservers = new ArrayList<>();
        ListofObservers = VideoCallDataBase.getDB(context).selectGroupmembers("select * from projectHistory where projectId='" + projectId + "' and taskObservers <> 'null' and taskObservers IS NOT NULL and taskObservers != ''", "taskObservers");
        Log.i("viewProjectMembers", "ListofObservers size is " + ListofObservers.size());
        ArrayList<String> listofObs = new ArrayList<>();

        for (String str : ListofObservers) {
            Log.i("viewProjectMembers", "observer str " + str);
            if (str != null && !str.equalsIgnoreCase(null) && !str.equalsIgnoreCase("") && !str.equalsIgnoreCase("null") && str.contains(",")) {
                int counter_1 = 0;
                for (int i = 0; i < str.length(); i++) {
                    if (str.charAt(i) == ',') {
                        counter_1++;
                    }
                }
                Log.d("viewProjectMembers", "Task Mem's counter size is == " + counter_1);
                for (int j = 0; j < counter_1 + 1; j++) {
                    if (counter_1 == 0) {
                        if (str != null && !ListofMem.contains(str)) {
                            listofObs.add(str);
                        }
                    } else {
                        String Mem_name_1 = str.split(",")[j];
                        Log.i("viewProjectMembers", "Mem_name_1 is " + Mem_name_1);
                        Log.i("viewProjectMembers", "ListofMem is " + ListofMem);
                        if (Mem_name_1 != null && !ListofMem.contains(Mem_name_1)) {
                            listofObs.add(Mem_name_1);
                        }
                    }
                }
            } else if (str != null && !str.equalsIgnoreCase(null) && !str.equalsIgnoreCase("") && !str.equalsIgnoreCase("null") && !ListofMem.contains(str)) {
                listofObs.add(str);
                Log.i("viewProjectMembers", "observer added");
            }
        }
        if (listofObs.size() > 0) {
            for (String str1 : listofObs) {
                Log.i("viewProjectMembers", "observer str1 " + str1);
                ListMember lm_1 = new ListMember();
                if (str1 != null) {
                    lm_1.setUsername(VideoCallDataBase.getDB(context).getname(str1) + " (Observer)");
                }
                arrayList.add(lm_1);
            }
        }
//        }
//        else {
//            Log.i("project_details", "Task group_Id == " + group_Id);
//            arrayList = VideoCallDataBase.getDB(context).getGroupmemberHistory("select * from groupmember where groupId='" + group_Id + "'");
//        }

        Log.i("viewProjectMembers", "arrayList size is " + arrayList);
        if (arrayList.size() > 0) {
            for (int k = 0; k < arrayList.size(); k++)
                Log.i("viewProjectMembers", "listmember frst_last_name_s " + arrayList.get(k).getUsername());
        }
//        if (arrayList.size() > 0) {
//            for (ListMember listMember : arrayList) {
//                if (listMember.getUsername().equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
//                    listMember.setFirstName(Appreference.loginuserdetails.getFirstName());
//                    listMember.setLastName(Appreference.loginuserdetails.getLastName());
//                } else {
//                    String frst_last_name = VideoCallDataBase.getDB(context).getName(listMember.getUsername());
//                    Log.i("project_details", "frst_lst_name " + frst_last_name);
//                    if (frst_last_name != null) {
//                        listMember.setFirstName(frst_last_name.split(" ")[0]);
//                        listMember.setLastName(frst_last_name.split(" ")[1]);
//                    }
//                }
//                arrayList_1.add(listMember);
//            }
//        }

        Log.i("viewProjectMembers", "arrayList_1" + arrayList.size());

        final MyAdapter buddyArrayAdapter = new MyAdapter(context, arrayList);
        listView.setAdapter(buddyArrayAdapter);
        buddyArrayAdapter.notifyDataSetChanged();

        /*listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListMember item = arrayList.get(position);
                String buddy_uri = item.getUsername();
                Log.i("sipTest 1", "size :" + arrayList.size());
                if (!item.getIscheck()) {
                    Log.i("sipTest 1", "Inside True" + position);
                    item.setIscheck(true);

                } else {
                    Log.i("sipTest 1", "Inside False" + position);
                    item.setIscheck(false);
                }
                buddyArrayAdapter.notifyDataSetChanged();
            }
        });*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (Appreference.context_table.containsKey("grouppercentagestatus")) {
            Appreference.context_table.remove("grouppercentagestatus");
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

//        if (id == R.id.submit) {
//            submitClickEvent();
//            return;
//        }
        if (id == R.id.private_cancel) {
            cancelClilckEvent();
            return;
        }

    }

    private void cancelClilckEvent() {
        finish();
    }

   /* private void submitClickEvent() {
        chatUsers = new ArrayList<String>();
        chatUsersName = new ArrayList<>();
        for (int position = 0; position < arrayList.size(); position++) {
            ListMember item = arrayList.get(position);
            if (item.getIscheck()) {
                String buddy_uri = String.valueOf(item.getUserid());
                String buddy_Name = item.getUsername();
                chatUsers.add(buddy_uri);
                chatUsersName.add(buddy_Name);
                Log.i("private message", "chat Selected user--> " + buddy_uri);
                Log.i("private message", "chatUsers " + chatUsers);
                Log.i("private message", "chatUsersName " + chatUsersName);
                Intent i = new Intent();
//                chatUsers = new ArrayList<String>();
//                chatUsers = i.getStringArrayListExtra("chatUsers");
                i.putStringArrayListExtra("chatUsers", chatUsers);
                i.putStringArrayListExtra("chatUsersName", chatUsersName);
                setResult(RESULT_OK, i);
                finish();
            }
        }
    }*/

    public class MyAdapter extends ArrayAdapter<ListMember> {

        private final Context context;
        private final ArrayList<ListMember> listMembers;

        public MyAdapter(Context context, ArrayList<ListMember> listMembers_1) {
            super(context, R.layout.group_percent_status, listMembers_1);

//            super(context,R.layout.group_percent_status,0,listMembers_1);

            this.context = context;
            this.listMembers = listMembers_1;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            try {
                if (convertView == null) {
                    // 1. Create inflater
                    inflater = (LayoutInflater) context
                            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                    // 2. Get rowView from inflater
                    convertView = inflater.inflate(R.layout.group_percent_status, null, false);
                }

                // 3. Get the two text view from the rowView
                final ListMember contactBean = listMembers.get(position);
                TextView frst_lst_name = (TextView) convertView.findViewById(R.id.frst_lst_name);
                TextView percent_status = (TextView) convertView.findViewById(R.id.percent_status);
                ImageView image_1 = (ImageView) convertView.findViewById(R.id.personImage);
                image_1.setVisibility(View.VISIBLE);
                percent_status.setVisibility(View.GONE);
//                String ft_lt_name = contactBean.getFirstName() + " " + contactBean.getLastName();
                Log.i("project_details", "project_Owner " + project_Owner);
                Log.i("project_details", "contactBean.getUsername() " + contactBean.getUsername());
//                String ft_lt_name = null;
//                if (contactBean.getUsername() != null) {
//                    ft_lt_name = VideoCallDataBase.getDB(context).getname(contactBean.getUsername());
//                }
//                if (project_Owner != null && project_Owner.equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
//                    frst_lst_name.setText("Me (Giver)");
//                } else if (project_Owner != null && !project_Owner.equalsIgnoreCase(Appreference.loginuserdetails.getUsername()) && project_Owner.equalsIgnoreCase(contactBean.getUsername())) {
//                    frst_lst_name.setText(ft_lt_name + " (Giver)");
//                } else {
                frst_lst_name.setText(contactBean.getUsername());
//                }
//                if (sub_type != null && sub_type.equalsIgnoreCase("normal")) {
//                    percent_status.setText(contactBean.getCode() + "%");
//                } else {
//                    percent_status.setVisibility(View.GONE);
//            }
//                if (contactBean.getIscheck()) {
//                    frst_lst_name.setTextColor(getResources().getColor(R.color.appcolor));
//                    Log.i("sipTest", "adapter true " + contactBean.getIscheck() + " pos :" + position);
//                } else {
//                    frst_lst_name.setTextColor(getResources().getColor(R.color.black));
//                    Log.i("sipTest", "adapter false " + contactBean.getIscheck() + " pos :" + position);
//                }
                // 5. return rowView
            } catch (Exception e) {
                e.printStackTrace();
            }
            return convertView;
        }
    }
}
