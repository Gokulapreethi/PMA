package com.ase;


import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ase.Bean.TaskDetailsBean;
import com.ase.DB.VideoCallDataBase;

import java.util.ArrayList;
import java.util.List;

public class DisplayList extends Activity {

    SQLiteDatabase db = null;
    ListAct adapter;
    List<String> names = new ArrayList<String>();
    List<String> number = new ArrayList<String>();
    Context context;
    //    List<Contact> contacts = new ArrayList<Contact>();
    ArrayList<TaskDetailsBean> list_date;
    ListView list;
    View view1;
    boolean isFromcustom1;
    String query;
    String projectId, webtaskId, date_type, completedate_display;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_display);

        context = this;
        view1 = this.getCurrentFocus();
        list = (ListView) findViewById(R.id.travel_list);
        list_date = new ArrayList<TaskDetailsBean>();
        projectId = getIntent().getStringExtra("projectId");
        webtaskId = getIntent().getStringExtra("webtaskId");
        date_type = getIntent().getStringExtra("date_type");
        isFromcustom1 = getIntent().getBooleanExtra("isFromcustom1",false);
        completedate_display = getIntent().getStringExtra("completedate_display");
        Log.i("date", "completedate_display ==> " + completedate_display);
        if(!isFromcustom1) {
            query = "Select * from projectStatus where projectId ='" + projectId + "' and taskId = '" + webtaskId + "' and (status ='7' or status='9') and " + (("travelStartTime like '" + completedate_display + "%" + "' and travelEndTime like '" + completedate_display + "%" + "'") + " or " + ("travelStartTime like '" + completedate_display + "%" + "' and travelEndTime IS NULL") + " or " + ("travelStartTime IS NULL and travelEndTime like '" + completedate_display + "%" + "'"));
        }else
             query = "Select * from projectStatus where projectId ='" + projectId + "' and taskId = '" + webtaskId + "' and (status ='7' or status='9')";
        list_date.clear();
        list_date = VideoCallDataBase.getDB(context).getTravelDetails(query);
        Log.i("DisplayList", "query and list_date " + query + " " + list_date.size());
        adapter = new ListAct(context, list_date);
        list.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        // TODO Auto-generated method stub
    }

    public class ListAct extends ArrayAdapter<TaskDetailsBean> {
        private final Context context;
        private final List<TaskDetailsBean> values;

        public ListAct(Context context, List<TaskDetailsBean> values) {
            super(context, R.layout.listviewdisplay, values);
            this.context = context;
            this.values = values;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            TaskDetailsBean user = values.get(position);
            View rowView = inflater.inflate(R.layout.listviewdisplay, parent, false);

            if (position % 2 == 1) {
                rowView.setBackgroundColor(Color.LTGRAY);
            } else {
                rowView.setBackgroundColor(Color.WHITE);
            }

            TextView tvName = (TextView) rowView.findViewById(R.id.start_date_1);
            TextView tvHome = (TextView) rowView.findViewById(R.id.end_date_1);
            Log.i("DisplayList", "Time_start ===========>" + user.getActivityStartTime());
            Log.i("DisplayList", "Time_end ===========>" + user.getActivityEndTime());
            if (date_type != null && date_type.equalsIgnoreCase("travel_start")) {
                tvName.setText(user.getTravelStartTime());
                tvHome.setText(user.getTravelEndTime());
            } else if (date_type != null && date_type.equalsIgnoreCase("activity_date")) {
                tvName.setText(user.getActivityStartTime());
                tvHome.setText(user.getActivityEndTime());
            }/*else if(date_type!=null && date_type.equalsIgnoreCase("travel_end")){
                tvName.setText(user.getToTravelStartTime());
                tvHome.setText(user.getToTravelEndTime());
            }*/
            return rowView;
        }
    }

}
