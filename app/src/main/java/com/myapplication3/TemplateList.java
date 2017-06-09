package com.myapplication3;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.myapplication3.Bean.TaskDetailsBean;
import com.myapplication3.DB.VideoCallDataBase;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Ramdhas on 19-09-2016.
 */
public class TemplateList extends Activity {
    static TaskArrayAdapter taskArrayAdapter;
    ListView fileListView;
    SwipeMenuListView templateview;
    ImageView template;
    TextView back, title;
    Context context;
    ListofFileAdapter listofFileAdapter;
    ArrayList<TaskDetailsBean> arrayList = new ArrayList<TaskDetailsBean>();
    ArrayList<TaskDetailsBean> templatelist, templatelist1;
    Handler handler = new Handler();
    TaskDetailsBean taskDetailsBean;

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listoffiles);
        String query = null;
        context = this;
        Log.i("TemplateList", "true *1 ");
        templateview = (SwipeMenuListView) findViewById(R.id.templatelist);
        fileListView = (ListView) findViewById(R.id.file_list);
        back = (TextView) findViewById(R.id.back);
        title = (TextView) findViewById(R.id.title);
        template = (ImageView) findViewById(R.id.template);
        fileListView.setVisibility(View.GONE);
        templateview.setVisibility(View.VISIBLE);
        template.setVisibility(View.VISIBLE);
        Log.i("TemplateList", "true *2 ");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        template.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NewTaskConversation.class);
                intent.putExtra("task", "Newtemplate");
                intent.putExtra("type", "Individual");
                intent.putExtra("template", true);
                Log.i("TemplateList","template.setOnClickListener ");
                startActivityForResult(intent, 404);
            }
        });

        title.setText("Templates");
        query = "select * from taskHistoryInfo where taskStatus='draft' and loginuser='" + Appreference.loginuserdetails.getEmail() + "' group by taskId";
        templatelist = VideoCallDataBase.getDB(context).getTaskHistoryInfo(query);
        Log.i("TemplateList", "Query " + query);
        Log.i("TemplateList", "Query size is " + templatelist.size());
        Collections.reverse(templatelist);
        refresh();


        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                // create "view task" item
                SwipeMenuItem view_task = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                view_task.setBackground(new ColorDrawable(Color.rgb(0xff, 0x91,
                        0x11)));
                // set item width
                view_task.setWidth(dp2px(90));
                // set item title
                view_task.setTitle("View Tasks");
                // set item title fontsize
                view_task.setTitleSize(18);
                // set item title font color
                view_task.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(view_task);

                // create "open" item
                SwipeMenuItem openItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                openItem.setBackground(new ColorDrawable(Color.rgb(0x00, 0xff,
                        0x11)));
                // set item width
                openItem.setWidth(dp2px(90));
                // set item title
                openItem.setTitle("Assign");
                // set item title fontsize
                openItem.setTitleSize(18);
                // set item title font color
                openItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(openItem);
   /* }
}*/

                /*// create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xFf,
                        0x00, 0x00)));
                // set item width
                deleteItem.setWidth(dp2px(90));
                // set a icon
                deleteItem.setTitle("Delete");
                deleteItem.setTitleSize(18);
                deleteItem.setTitleColor(Color.WHITE);
//            deleteItem.setIcon(R.drawable.ic_delete_32);
                // add to menu
                menu.addMenuItem(deleteItem);*/
            }
        };
        templateview.setMenuCreator(creator);
        templateview.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
        taskArrayAdapter = new TaskArrayAdapter(context, templatelist);
        templateview.setAdapter(taskArrayAdapter);

        templateview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TaskDetailsBean taskDetailsBean = templatelist.get(position);
                Log.i("TemplateList", String.valueOf(position));
                Intent intent = new Intent(context, NewTaskConversation.class);
                intent.putExtra("task", "templatehistory");
                intent.putExtra("taskHistoryBean", taskDetailsBean);
                intent.putExtra("template", true);
                startActivityForResult(intent, 404);
            }
        });

        templateview.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                Log.i("TemplateList", "position" + position + "  size" + templatelist.size());
                taskDetailsBean = templatelist.get(position);

                if (taskDetailsBean.getOwnerOfTask().equals(Appreference.loginuserdetails.getUsername())) {
                    menu.getMenuItem(index).getTitle();
                    Log.i("TemplateList", "case" + menu.getMenuItem(index).getTitle());
                }
                switch (menu.getMenuItem(index).getTitle()) {
                    case "View Tasks":
                        Intent intent_1 = new Intent(getApplicationContext(), AssignedTemplates.class);
                        intent_1.putExtra("taskid", taskDetailsBean.getTaskId());
                        startActivity(intent_1);
                        break;
                    case "Assign":
                        Intent intent = new Intent(getApplicationContext(), AddObserver.class);
                        intent.putExtra("taskId", taskDetailsBean.getTaskId());
                        intent.putExtra("from", "templatetask");
                        intent.putExtra("taskName", taskDetailsBean.getTaskName());
                        intent.putExtra("taskNo", taskDetailsBean.getTaskNo());
//                        intent.putExtra("subType","normal");
//                        Log.i("Template ","assigning "+taskDetailsBean.getTaskDescription());
//                        intent.putExtra("taskDescription", taskDetailsBean.getTaskDescription());
//                        Log.i("Template ","assigning "+taskDetailsBean.getTaskDescription());
                        startActivity(intent);
                        break;
                    case "Delete":
                        String query = "delete from taskDetailsInfo where taskId='" + taskDetailsBean.getTaskId() + "';";
                        String query_1 = "delete from taskHistoryInfo where taskId='" + taskDetailsBean.getTaskId() + "'";
                        Log.i("TemplateList", query);
                        VideoCallDataBase.getDB(context).getTaskHistory(query);
                        VideoCallDataBase.getDB(context).getTaskHistoryInfo(query_1);
                        Log.i("TemplateList", "getTaskHistory" + query);
                        Log.i("TemplateList", "getTaskHistoryInfo" + query_1);
                        templatelist.remove(position);
                        taskArrayAdapter.notifyDataSetChanged();
                        break;
                }
                return false;
            }
        });
    }

    /*public void onRestart()
    {
        super.onRestart();
        Log.i("template","onresumed");
        String query = "select * from taskDetailsInfo where  ( ( taskstatus='draft' ))  and ( loginuser='" + Appreference.loginuserdetails.getEmail() + "') group by taskId;";
        templatelist.clear();
        templatelist = VideoCallDataBase.getDB(context).getTaskHistory(query);
        Collections.reverse(templatelist);
        taskArrayAdapter.notifyDataSetChanged();

    }*/
    /*@Override
    protected void onResume() {
        super.onResume();
        Log.i("template","onresumed");
        String query = "select * from taskHistoryInfo where taskStatus='draft' and loginuser='" + Appreference.loginuserdetails.getEmail() + "' group by taskId";
        templatelist = VideoCallDataBase.getDB(context).getTaskHistoryInfo(query);
        Log.i("template", "Query " + query);
        Log.i("template", "Query size is " + templatelist.size());
        Collections.reverse(templatelist);

        handler.post(new Runnable() {
            @Override
            public void run() {
                taskArrayAdapter.notifyDataSetChanged();
            }
        });

    }*/

    public void refresh() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                taskArrayAdapter.notifyDataSetChanged();
                Log.i("template", "refreshed");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode) {
            Log.i("template", String.valueOf(requestCode));
            if (requestCode == 404) {
                templatelist.clear();
                Log.i("template", String.valueOf(requestCode));

                String query = "select * from taskHistoryInfo where taskStatus='draft' and loginuser='" + Appreference.loginuserdetails.getEmail() + "' group by taskId";
                templatelist1 = VideoCallDataBase.getDB(context).getTaskHistoryInfo(query);
                templatelist.addAll(templatelist1);
                Collections.reverse(templatelist);
                Log.i("template", String.valueOf(requestCode) + "  size" + templatelist.size());
                refresh();
            }
        }
    }

    public class TaskArrayAdapter extends BaseAdapter {
        ArrayList<TaskDetailsBean> arrayBuddyList;
        LayoutInflater inflater = null;
        Context adapContext;


        public TaskArrayAdapter(Context context, ArrayList<TaskDetailsBean> buddyList1) {

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
                TextView category = (TextView) conView.findViewById(R.id.catagory);
                ImageView temp_image = (ImageView) conView.findViewById(R.id.selected);

                category.setText("Template Id: " + contactBean.getTaskId());
                taskName.setText("Template Name: " + contactBean.getTaskName());
                task_giver.setText("Template Owner : Me");
                temp_image.setBackgroundResource(R.drawable.template);
                //task_status.setText("Template");
//                imageView.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_arrow_18_16));
                task_observer.setVisibility(View.GONE);
                task_taker.setVisibility(View.GONE);
                remain.setVisibility(View.GONE);
                percen.setVisibility(View.GONE);
                msg_count.setVisibility(View.GONE);
                task_status.setVisibility(View.GONE);

            } catch (Exception e) {
                e.printStackTrace();
            }


            return conView;

        }
    }
}
