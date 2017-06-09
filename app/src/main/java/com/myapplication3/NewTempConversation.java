package com.myapplication3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by prasanth on 1/28/2017.
 */
public class NewTempConversation extends Activity {
    String toUserName,userName,taskType,task_catagory,webtaskId;
    int toUserId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_tempconversation);
        Log.d("NewTempConversation"," NewTempConversation ");
        toUserId = getIntent().getExtras().getInt("toUserId");
        toUserName = getIntent().getExtras().getString("toUserName");
        webtaskId=getIntent().getExtras().getString("parentId");
        taskType = getIntent().getExtras().getString("type");
        task_catagory = getIntent().getExtras().getString("task");

        Intent intent = new Intent(this, NewTaskConversation.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra("toUserId", toUserId);
        intent.putExtra("parentId", webtaskId);
        intent.putExtra("toUserName", toUserName);
        intent.putExtra("task", "newissue");
        intent.putExtra("type", taskType);
        finish();
        startActivity(intent);

    }
}
