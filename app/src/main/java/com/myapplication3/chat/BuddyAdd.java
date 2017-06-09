package com.myapplication3.chat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;


import com.myapplication3.ContactBean;
import com.myapplication3.R;

import org.pjsip.pjsua2.app.MainActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BuddyAdd extends AppCompatActivity {

    private Button back,addBuddy;
    private ListView lv_buddylist;
    public ArrayList<Map<String, String>> buddy_list;
    public ArrayList<ContactBean> contactBeen;
    private BuddyAddAdapter buddyAddAdapter;
    Context context;
    private ArrayList<String> chatUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_buddy_add);
        context=this;
        Intent intent=getIntent();
        if(intent.getStringArrayListExtra("userlist")!=null){
            Log.i("buddyadd","intent.getStringArrayListExtra(userlist)!=null");
            chatUsers=intent.getStringArrayListExtra("userlist");
        }else{
            chatUsers=new ArrayList<>();
        }
        back=(Button)findViewById(R.id.btn_back);
        addBuddy=(Button)findViewById(R.id.btn_addbuddy);
        lv_buddylist=(ListView)findViewById(R.id.buddy_list);
        buddy_list=new ArrayList<>();
        contactBeen=new ArrayList<ContactBean>();
        for (int i = 0; i < MainActivity.account.buddyList.size(); i++) {
//            if(chatUsers!=null && chatUsers.size()>0) {
//                for(String buddyUri:chatUsers) {
//                    if(!buddyUri.equalsIgnoreCase(MainActivity.account.buddyList.get(i).cfg.getUri())) {
                        buddy_list.add(putData(MainActivity.account.buddyList.get(i).cfg.getUri(),
                                MainActivity.account.buddyList.get(i).getStatusText(), "unselected"));
//                    }
//                }
//            }else{
//                buddy_list.add(putData(MainActivity.account.buddyList.get(i).cfg.getUri(),
//                        MainActivity.account.buddyList.get(i).getStatusText(), "unselected"));
//            }
        }
        if(chatUsers!=null && chatUsers.size()>0){
            outerloop:
            for(String buddyUri:chatUsers){
                Log.i("buddyadd","chatUser-->"+buddyUri);
            innerloop:
                for(int i=0;i<buddy_list.size();i++) {
                    HashMap<String, String> item = (HashMap<String,String>)buddy_list.get(i);
                    String buddy_uri = item.get("uri");
                    Log.i("buddyadd","Allbuddies-->"+buddy_uri);
                    if(buddyUri.equalsIgnoreCase(buddy_uri)){
                        Log.i("buddyadd","remove buddy-->"+buddy_uri);
                        buddy_list.remove(item);
                        break innerloop;
                    }
                }
            }
        }
        buddyAddAdapter = new BuddyAddAdapter(context, contactBeen);
        lv_buddylist.setAdapter(buddyAddAdapter);
        lv_buddylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String, String> item = buddy_list.get(position);
                String buddy_uri = item.get("uri");
                if (item.get("selected").equals("selected")) {
                    item.remove("selected");
                    item.put("selected", "unselected");
                } else {
                    item.remove("selected");
                    item.put("selected", "selected");
                }
                buddyAddAdapter.notifyDataSetChanged();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        addBuddy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> selectedBuddy=new ArrayList<String>();
                for (int position = 0; position < buddy_list.size(); position++) {
                    Map<String, String> item = buddy_list.get(position);
                    if (item.get("selected").equals("selected")) {
                        item.remove("selected");
                        item.put("selected", "unselected");
                        String buddy_uri = item.get("uri");
                        selectedBuddy.add(buddy_uri);
                        Log.i("buddyadd", "Selected user-->" + buddy_uri);
                    }
                }
                if(selectedBuddy.size()>0) {
                    Intent data = new Intent();
                    data.putExtra("RESULT", selectedBuddy);
                    setResult(RESULT_OK, data);
                    BuddyAdd.this.finish();
                }else{
                    Toast.makeText(context,"Please select the user",Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    private HashMap<String, String> putData(String uri, String status, String isSelected) {
        HashMap<String, String> item = new HashMap<String, String>();
        item.put("uri", uri);
        item.put("status", status);
        item.put("selected", isSelected);
        return item;
    }
}
