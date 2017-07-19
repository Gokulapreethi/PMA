package com.ase;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ase.Bean.TaskDetailsBean;
import com.ase.DB.VideoCallDataBase;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by prasanth on 2/13/2017.
 */
public class NotesList extends Activity {
    static NoteArrayAdapter noteArrayAdapter;
    ListView fileListView;
    ImageView newNotes;
    TextView back, title;
    Context context;
    ArrayList<TaskDetailsBean> notelist, notelist1;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listofnotes);
        String query = null;
        context = this;
        fileListView = (ListView) findViewById(R.id.file_list);
        back = (TextView) findViewById(R.id.back);
        title = (TextView) findViewById(R.id.title);
        newNotes = (ImageView) findViewById(R.id.template);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        newNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NewTaskConversation.class);
                intent.putExtra("task", "NewNotes");
                intent.putExtra("type", "Individual");
                intent.putExtra("note", true);
                startActivityForResult(intent, 404);
            }
        });
        title.setText("Me");
        query = "select * from taskHistoryInfo where category='note' and loginuser='" + Appreference.loginuserdetails.getEmail() + "' group by taskId";
        notelist = VideoCallDataBase.getDB(context).getTaskHistoryInfo(query);
        Log.i("NoteList", "Query " + query);
        Log.i("NoteList", "Query size is " + notelist.size());
        Collections.reverse(notelist);
        noteArrayAdapter = new NoteArrayAdapter(context, notelist);
        fileListView.setAdapter(noteArrayAdapter);
        refresh();

        fileListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TaskDetailsBean taskDetailsBean = notelist.get(position);
                Log.i("NoteList", "onclick notelist " + String.valueOf(position));
                Intent intent = new Intent(context, NewTaskConversation.class);
                intent.putExtra("task", "notehistory");
                intent.putExtra("noteHistoryBean", taskDetailsBean);
                intent.putExtra("note", true);
                startActivityForResult(intent, 404);
            }
        });
    }

    public void refresh() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                noteArrayAdapter.notifyDataSetChanged();
                Log.i("template", "refreshed");
            }
        });
    }

    @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode) {
            Log.i("note", "note values " + String.valueOf(requestCode));
            if (requestCode == 404) {
                notelist.clear();
                Log.i("note", String.valueOf(requestCode));

                String query = "select * from taskHistoryInfo where category='note' and loginuser='" + Appreference.loginuserdetails.getEmail() + "' group by taskId";
                notelist1 = VideoCallDataBase.getDB(context).getTaskHistoryInfo(query);
                notelist.addAll(notelist1);
                Collections.reverse(notelist);
                Log.i("note", "note values ** " + String.valueOf(requestCode) + "  size" + notelist.size());
                refresh();
            }
        }
    }

    public class NoteArrayAdapter extends BaseAdapter {
        ArrayList<TaskDetailsBean> arrayBuddyList;
        LayoutInflater inflater = null;
        Context adapContext;


        public NoteArrayAdapter(Context context, ArrayList<TaskDetailsBean> buddyList1) {

            this.arrayBuddyList = buddyList1;
            adapContext = context;

        }

        @Override
        public int getCount() {
            return arrayBuddyList.size();
        }

        public TaskDetailsBean getItem(int position) {
            return arrayBuddyList.get(position);
        }

        @Override
        public long getItemId(int position) {

            return position;
        }

        public int getItemViewType(int position) {

            return 0;
        }


        public int getViewTypeCount() {
            return 1;
        }

        public View getView(int pos, View conView, ViewGroup group) {
            try {
                if (conView == null) {
                    inflater = (LayoutInflater) adapContext
                            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    conView = inflater.inflate(R.layout.task_history_row, null,
                            false);
                }
                final TaskDetailsBean contactBean = arrayBuddyList.get(pos);
                TextView task_giver = (TextView) conView.findViewById(R.id.task_giver);
                ImageView imageView = (ImageView) conView.findViewById(R.id.state);

                TextView msg_count = (TextView) conView.findViewById(R.id.item_counter);
                TextView task_taker = (TextView) conView.findViewById(R.id.task_taker);
                TextView task_observer = (TextView) conView.findViewById(R.id.task_observer);
                TextView taskName = (TextView) conView.findViewById(R.id.task_name);
                TextView remain = (TextView) conView.findViewById(R.id.remain_count);
                TextView percen = (TextView) conView.findViewById(R.id.percent_update);
                TextView task_status = (TextView) conView.findViewById(R.id.task_status);
                TextView catagory = (TextView) conView.findViewById(R.id.catagory);
                ImageView selected = (ImageView) conView.findViewById(R.id.selected);

                task_giver.setText("Owner : Me");
                catagory.setText("Me Id: " + contactBean.getTaskId());
                selected.setBackgroundResource(R.drawable.ic_note_32_2);
                //task_status.setText("Template");
//                imageView.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_arrow_18_16));
                taskName.setText("Me Name: " + contactBean.getTaskName());
                if (contactBean.getTaskObservers() != null && !contactBean.getTaskObservers().equalsIgnoreCase("") && !contactBean.getTaskObservers().equalsIgnoreCase(null) && !contactBean.getTaskObservers().equalsIgnoreCase("null")) {
                    task_observer.setVisibility(View.VISIBLE);
                    if (contactBean.getTaskObservers() != null && contactBean.getTaskObservers().contains("_")) {
                        task_observer.setText("Observer : " + VideoCallDataBase.getDB(context).getname(contactBean.getTaskObservers()));
                    } else {
                        task_observer.setText("Observer : " + contactBean.getTaskObservers());
                    }
                } else {
                    task_observer.setVisibility(View.GONE);
                }
                if (contactBean.getTaskReceiver() != null && !contactBean.getTaskReceiver().equalsIgnoreCase("") && !contactBean.getTaskReceiver().equalsIgnoreCase(null) && !contactBean.getTaskReceiver().equalsIgnoreCase("null") && !contactBean.getTaskReceiver().equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                    task_taker.setVisibility(View.VISIBLE);
                    String name=VideoCallDataBase.getDB(context).getName(contactBean.getTaskReceiver());
                    task_taker.setText("Taker : " + name);
                } else {
                    task_taker.setVisibility(View.GONE);
                }
                percen.setVisibility(View.VISIBLE);
                if (contactBean.getCompletedPercentage() != null && !contactBean.getCompletedPercentage().equalsIgnoreCase("") && !contactBean.getCompletedPercentage().equalsIgnoreCase("null")) {
                    percen.setText(contactBean.getCompletedPercentage() + "%");
                } else {
                    percen.setText("0%");
                }
                remain.setVisibility(View.GONE);
                msg_count.setVisibility(View.GONE);
                task_status.setVisibility(View.GONE);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return conView;

        }
    }
}
