package com.myapplication3;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.myapplication3.Bean.ProjectDetailsBean;
import com.myapplication3.Bean.TaskDetailsBean;
import com.myapplication3.DB.VideoCallDataBase;
import com.myapplication3.call_list.Call_History;
import com.myapplication3.call_list.Call_ListBean;

import org.pjsip.pjsua2.app.MainActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by Amuthan on 22/03/2016.
 */
public class ChatFragment extends Fragment {


    public SwipeMenuListView listView, listView_call;
    public Handler history_handler;
//    public Vector<ChatBean> chat_list = null;
    public ArrayList<TaskDetailsBean> chat_list = null;
    public ArrayList<Call_ListBean> call_list = null;
    public ChatAdapter adapter = null;
    public CallAdapter adapter1 = null;
    public TextView chat, call, chat_count, exclation_counter;
    LinearLayout call_view,chat_view,ll_networkUI = null;
    TextView tv_networkstate = null;
    Handler handler = new Handler();
    int contact_count = 0;
    boolean isCurrentlyActivie = false;
    Context context;
    public ArrayList<TaskDetailsBean> chatlist5;

    public static ChatFragment newInstance(int sectionNumber) {
        Log.i("chat", "Chat Fragment newInstance ");
        ChatFragment fragment = new ChatFragment();
        Bundle args = new Bundle();
//        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }




    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.i("LifeCycle", " projectFragment isVisibleToUser : " + isVisibleToUser);
        isCurrentlyActivie = isVisibleToUser;
        if(Appreference.is_chat == true){
            chat_list = VideoCallDataBase.getDB(getContext()).getChatnames();
            contact_count = 0;
            for (TaskDetailsBean chatBean : chat_list) {
                int msgCount = VideoCallDataBase.getDB(getContext()).getchatUnReadMsgCount(chatBean.getTaskId());
                Log.i("chat", "msgcount " + msgCount);
                if (msgCount > 0) {
                    contact_count = contact_count + 1;
                }
                chatlist5.add(chatBean);
                Log.i("chat", "CF count ** " + contact_count);
            }
            Log.i("chat", "CF Database size ** " + contact_count);
            Log.i("chat", "CF Database size ** " + chatlist5.size());
            if (contact_count != 0 && contact_count > 0) {
                Log.i("chat", "CF count Visible " + contact_count);
                chat_count.setVisibility(View.VISIBLE);
                chat_count.setText(String.valueOf(contact_count));
                contact_count=0;
            } else {
                Log.i("chat", "CF count GONE " + contact_count);
                chat_count.setVisibility(View.GONE);
            }

            listView.setVisibility(View.VISIBLE);
            listView_call.setVisibility(View.GONE);
            adapter = new ChatAdapter(getContext(), chat_list);
            listView.setAdapter(adapter);
            Log.i("chat", "chatFragment " + MainActivity.username);
            Log.i("chat", "chatFragment " + Appreference.loginuserdetails.getUsername());
            Log.i("chat", "chatFragment " + Appreference.is_chat);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.chat_fragment_layout, container, false);
        Appreference.context_table.put("chatfragment", this);
        listView = (SwipeMenuListView) rootView.findViewById(R.id.history_view);
        ll_networkUI = (LinearLayout) rootView.findViewById(R.id.ll_networkstate);
        chat_view = (LinearLayout) rootView.findViewById(R.id.group_view);
        call_view = (LinearLayout) rootView.findViewById(R.id.contact_view);
        tv_networkstate = (TextView) rootView.findViewById(R.id.tv_networksate);
        Log.i("chat", "ChatFragment oncreateview ");
        Log.d("CallHistory", "OncreateView called ");
        Log.i("chat", "chatFragment boolean" + Appreference.is_chat);
        chat = (TextView) rootView.findViewById(R.id.chat1);
        call = (TextView) rootView.findViewById(R.id.call1);
        chat_count = (TextView) rootView.findViewById(R.id.chat_count);
        listView_call = (SwipeMenuListView) rootView.findViewById(R.id.history_call);
        chat_list = VideoCallDataBase.getDB(getContext()).getChatnames();
        call_list = VideoCallDataBase.getDB(getContext()).getCallHistoty(Appreference.loginuserdetails.getUsername());
        adapter1 = new CallAdapter(getContext(), call_list);
        listView_call.setAdapter(adapter1);
        exclation_counter = (TextView) rootView.findViewById(R.id.exclation_counter);
        chatlist5 = new ArrayList<TaskDetailsBean>();
        Log.i("chat", "chatfragment  " + MainActivity.username);
        Log.i("chat", "chatfragment  " + Appreference.loginuserdetails.getUsername());
        Log.i("chat", "chatfragment  " + chat_list.size());
        contact_count = 0;
        for (TaskDetailsBean chatBean : chat_list) {
            int msgCount = VideoCallDataBase.getDB(getContext()).getchatUnReadMsgCount(chatBean.getTaskId());
            Log.i("chat", "msgcount " + msgCount);
            if (msgCount > 0) {
                contact_count = contact_count + 1;
            }
            chatlist5.add(chatBean);
            Log.i("chat", "CF count ** " + contact_count);
        }
        Log.i("chat", "CF Database size ** " + contact_count);
        Log.i("chat", "CF Database size ** " + chatlist5.size());
        if (contact_count != 0 && contact_count > 0) {
            Log.i("chat", "CF count Visible " + contact_count);
            chat_count.setVisibility(View.VISIBLE);
            chat_count.setText(String.valueOf(contact_count));
            contact_count=0;
        } else {
            Log.i("chat", "CF count GONE " + contact_count);
            chat_count.setVisibility(View.GONE);
        }

        try{
            String s = "select * from taskDetailsInfo where readStatus='1'";
            ArrayList<ProjectDetailsBean> projectDetailsBeen = VideoCallDataBase.getDB(getContext()).getExclationdetails(s);
            if(projectDetailsBeen.size() > 0)
                exclation_counter.setVisibility(View.VISIBLE);
            else
                exclation_counter.setVisibility(View.GONE);

        }catch (Exception e){
            e.printStackTrace();
        }


        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Appreference.is_chat = true;
                Log.i("chat", "ChatFragment chattab ");
                chat.setTextColor(getResources().getColor(R.color.white));
                call.setTextColor(getResources().getColor(R.color.black));
                chat.setBackgroundColor(getResources().getColor(R.color.appcolor));
                chat_view.setBackgroundColor(getResources().getColor(R.color.appcolor));
                call_view.setBackgroundColor(getResources().getColor(R.color.grey_dark));
                call.setBackgroundColor(getResources().getColor(R.color.grey_dark));
                listView.setVisibility(View.VISIBLE);
                listView_call.setVisibility(View.GONE);
                adapter = new ChatAdapter(getContext(), chat_list);
                listView.setAdapter(adapter);
                Log.i("chat", "chatFragment " + MainActivity.username);
                Log.i("chat", "chatFragment " + Appreference.loginuserdetails.getUsername());
            }
        });

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Appreference.is_chat = false;
                Log.i("chat", "ChatFragment calltab ");
                chat.setTextColor(getResources().getColor(R.color.black));
                call.setTextColor(getResources().getColor(R.color.white));
                call.setBackgroundColor(getResources().getColor(R.color.appcolor));
                chat_view.setBackgroundColor(getResources().getColor(R.color.grey_dark));
                chat.setBackgroundColor(getResources().getColor(R.color.grey_dark));
                call_view.setBackgroundColor(getResources().getColor(R.color.appcolor));
                listView.setVisibility(View.GONE);
                listView_call.setVisibility(View.VISIBLE);
                adapter1 = new CallAdapter(getContext(), call_list);
                listView_call.setAdapter(adapter1);

            }
        });
        Log.i("chat", "Chat Fragment oncreateView ");
        history_handler = new Handler();

        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem openItem = new SwipeMenuItem(
                        getContext());
                // set item background
                openItem.setBackground(new ColorDrawable(Color.rgb(0xFf,
                        0x00, 0x00)));
                // set item width
                openItem.setWidth(dp2px(90));
                // set item title
                openItem.setTitle("Delete");
                // set item title fontsize
                openItem.setTitleSize(18);
                // set item title font color
                openItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(openItem);

            }
        };
//        listView.setMenuCreator(creator);
//        listView.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
        listView_call.setMenuCreator(creator);
        listView_call.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                Log.i("chat", "ChatFragment listViewmenuitem for chat ");
                TaskDetailsBean chatBean = chat_list.get(position);
                String Query = "delete from taskDetailsInfo where chatid='" + chatBean.getTaskId() + "';";
                VideoCallDataBase.getDB(getContext()).getTaskHistory(Query);
                chat_list.remove(position);
                adapter.notifyDataSetChanged();
                return true;
            }
        });
        listView_call.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                Log.i("chat", "ChatFragment listViewmenuitem for call ");
                try {
                    Call_ListBean chatBean = call_list.get(position);
                    String Query = "delete from call where start_time= '" + chatBean.getStart_time() + "';";
                    VideoCallDataBase.getDB(getContext()).getTaskHistory(Query);
                    call_list.remove(position);
                    adapter1.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            }
        });

        if(Appreference.is_chat == true){
            listView.setVisibility(View.VISIBLE);
            listView_call.setVisibility(View.GONE);
            adapter = new ChatAdapter(getContext(), chat_list);
            listView.setAdapter(adapter);
            Log.i("chat", "chatFragment " + MainActivity.username);
            Log.i("chat", "chatFragment " + Appreference.loginuserdetails.getUsername());
            Log.i("chat", "chatFragment " + Appreference.is_chat);
            adapter1.notifyDataSetChanged();
        } else {
            Log.i("chat", "chatFragment " + Appreference.is_chat);
        }

//        Intent intent= new Intent(getActivity(), MainActivity.class);
//        startActivity(intent);


        Log.i("chat", "Chat Fragment chat_list size---> " + chat_list.size());
//        Collections.sort(chat_list,new chathistorysorting());

        listView_call.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                Log.i("chat", "ChatFragment setonitemclick for call ");
                Call_ListBean chatBean = call_list.get(arg2);
                Intent intent = new Intent(getContext(), Call_History.class);
                intent.putExtra("calltype", chatBean.getType());
                intent.putExtra("host", chatBean.getHost());
                intent.putExtra("start_time", chatBean.getStart_time());
                intent.putExtra("participant", chatBean.getParticipant());
                intent.putExtra("duration", chatBean.getCall_duration());
                intent.putExtra("recordedpath", chatBean.getRecording_path());
                Log.i("call", "type--->" + call_list.get(arg2).toString());
                Log.i("call", "callname--->" + chatBean.getHost());
                Log.i("call", "callparticipant--->" + chatBean.getParticipant());

                startActivity(intent);
//                }

            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                Log.i("chat", "ChatFragment setonitemclick for chat ");
                // TODO Auto-generated method stub
                ArrayList<String> chatUsers = new ArrayList<String>();
                TaskDetailsBean chatBean = chat_list.get(arg2);
//                String user[] = chatBean.getChatmembers().split(",");
//                for (String name : user) {
//                    chatUsers.add(name);
//                }

//                chatBean.setViewmode(false);

//                adapter.notifyDataSetChanged();
//                String confuri=DatabaseHelper.getDB(context).ChatConfUri(chatBean.getChatname());
//                if(confuri==null||(confuri!=null&&!confuri.equals(AppReferences.conferenceURI_Line1)&&!confuri.equals(AppReferences.conferenceURI_Line2))) {
//                Intent intent = new Intent(getContext(), ChatActivity.class);
                Intent intent = new Intent(getContext(), NewTaskConversation.class);
//                intent.putExtra("datetime", chatBean.getChatname());
//                intent.putExtra("chattype", "SecureChat");
                intent.putExtra("chatid", chatBean.getTaskId());
                intent.putExtra("task", "chathistory");
                intent.putExtra("chatHistoryBean", chatBean);
                intent.putExtra("catagory", chatBean.getCatagory());
//                intent.putExtra("users", chatUsers);
//                Log.i("chat", "CF chatname--->" + chat_list.get(arg2).toString());
//                Log.i("chat", "CF chatname--->" + chatBean.getChatname());
//                Log.i("chat", "CF chatId--->" + chatBean.getChatid());
//                Log.i("chat", "CF users--->" + chatUsers);
                startActivity(intent);
            }
        });
        Log.i("task", "CF checkedchat ");
        return rootView;

    }

    public void refresh() {
        Log.i("chat", "chatFragment refresh" + MainActivity.username);
//        if(Appreference.is_chat == true){
        chat_list.clear();
        chat_list = VideoCallDataBase.getDB(getContext()).getChatnames();

        //            listView.setVisibility(View.VISIBLE);
//            listView_call.setVisibility(View.GONE);
        try {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    adapter = new ChatAdapter(getActivity(), chat_list);
                    listView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        MainActivity mainActivity = (MainActivity) Appreference.context_table.get("mainactivity");
        mainActivity.BadgeReferece();
        Log.i("chat", "chatFragment " + MainActivity.username);
        Log.i("chat", "chatFragment " + Appreference.loginuserdetails.getUsername());
        Log.i("chat", "chatFragment " + Appreference.is_chat);
//        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("CallHistory", "OnResume called ");
        Log.i("chat", "CF OnResume called ");
        chat_list = VideoCallDataBase.getDB(context).getChatnames();
        call_list = VideoCallDataBase.getDB(context).getCallHistoty(Appreference.loginuserdetails.getUsername());
        Log.i("chat","chat list "+chat_list.size());
        showNetworkStateUI();
        contact_count = 0;
        for (TaskDetailsBean chatBean : chat_list) {
            int msgCount = VideoCallDataBase.getDB(getContext()).getchatUnReadMsgCount(chatBean.getTaskId());
            Log.i("chat", "msgcount " + msgCount);

            if (msgCount > 0) {
                contact_count = contact_count + 1;
//                chatBean.setCount(String.valueOf(msgCount));
            }
            chatlist5.add(chatBean);
            Log.i("chat", "CF count ** " + contact_count);
        }
        Log.i("chat", "CF Database size ** " + contact_count);
        Log.i("chat", "CF Database size ** " + chatlist5.size());
        if (contact_count != 0 && contact_count > 0) {
            Log.i("chat", "CF count VISIBLE " + contact_count);
            chat_count.setVisibility(View.VISIBLE);
            chat_count.setText(String.valueOf(contact_count));
            contact_count=0;
        } else {
            Log.i("chat", "CF count GONE " + contact_count);
            chat_count.setVisibility(View.GONE);
        }
        if(Appreference.is_chat == true){
            listView.setVisibility(View.VISIBLE);
            listView_call.setVisibility(View.GONE);
            adapter = new ChatAdapter(getContext(), chat_list);
            listView.setAdapter(adapter);
            Log.i("chat", "chatFragment " + MainActivity.username);
            Log.i("chat", "chatFragment " + Appreference.loginuserdetails.getUsername());
            Log.i("chat", "chatFragment " + Appreference.is_chat);
        }else {
            listView.setVisibility(View.GONE);
            listView_call.setVisibility(View.VISIBLE);
            call_list = VideoCallDataBase.getDB(getContext()).getCallHistoty(Appreference.loginuserdetails.getUsername());
            adapter1 = new CallAdapter(getContext(), call_list);
            listView_call.setAdapter(adapter1);
        }
    }

    public class ChatAdapter extends ArrayAdapter<TaskDetailsBean> {
        /**
         * ******** Declare Used Variables ********
         */
        private Context context;
        private ArrayList<TaskDetailsBean> chatList;
        private LayoutInflater inflater = null;
        // ChatBean tempValues = null;
        int i = 0;

        /**
         * ********** CustomAdapter Constructor ****************
         */
        public ChatAdapter(Context context, ArrayList<TaskDetailsBean> chatList) {

            super(context, R.layout.chathistory_row, chatList);
            /********** Take passed liveCall_Values **********/
            this.context = context;
            this.chatList = chatList;

            /*********** Layout inflator to call external xml layout () ***********/
        }

        public TaskDetailsBean getItem(int position) {
            return chatList.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        /**
         * ***
         * Depends upon data size called for each row , Create each ListView row
         * ***
         */
        public View getView(final int position, View convertView,
                            ViewGroup parent) {
            try {
                if (convertView == null) {
                    inflater = (LayoutInflater) context
                            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    convertView = inflater.inflate(R.layout.chathistory_row, null);
                }
                Log.i("chat", "CF chathist-getview");
                final TaskDetailsBean bean = chatList.get(position);
                TextView chatname = (TextView) convertView
                        .findViewById(R.id.tv_chatname);
                TextView item_counter = (TextView) convertView.findViewById(R.id.item_counter);
//                chatname.setTypeface(AppReferences.normal_type);
                if(bean.getTaskType().equalsIgnoreCase("group")){
                    chatname.setText(bean.getTaskReceiver());
                } else {
                    if (bean.getOwnerOfTask().equalsIgnoreCase(MainActivity.username))
                        chatname.setText(VideoCallDataBase.getDB(context).getname(bean.getTaskReceiver()));
                    else
                        chatname.setText(VideoCallDataBase.getDB(context).getname(bean.getOwnerOfTask()));
                }

                ImageView iv_arrow = (ImageView) convertView
                        .findViewById(R.id.iv_arrow);
                iv_arrow.setTag(position);
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
                SimpleDateFormat day = new SimpleDateFormat("yyyy-MM-DD HH:mm:ss");

                int msgCount = VideoCallDataBase.getDB(getContext()).getchatUnReadMsgCount(bean.getTaskId());
                Log.i("chat", "msgcount " + msgCount);

                if (msgCount > 0) {
                    contact_count = contact_count + 1;
//                    bean.setCount(String.valueOf(msgCount));
                    item_counter.setVisibility(View.VISIBLE);
                } else {
                    item_counter.setVisibility(View.GONE);
                }

                item_counter.setText(String.valueOf(msgCount));

//                if (bean.getOpened().equalsIgnoreCase("0")) {
//                    Log.i("chat", "bean.getOpened().equalsIgnoreCase(0)");
//                    if(bean.getChatname()!=null && bean.getChatname().contains("/")) {
//                        try {
//                            Date d = sdf.parse(bean.getChatname());
//                            chatname.setText(Html
//                                    .fromHtml("<b><font color=\"#167aa8\">"
//                                            + day.format(d) + "</font></b>"));
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }else {
//                        chatname.setText(Html
//                                .fromHtml("<b><font color=\"#1b75b2\">"
//                                        + bean.getChatname() + "</font></b>"));
//                    }
////        chatname.setTypeface(null, Typeface.BOLD);
//                } else {
//                    Log.i("chat", "bean.getOpened().equalsIgnoreCase(1)");
//                    if(bean.getChatname()!=null && bean.getChatname().contains("/")) {
//                        try {
//                            Date d = sdf.parse(bean.getChatname());
//                            chatname.setText(day.format(d));
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }else {
//                        chatname.setText(bean.getChatname());
//                    }
//
////                chatname.setTextSize(20);
////        chatname.setTypeface(null, Typeface.NORMAL);
//
//                }
//                if(AppReferences.opened.containsKey(bean.getChatid())){
//
//                    if(bean.getChatname()!=null && bean.getChatname().contains("/")) {
//                        try {
//                            Date d = sdf.parse(bean.getChatname());
//                            chatname.setText(Html
//                                    .fromHtml("<b><font color=\"#167aa8\">"
//                                            + day.format(d) + "</font></b>"));
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }else {
//                        chatname.setText(Html
//                                .fromHtml("<b><font color=\"#1b75b2\">"
//                                        + bean.getChatname() + "</font></b>"));
//                    }
//                }
//                String confuri=DatabaseHelper.getDB(context).ChatConfUri(bean.getChatname());
//                if(confuri==null||(confuri!=null&&!confuri.equals(AppReferences.conferenceURI_Line1)&&!confuri.equals(AppReferences.conferenceURI_Line2)))
//                {
//                    iv_arrow.setVisibility(View.VISIBLE);
//                }else {
//
//                    iv_arrow.setVisibility(View.INVISIBLE);
//                    chatname.setText(Html
//                            .fromHtml("<b><font color=\"#167aa8\">"
//                                    + bean.getChatname() + "</font></b>"));
//                }
                contact_count = 0;
                for (TaskDetailsBean chatBean : chat_list) {
                    int msgCount1 = VideoCallDataBase.getDB(getContext()).getchatUnReadMsgCount(chatBean.getTaskId());
                    Log.i("chat", "msgcount " + msgCount1);
                    if (msgCount1 > 0) {
                        contact_count = contact_count + 1;
                    }
                    chatlist5.add(chatBean);
                    Log.i("chat", "CF count ** " + contact_count);
                }
                Log.i("chat", "CF Database size ** " + contact_count);
                Log.i("chat", "CF Database size ** " + chatlist5.size());
                if (contact_count != 0 && contact_count > 0) {
                    Log.i("chat", "CF count Visible " + contact_count);
                    chat_count.setVisibility(View.VISIBLE);
                    chat_count.setText(String.valueOf(contact_count));
                    contact_count = 0;
                } else {
                    Log.i("chat", "CF count GONE " + contact_count);
                    chat_count.setVisibility(View.GONE);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return convertView;

        }

    }

    public class CallAdapter extends ArrayAdapter<Call_ListBean> {
        /**
         * ******** Declare Used Variables ********
         */
        private Context context;
        private ArrayList<Call_ListBean> callList;
        private LayoutInflater inflater = null;
        // ChatBean tempValues = null;
        int i = 0;

        /**
         * ********** CustomAdapter Constructor ****************
         */
        public CallAdapter(Context context, ArrayList<Call_ListBean> chatList) {

            super(context, R.layout.chathistory_row, chatList);
            /********** Take passed liveCall_Values **********/
            this.context = context;
            this.callList = chatList;

            /*********** Layout inflator to call external xml layout () ***********/
        }

        public Call_ListBean getItem(int position) {
            return callList.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        /**
         * ***
         * Depends upon data size called for each row , Create each ListView row
         * ***
         */
        public View getView(final int position, View convertView,
                            ViewGroup parent) {
            try {
                if (convertView == null) {
                    inflater = (LayoutInflater) context
                            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    convertView = inflater.inflate(R.layout.chathistory_row, null);
                }
                Log.i("call", "CF chathist-getview");
                final Call_ListBean bean = callList.get(position);
                TextView chatname = (TextView) convertView
                        .findViewById(R.id.tv_chatname);
                TextView chattime = (TextView) convertView.findViewById(R.id.timedisplay);
                TextView chattype = (TextView) convertView.findViewById(R.id.display_type);

                chattype.setText(bean.getType());
                chattime.setText(bean.getStart_time());
//                chatname.setTypeface(AppReferences.normal_type);
                Log.i("calling", "Value" + bean.getHost());
                Log.i("calling", "Value" + bean.getType());
                if (bean.getType().equals("IncomingCall")) {
                    chatname.setText(bean.getHost());
                } else if (bean.getType().equalsIgnoreCase("MissedCall")) {
                    chatname.setText(bean.getHost());
                } else {
                    chatname.setText(bean.getParticipant());
                }
                ImageView iv_arrow = (ImageView) convertView
                        .findViewById(R.id.iv_arrow);
                iv_arrow.setTag(position);
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
                SimpleDateFormat day = new SimpleDateFormat("yyyy-MM-DD HH:mm:ss");

//                if (bean.getOpened().equalsIgnoreCase("0")) {
//                    Log.i("chat", "bean.getOpened().equalsIgnoreCase(0)");
//                    if(bean.getChatname()!=null && bean.getChatname().contains("/")) {
//                        try {
//                            Date d = sdf.parse(bean.getChatname());
//                            chatname.setText(Html
//                                    .fromHtml("<b><font color=\"#167aa8\">"
//                                            + day.format(d) + "</font></b>"));
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }else {
//                        chatname.setText(Html
//                                .fromHtml("<b><font color=\"#1b75b2\">"
//                                        + bean.getChatname() + "</font></b>"));
//                    }
////        chatname.setTypeface(null, Typeface.BOLD);
//                } else {
//                    Log.i("chat", "bean.getOpened().equalsIgnoreCase(1)");
//                    if(bean.getChatname()!=null && bean.getChatname().contains("/")) {
//                        try {
//                            Date d = sdf.parse(bean.getChatname());
//                            chatname.setText(day.format(d));
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }else {
//                        chatname.setText(bean.getChatname());
//                    }
//
////                chatname.setTextSize(20);
////        chatname.setTypeface(null, Typeface.NORMAL);
//
//                }
//                if(AppReferences.opened.containsKey(bean.getChatid())){
//
//                    if(bean.getChatname()!=null && bean.getChatname().contains("/")) {
//                        try {
//                            Date d = sdf.parse(bean.getChatname());
//                            chatname.setText(Html
//                                    .fromHtml("<b><font color=\"#167aa8\">"
//                                            + day.format(d) + "</font></b>"));
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }else {
//                        chatname.setText(Html
//                                .fromHtml("<b><font color=\"#1b75b2\">"
//                                        + bean.getChatname() + "</font></b>"));
//                    }
//                }
//                String confuri=DatabaseHelper.getDB(context).ChatConfUri(bean.getChatname());
//                if(confuri==null||(confuri!=null&&!confuri.equals(AppReferences.conferenceURI_Line1)&&!confuri.equals(AppReferences.conferenceURI_Line2)))
//                {
//                    iv_arrow.setVisibility(View.VISIBLE);
//                }else {
//
//                    iv_arrow.setVisibility(View.INVISIBLE);
//                    chatname.setText(Html
//                            .fromHtml("<b><font color=\"#167aa8\">"
//                                    + bean.getChatname() + "</font></b>"));
//                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return convertView;

        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (Appreference.context_table.containsKey("chatfragment")) {
            Appreference.context_table.remove("chatfragment");
        }
    }

    public void showNetworkStateUI() {
        if (ll_networkUI != null && tv_networkstate != null) {
            if (Appreference.networkState) {
                if (Appreference.sipRegistrationState) {
//                    ll_networkUI.setVisibility(View.GONE);
                } else if (!Appreference.sipRegistrationState) {
                    ll_networkUI.setVisibility(View.VISIBLE);
                    ll_networkUI.setBackgroundColor(getResources().getColor(R.color.orange));
                    tv_networkstate.setText("Connecting...");
                }
            } else if (!Appreference.networkState) {
                ll_networkUI.setVisibility(View.VISIBLE);
                ll_networkUI.setBackgroundColor(getResources().getColor(R.color.red_color));
                tv_networkstate.setText("No Internet Connection");
            }
        }

    }

    public void showNetWorkConnectedState() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (ll_networkUI != null && tv_networkstate != null) {
                    Log.i("network", "ll_networkUI!=null");
                    if (Appreference.networkState) {
                        Log.i("network", "Appreference.networkState");
                        if (Appreference.sipRegistrationState) {
                            Log.i("network", "Appreference.sipRegistrationState");
                            ll_networkUI.setVisibility(View.VISIBLE);
                            ll_networkUI.setBackgroundColor(getResources().getColor(R.color.connected));
                            tv_networkstate.setText("Connected");
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    ll_networkUI.setVisibility(View.GONE);
                                }
                            }, 2000);
                        }
                    }
                }
            }
        });
    }
}
