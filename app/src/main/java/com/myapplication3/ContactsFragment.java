package com.myapplication3;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.myapplication3.Bean.ProjectDetailsBean;
import com.myapplication3.Bean.TaskDetailsBean;
import com.myapplication3.DB.VideoCallDataBase;
import com.myapplication3.RandomNumber.Utility;
import com.myapplication3.chat.ChatActivity;
import com.myapplication3.expand.Child;
import com.myapplication3.expand.Group;
import com.squareup.picasso.Picasso;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.json.JSONArray;
import org.json.JSONObject;
import org.pjsip.pjsua2.BuddyConfig;
import org.pjsip.pjsua2.BuddyInfo;
import org.pjsip.pjsua2.CallInfo;
import org.pjsip.pjsua2.CallOpParam;
import org.pjsip.pjsua2.CallSetting;
import org.pjsip.pjsua2.PresenceStatus;
import org.pjsip.pjsua2.SendInstantMessageParam;
import org.pjsip.pjsua2.app.CallActivity;
import org.pjsip.pjsua2.app.GroupMemberAccess;
import org.pjsip.pjsua2.app.MainActivity;
import org.pjsip.pjsua2.app.MyBuddy;
import org.pjsip.pjsua2.app.MyCall;
import org.pjsip.pjsua2.pjrpid_activity;
import org.pjsip.pjsua2.pjsip_evsub_state;
import org.pjsip.pjsua2.pjsip_inv_state;
import org.pjsip.pjsua2.pjsip_status_code;
import org.pjsip.pjsua2.pjsua_buddy_status;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

import json.CommunicationBean;
import json.EnumJsonWebservicename;
import json.WebServiceInterface;
import xml.xmlcomposer;


/**
 * Created by Amuthan on 22/03/2016.
 */
public class ContactsFragment extends Fragment implements View.OnClickListener, Handler.Callback, WebServiceInterface {

    private BuddyArrayAdapter buddyArrayAdapter;
    ArrayAdapter<String> adapter;
    public ArrayList<Map<String, String>> total_buddyList;
    public ArrayList<ContactBean> buddyList;
    public ArrayList<ContactBean> buddyList1;
    public ArrayList<ContactBean> totalbuddy;
    public ArrayList<ContactBean> filterbuddy;
    public ArrayList<String> buddy1;
    public ArrayList<Map<String, String>> filter_buddyList;
    public ArrayList<String> child;
    public ArrayList<String> images;
    public ArrayList alphaindex;
    ProgressDialog progress;
    public Handler hand = new Handler();
    public ArrayList<Integer> grp;
    private ListView alpha, alpha1;
    private SwipeMenuListView buddyListView;
    int contact_count = 0, group_count = 0;
    private final Handler handler = new Handler(this);
    private int buddyListSelectedIdx = -1;
    LinearLayout indexLayout, indexLayout1, contact_view, group_view;
    String userId;
    boolean alphaSelected = true;
    boolean onlineSelected = true;
    boolean selection = true;
    TextView item;
    private String lastRegStatus = "";
    Map<String, Integer> map;
    ListView listView;
    public static Context classContext;
    static ContactsFragment fragment;
    EditText search_contact;
    private Context context;
    private ContactsFilter filter;
    TextView contact, contact_batch, group, group_batch, t1, t2;
    String list;
    String Audio_Access, Chat_Access, Video_Access, Admin_Access;
    SwipeDetector swipeDetector;
    ImageView chat, audio_call, video_call, iv_txtstatus, template, new_task, notes;
    public ArrayList list1 = new ArrayList<String>();
    ContactBean bean = null, contactBean;
    boolean isgroupicon = false;
//    public GroupMemberAccess groupMemberAccess;

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }


    ExpandableListView expandableListView;
    DisplayMetrics displayMetrics;
    private ExpandableAdapter ExpAdapter;
    private ArrayList<Group> ExpListItems;
    ImageLoader imageLoader;

    String[] char1 = new String[100];
    xmlcomposer xmlcomposer = null;
    Handler ui_handler = new Handler();
    final String[] alphabaticalList = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    TextView loginuserStatus;
    ImageView alpha_sort, online_sort,reportdetails;
    RelativeLayout ll_statusChange;
    LinearLayout ll_networkUI = null, sortlayout;
    TextView tv_networkstate = null,exclation_counter;

    ProgressDialog dialog = null;
    boolean check = false;
    boolean contactTab = true;
    boolean contactTab_1 = true;

    View group_popup = null;
//    int swipe_position=0;
//    boolean swipe_touch=false;

    public static ContactsFragment newInstance(int sectionNumber, Context context) {
        if (fragment == null) {
            fragment = new ContactsFragment();
            classContext = context;
            Bundle args = new Bundle();
//        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
        }
        return fragment;
    }

    public static ContactsFragment getInstance() {
        return fragment;
    }

    public void cancelDialog() {
        try {
            if (dialog != null && dialog.isShowing()) {
                Log.i("register", "--progress bar end-----");
                dialog.dismiss();
                dialog = null;
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
//        SingleInstance.contextTable.put("contactfragment",classContext);
        //File profile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/High Message/profilePic/" + contactBean.getProfileImage());

        Log.d("LifeCycle", "Fragment ==  onCreate");
        classContext = getContext();
        xmlcomposer = new xmlcomposer();
        Appreference.context_table.put("contactsfragment", this);
        View rootView = inflater.inflate(R.layout.contacts_fragment_layout, container, false);
        View rootView1 = inflater.inflate(R.layout.item, container, false);
        loginuserStatus = (TextView) rootView.findViewById(R.id.tv_status);
        alpha_sort = (ImageView) rootView.findViewById(R.id.alpha_sort);
        reportdetails = (ImageView) rootView.findViewById(R.id.allreport);
        reportdetails.setOnClickListener(this);
        alpha_sort.setBackgroundResource(R.drawable.z_to_a);
        online_sort = (ImageView) rootView.findViewById(R.id.online_sort);
        sortlayout = (LinearLayout) rootView.findViewById(R.id.sort);
        sortlayout.setVisibility(View.VISIBLE);
        iv_txtstatus = (ImageView) rootView.findViewById(R.id.iv_txtstatus);
        search_contact = (EditText) rootView.findViewById(R.id.searchtext);
        exclation_counter = (TextView) rootView.findViewById(R.id.exclation_counter);
        Log.i("ss", "ContactFragment OncreateView");
        if (Appreference.loginuser_status != null) {
            Log.i("ss", "ContactFragment OncreateView!=null");
            Log.i("presence", "Oncreate " + Appreference.loginuser_status);
//            iv_txtstatus.setBackground(getResources().getDrawable(R.drawable.away));
//            showBusyState();
            switch (Appreference.loginuser_status) {
                case "Online":
                    loginuserStatus.setText(Appreference.loginuser_status);
                    iv_txtstatus.setBackground(getResources().getDrawable(R.drawable.online1));
                    break;
                case "Away":
                    loginuserStatus.setText(Appreference.loginuser_status);
                    iv_txtstatus.setBackground(getResources().getDrawable(R.drawable.away));
                    break;
                case "Busy":
                    loginuserStatus.setText(Appreference.loginuser_status);
                    iv_txtstatus.setBackground(getResources().getDrawable(R.drawable.busy));
                    break;
            /*    case "Offline":
                    loginuserStatus.setText(Appreference.loginuser_status);
                    iv_txtstatus.setBackground(getResources().getDrawable(R.drawable.off_line1));
                    break;*/
                default:
                    break;

            }
        }
        userId = String.valueOf(Appreference.loginuserdetails.getId());
        ll_networkUI = (LinearLayout) rootView.findViewById(R.id.ll_networkstate);
        tv_networkstate = (TextView) rootView.findViewById(R.id.tv_networksate);
        ll_statusChange = (RelativeLayout) rootView.findViewById(R.id.head1);
        t1 = (TextView) rootView.findViewById(R.id.t1);
        t2 = (TextView) rootView.findViewById(R.id.t2);
        chat = (ImageView) rootView.findViewById(R.id.chat_button);
        indexLayout = (LinearLayout) rootView.findViewById(R.id.side_index);
        indexLayout1 = (LinearLayout) rootView.findViewById(R.id.side1_index);
        contact_view = (LinearLayout) rootView.findViewById(R.id.contact_view);
        group_view = (LinearLayout) rootView.findViewById(R.id.group_view);
        alpha = (ListView) rootView.findViewById(R.id.alpha);
        alpha1 = (ListView) rootView.findViewById(R.id.alpha1);
        item = (TextView) rootView1.findViewById(R.id.item);
        template = (ImageView) rootView.findViewById(R.id.template);
        notes = (ImageView) rootView.findViewById(R.id.notes);
        template.setOnClickListener(this);
        notes.setOnClickListener(this);
        notes.setVisibility(View.VISIBLE);
        chat.setOnClickListener(this);
        audio_call = (ImageView) rootView.findViewById(R.id.call);
        audio_call.setOnClickListener(this);
        video_call = (ImageView) rootView.findViewById(R.id.video_call);
        video_call.setOnClickListener(this);
        new_task = (ImageView) rootView.findViewById(R.id.new_task);
        new_task.setOnClickListener(this);
//        TextView alpha=(TextView)rootView.findViewById(R.id.side_list_item);
        TextView name = (TextView) rootView.findViewById(R.id.contactname);

        if (Appreference.loginuserdetails.getFirstName() != null && Appreference.loginuserdetails.getLastName() != null) {

            name.setText(Appreference.loginuserdetails.getFirstName() + " " + Appreference.loginuserdetails.getLastName());
        } else if (Appreference.loginuserdetails.getFirstName() != null) {
            buddyList = VideoCallDataBase.getDB(classContext).getContact(Appreference.loginuserdetails.getFirstName());
        } else {
            name.setText(Appreference.loginuserdetails.getEmail());

        }
        //name.setText(Appreference.loginuserdetails.getFirstName()+" "+Appreference.loginuserdetails.getLastName());

        swipeDetector = new SwipeDetector();
        getIndex(alphabaticalList);
        displayIndex();
        t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (indexLayout.getVisibility() == View.VISIBLE && alpha.getVisibility() == View.VISIBLE) {
                    indexLayout.setVisibility(View.GONE);
                    t1.setBackground(getResources().getDrawable(R.drawable.ic_arrow_bwd));
                    alpha.setVisibility(View.GONE);
                } else {
                    indexLayout.setVisibility(View.VISIBLE);
                    t1.setBackground(getResources().getDrawable(R.drawable.ic_arrow_fwd));
                    alpha.setVisibility(View.VISIBLE);
                }

            }
        });
        t2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (indexLayout1.getVisibility() == View.VISIBLE && alpha1.getVisibility() == View.VISIBLE) {
                    indexLayout1.setVisibility(View.GONE);
                    t2.setBackground(getResources().getDrawable(R.drawable.ic_arrow_bwd));
                    alpha1.setVisibility(View.GONE);
                } else {
                    indexLayout1.setVisibility(View.VISIBLE);
                    t2.setBackground(getResources().getDrawable(R.drawable.ic_arrow_fwd));
                    alpha1.setVisibility(View.VISIBLE);
                }
            }
        });
        Log.i("Appreference", "getprofileimage-------->" + Appreference.loginuserdetails.getProfileImage());
        final ImageView imageView = (ImageView) rootView.findViewById(R.id.contactimage);
        File myFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/High Message/profilePic/" + Appreference.loginuserdetails.getProfileImage());
        if (Appreference.loginuserdetails.getProfileImage() != null) {
            if (myFile.exists()) {
                imageLoader = new ImageLoader(context);
                Log.i("Appreference", "if-------->" + Appreference.loginuserdetails.getProfileImage());
                imageLoader.DisplayImage(Environment.getExternalStorageDirectory().getAbsolutePath() + "/High Message/profilePic/" + Appreference.loginuserdetails.getProfileImage(), imageView, R.drawable.default_person_circle);
            } else {
                Picasso.with(getContext()).load(getResources().getString(R.string.user_upload) + Appreference.loginuserdetails.getProfileImage()).into(imageView);
                MainActivity mainActivity = (MainActivity) Appreference.context_table.get("mainactivity");
                mainActivity.callprofile(getResources().getString(R.string.user_upload) + Appreference.loginuserdetails.getProfileImage());
            }

        }

        try{
            String s = "select * from taskDetailsInfo where readStatus='1'";
            ArrayList<ProjectDetailsBean> projectDetailsBeen = VideoCallDataBase.getDB(classContext).getExclationdetails(s);
            if(projectDetailsBeen.size() > 0)
                exclation_counter.setVisibility(View.VISIBLE);
            else
                exclation_counter.setVisibility(View.GONE);

        }catch (Exception e){
            e.printStackTrace();
        }

        final TextView add_buddy = (TextView) rootView.findViewById(R.id.add);
        contact = (TextView) rootView.findViewById(R.id.contactsearch);
        contact_batch = (TextView) rootView.findViewById(R.id.item_counter_1);
//        contact.setBackgroundColor(getResources().getColor(R.color.appcolor));
        contact_view.setBackgroundColor(getResources().getColor(R.color.appcolor));
        contact.setTextColor(getResources().getColor(R.color.white));
        // group.setTextColor(getResources().getColor(R.color.default_color));
        group = (TextView) rootView.findViewById(R.id.groupsearch);
        group_batch = (TextView) rootView.findViewById(R.id.item_counter_2);
//        group.setBackgroundColor(getResources().getColor(R.color.grey_dark));
        group.setTextColor(getResources().getColor(R.color.black));
        group_view.setBackgroundColor(getResources().getColor(R.color.grey_dark));
        add_buddy.setOnClickListener(this);
        String b_uri = "sip:" + "amuthan2".toString()
                + "@" + getResources().getString(R.string.server_ip);
        total_buddyList = new ArrayList<Map<String, String>>();
//        total_buddyList.add(putData(b_uri,""));

        for (int i = 0; i < MainActivity.account.buddyList.size(); i++) {
            total_buddyList.add(putData(MainActivity.account.buddyList.get(i).cfg.getUri(),
                    MainActivity.account.buddyList.get(i).getStatusText(), "unselected"));
        }
        filter_buddyList = (ArrayList<Map<String, String>>) total_buddyList.clone();
        buddyList = new ArrayList<ContactBean>();
        buddyList1 = new ArrayList<ContactBean>();
        totalbuddy = new ArrayList<ContactBean>();
        filterbuddy = new ArrayList<ContactBean>();
        buddy1 = new ArrayList<String>();
        expandableListView = (ExpandableListView) rootView.findViewById(R.id.expandable);
        buddyListView = (SwipeMenuListView) rootView.findViewById(R.id.listViewBuddy);

        displayMetrics = getActivity().getResources().getDisplayMetrics();
        int width = displayMetrics.widthPixels;
        expandableListView.setIndicatorBounds(width - GetPixelFromDips(50), width - GetPixelFromDips(10));
        buddyList.clear();
        buddyList1.clear();

        /*if (VideoCallDataBase.getDB(classContext).getContact(Appreference.loginuserdetails.getUsername()) != null) {
            list = "Contact";
            Log.i("Contact", "Database");
            if (Appreference.loginuserdetails.getFirstName() != null && Appreference.loginuserdetails.getLastName() != null) {
                buddyList1 = VideoCallDataBase.getDB(classContext).getContact(Appreference.loginuserdetails.getUsername());
                if (buddyList1 != null && buddyList1.size() > 0)
                    for (ContactBean contactBean : buddyList1) {
                        int msgCount = VideoCallDataBase.getDB(classContext).getContactsUnReadMsgCount(contactBean.getUsername());
                        contactBean.setMsg_count(msgCount);
                        buddyList.add(contactBean);
                        totalbuddy.add(contactBean);
                    }
            }
           *//* else if (Appreference.loginuserdetails.getFirstName()!=null)
            {
                buddyList=VideoCallDataBase.getDB(classContext).getContact(Appreference.loginuserdetails.getFirstName());
            }
            else
            {
                buddyList=VideoCallDataBase.getDB(classContext).getContact(Appreference.loginuserdetails.getEmail());
            }*//*
            //buddyList=VideoCallDataBase.getDB(classContext).getContact(Appreference.loginuserdetails.getUsername());
            Log.i("ContactValue", "Arraysize" + buddyList.size());
            int cnt = buddyList.size();

            for (int i = 0; i < cnt; i++) {
                bean = buddyList.get(i);
                String s1 = buddyList.get(i).username;
                list1.add(s1);
            }

            Log.i("List", "arraylist " + list1);
            Collections.sort(buddyList, new CustomComparator());
            if(buddyList.size()==0)
                contact.performClick();

*//*            int msgCount = VideoCallDataBase.getDB(getActivity().getApplicationContext()).getContactsUnReadMsgCount(contactBean.getUsername());
            for (ContactBean contactBean:buddyList) {
                contactBean.setMsg_count(msgCount);
            }*//*
            buddyArrayAdapter = new BuddyArrayAdapter(getActivity(), buddyList);
            buddyListView.setAdapter(buddyArrayAdapter);
            buddyArrayAdapter.notifyDataSetChanged();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Collections.sort(buddyList, new CustomComparator());
                    buddyArrayAdapter.notifyDataSetChanged();
                }
            });
        }*/

        int j = 0;
        for (int i = 65; i < 91; i++) {
            char1[j] = String.valueOf((char) i);
            j++;
        }

        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                switch (menu.getViewType()) {
                    case 0:

                        SwipeMenuItem deleteItem1 = new SwipeMenuItem(getContext().getApplicationContext());
                        // set item background
                        deleteItem1.setBackground(new ColorDrawable(Color.rgb(0xFf, 0xFf, 0xFf)));
                        // set item width
                        deleteItem1.setWidth(dp2px(65));
                        deleteItem1.setTitle("Video call");
                        // set a icon
                        deleteItem1.setIcon(R.drawable.videocall1);
                        deleteItem1.setTitleSize(5);
                        deleteItem1.setTitleColor(Color.WHITE);
                        // add to menu
                        menu.addMenuItem(deleteItem1);

                        SwipeMenuItem deleteItem2 = new SwipeMenuItem(getContext().getApplicationContext());
                        // set item background
                        deleteItem2.setBackground(new ColorDrawable(Color.rgb(0xFf, 0xFf, 0xFf)));
                        // set item width
                        deleteItem2.setWidth(dp2px(65));
                        deleteItem2.setTitle("Audio call");
                        // set a icon
                        deleteItem2.setIcon(R.drawable.ic_phone_128);
                        deleteItem2.setTitleSize(5);
                        deleteItem2.setTitleColor(Color.WHITE);
                        // add to menu
                        menu.addMenuItem(deleteItem2);

                        SwipeMenuItem deleteItem3 = new SwipeMenuItem(getContext().getApplicationContext());
                        // set item background
                        deleteItem3.setBackground(new ColorDrawable(Color.rgb(0xFf, 0xFf, 0xFf)));
                        // set item width
                        deleteItem3.setWidth(dp2px(65));
                        deleteItem3.setTitle("Chat");
                        // set a icon
                        deleteItem3.setIcon(R.drawable.chat_tab);
                        deleteItem3.setTitleSize(5);
                        deleteItem3.setTitleColor(Color.WHITE);
                        // add to menu
                        menu.addMenuItem(deleteItem3);

                        SwipeMenuItem deleteItem4 = new SwipeMenuItem(getContext().getApplicationContext());
                        // set item background
                        deleteItem4.setBackground(new ColorDrawable(Color.rgb(0xFf, 0xFf, 0xFf)));
                        // set item width
                        deleteItem4.setWidth(dp2px(65));
                        deleteItem4.setTitle("Tasks");
                        // set a icon
                        deleteItem4.setIcon(R.drawable.ic_all_items_filled_32);
                        deleteItem4.setTitleSize(5);
                        deleteItem4.setTitleColor(Color.WHITE);
                        // add to menu
                        menu.addMenuItem(deleteItem4);
                        break;

                }
            }
        };
        buddyListView.setMenuCreator(creator);
        buddyListView.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);

        alpha_sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                alphaSelectionEnable();
                if (!alphaSelected) {
                    alphaSelected = true;
                    onlineSelected = false;
                } else {
                    onlineSelected = false;
                    alphaSelected = false;
                }
//                alpha_sort.setTextColor(getResources().getColor(R.color.white));
//                online_sort.setTextColor(getResources().getColor(R.color.black));
//                sorting = "alpha";

//                String type=alpha_sort.getText().toString();

                hand.post(new Runnable() {
                    @Override
                    public void run() {
                        if (alphaSelected) {
                            Log.i("ContactsFragment", "inside 1 if condition alpha check");
                            alpha_sort.setBackgroundResource(R.drawable.a_to_z);
                            Collections.sort(buddyList, new CustomComparator());
                            buddyArrayAdapter.notifyDataSetChanged();
                        } else {
                            Log.i("ContactsFragment", "inside  1 if else condition alpha check");
                            alpha_sort.setBackgroundResource(R.drawable.z_to_a);
                            Collections.reverse(buddyList);
                            buddyArrayAdapter.notifyDataSetChanged();
                        }
                    }
                });
            }
        });

        online_sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                alpha_sort.setTextColor(Color.BLACK);
//                online_sort.setTextColor(Color.BLACK);
                if (!onlineSelected) {
                    alphaSelected = false;
                    onlineSelected = true;
                } else {
                    alphaSelected = false;
                    onlineSelected = false;
                }
//                onlineSelected="true";
                if (selection == true) {
                    selection = false;
                    ArrayList<ContactBean> onlinelist = new ArrayList<ContactBean>();
                    onlinelist.clear();
                    ArrayList<ContactBean> offlinelist = new ArrayList<ContactBean>();
                    offlinelist.clear();
                    ArrayList<ContactBean> awaylist = new ArrayList<ContactBean>();
                    awaylist.clear();
                    ArrayList<ContactBean> busylist = new ArrayList<ContactBean>();
                    busylist.clear();
                    ArrayList<ContactBean> emptylist = new ArrayList<ContactBean>();
                    emptylist.clear();
                    for (ContactBean contactBean : buddyList) {
                        if (contactBean.getStatus() != null && contactBean.getStatus().equalsIgnoreCase("Online")) {
                            onlinelist.add(contactBean);
                        }
                        if (contactBean.getStatus() != null && contactBean.getStatus().equalsIgnoreCase("Busy")) {
                            busylist.add(contactBean);
                        }
                        if (contactBean.getStatus() != null && contactBean.getStatus().equalsIgnoreCase("Offline")) {
                            offlinelist.add(contactBean);
                        }
                        if (contactBean.getStatus() != null && contactBean.getStatus().equalsIgnoreCase("Away")) {
                            awaylist.add(contactBean);
                        }
                        if (contactBean.getStatus() == null || contactBean.getStatus().equalsIgnoreCase("")) {
                            emptylist.add(contactBean);
                        }
                    }
                    buddyList.clear();
                    buddyList.addAll(onlinelist);
                    buddyList.addAll(awaylist);
                    buddyList.addAll(busylist);
                    buddyList.addAll(offlinelist);
                    buddyList.addAll(emptylist);
                    buddyArrayAdapter.notifyDataSetChanged();
//                onlineSelectionEnable();
//                buddyList.get(0).getStatus().equalsIgnoreCase("Online");
                } else {
                    selection = true;
                    Collections.sort(buddyList, new CustomComparator());
                    buddyArrayAdapter.notifyDataSetChanged();
                }
            }
        });


        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortlayout.setVisibility(View.VISIBLE);
//                alpha_sort.setText("A > Z");
                indexLayout1.setVisibility(View.GONE);
                indexLayout.setVisibility(View.GONE);
                alpha.setVisibility(View.GONE);
                reportdetails.setVisibility(View.VISIBLE);
                alpha1.setVisibility(View.GONE);
                t1.setVisibility(View.GONE);
                t2.setVisibility(View.GONE);
                indexLayout.setVisibility(View.GONE);
                alpha.setVisibility(View.GONE);
                chat.setVisibility(View.GONE);
                template.setVisibility(View.VISIBLE);
                notes.setVisibility(View.VISIBLE);
//                notes.setVisibility(View.GONE);
                contact_batch.setVisibility(View.GONE);
//                video_call.setVisibility(View.VISIBLE);
                audio_call.setVisibility(View.GONE);
                buddyList1 = new ArrayList<ContactBean>();
                buddyList = new ArrayList<ContactBean>();

                contactTab = true;
                contactTab_1 = true;
                buddyListView.setVisibility(View.VISIBLE);
                expandableListView.setVisibility(View.GONE);
                contact.setTextColor(getResources().getColor(R.color.white));
                group.setTextColor(getResources().getColor(R.color.black));
                contact.setBackgroundColor(getResources().getColor(R.color.appcolor));
                contact_view.setBackgroundColor(getResources().getColor(R.color.appcolor));
                group.setBackgroundColor(getResources().getColor(R.color.grey_dark));
                group_view.setBackgroundColor(getResources().getColor(R.color.grey_dark));
                Log.i("Contact", "MainPage " + Appreference.loginuserdetails.getUsername());
                list = "Contact";
                if (VideoCallDataBase.getDB(classContext).getContact(Appreference.loginuserdetails.getUsername()) != null) {
                    Log.i("Contact", "Database");
                    if (Appreference.loginuserdetails.getFirstName() != null && Appreference.loginuserdetails.getLastName() != null) {
                        buddyList1 = VideoCallDataBase.getDB(classContext).getContact(Appreference.loginuserdetails.getUsername());
                        for (ContactBean contactBean : buddyList1) {
                            int msgCount = VideoCallDataBase.getDB(classContext).getContactsUnReadMsgCount(String.valueOf(contactBean.getUserid()), "Individual");
                            contactBean.setMsg_count(msgCount);
                            buddyList.add(contactBean);
                            totalbuddy.add(contactBean);
                        }
                        Log.i("Contact", "Database " + buddyList.size());
                    }
                    /*else if (Appreference.loginuserdetails.getFirstName()!=null)
                    {
                        buddyList=VideoCallDataBase.getDB(classContext).getContact(Appreference.loginuserdetails.getFirstName());
                    }
                    else
                    {
                        buddyList=VideoCallDataBase.getDB(classContext).getContact(Appreference.loginuserdetails.getEmail());
                    }*/
                    //buddyList=VideoCallDataBase.getDB(classContext).getContact(Appreference.loginuserdetails.getUsername());
                    Log.i("ContactValue", "Arraysize" + buddyList.size());
                }
                if (group_count != 0 && group_count > 0) {
                    group_batch.setVisibility(View.VISIBLE);
                    group_batch.setText(String.valueOf(group_count));
                } else {
                    group_batch.setVisibility(View.GONE);
                }
                Log.i("contact", "group_count before " + group_count);
                buddyList.toString();
                Collections.sort(buddyList, new CustomComparator());
                if (alphaSelected) {
                    alphaSelectionEnable();
                } else if (onlineSelected) {
                    onlineSelectionEnable();
                }
                Log.i("list", "buddyList" + String.valueOf(buddyList));
                buddyArrayAdapter = new BuddyArrayAdapter(getActivity(), buddyList);
                buddyListView.setAdapter(buddyArrayAdapter);
                buddyArrayAdapter.notifyDataSetChanged();
             /*   String test=Appreference.demo;
                if(test.equals("add")){
                    getIndexList(char1);
                    displayIndex();
                    Appreference.demo="added";
                }*/
            }
        });
        alpha.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String s1 = alpha.getItemAtPosition(position).toString();
                int count = buddyListView.getCount();
                for (int i = 0; i < count; i++) {
                    String s = buddyList.get(i).firstname;
                    // Toast.makeText(getContext(),s,Toast.LENGTH_SHORT).show();
                    if (s1.equalsIgnoreCase(s.substring(0, 1))) {
                        buddyListView.setSelection(i);
                        break;
                    } else {
                        buddyListView.setSelection(0);
                    }
                }
            }
        });
        alpha1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String s1 = alpha1.getItemAtPosition(position).toString();
                int count = expandableListView.getCount();
                for (int i = 0; i < count; i++) {
                    String s = ExpListItems.get(i).getName();
                    // Toast.makeText(getContext(),s,Toast.LENGTH_SHORT).show();
                    if (s1.equalsIgnoreCase(s.substring(0, 1))) {
                        expandableListView.setSelection(i);
                        break;
                    } else {
                        expandableListView.setSelection(0);
                    }
                }
            }
        });

        group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortlayout.setVisibility(View.GONE);
                contactTab = false;
                contactTab_1 = false;
                indexLayout.setVisibility(View.GONE);
                alpha.setVisibility(View.GONE);
                indexLayout1.setVisibility(View.GONE);
                alpha1.setVisibility(View.GONE);
                template.setVisibility(View.GONE);
                chat.setVisibility(View.GONE);
                reportdetails.setVisibility(View.VISIBLE);
                notes.setVisibility(View.GONE);
                audio_call.setVisibility(View.GONE);
                group_batch.setVisibility(View.GONE);
//                video_call.setVisibility(View.VISIBLE);
                contact.setTextColor(getResources().getColor(R.color.black));
                group.setTextColor(getResources().getColor(R.color.white));
//                t2.setVisibility(View.VISIBLE);
                t1.setVisibility(View.GONE);
                //alpha1.setVisibility(View.VISIBLE);
                buddyListView.setVisibility(View.GONE);
                expandableListView.setVisibility(View.VISIBLE);
                group.setBackgroundColor(getResources().getColor(R.color.appcolor));
                group_view.setBackgroundColor(getResources().getColor(R.color.appcolor));
                contact.setBackgroundColor(getResources().getColor(R.color.grey_dark));
                contact_view.setBackgroundColor(getResources().getColor(R.color.grey_dark));
                if (contact_count != 0 && contact_count > 0) {
                    contact_batch.setVisibility(View.VISIBLE);
                    contact_batch.setText(String.valueOf(contact_count));
                } else {
                    contact_batch.setVisibility(View.GONE);
                }
                Log.i("contact", "contact_count before " + contact_count);

                groupListRefresh();

            }
        });

        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                Log.i("ContactFragment", "expandableListView.setOnGroupClickListener ");
                buddyListSelectedIdx = groupPosition;
                Group group = ExpListItems.get(groupPosition);
                if (!group.getIscheck()) {
                    Log.e("sipTest", "Inside True");
                    group.setIscheck(true);
                } else {
                    Log.e("sipTest", "Inside False");
                    group.setIscheck(false);
                }

                ExpAdapter.notifyDataSetChanged();
                showprogress();
//                int group1 = Integer.parseInt(ExpListItems.get(groupPosition).getId());
//                String groupId = String.valueOf(group1);
//                Log.i("expand", "groupId" + groupId);
//                Log.i("expand", "userId" + userId);
//                List<NameValuePair> nameValuePairs1 = new ArrayList<NameValuePair>(2);
//                nameValuePairs1.add(new BasicNameValuePair("userId", userId));
//                nameValuePairs1.add(new BasicNameValuePair("groupId", groupId));
//                Appreference.jsonRequestSender.listUserGroupMemberAccess(EnumJsonWebservicename.listUserGroupMemberAccess, nameValuePairs1, ContactsFragment.this);
                return false;
            }
        });

        expandableListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Group group = ExpListItems.get(position);
                Log.i("ContactFragment", "expandableListView.setOnItemClickListener ");
                /*Intent i = new Intent(context, TaskHistory.class);
                i.putExtra("userId", group.getName());
                Log.i("TaskStatus", "value 1 " + group.getName() + group.getId());
                i.putExtra("taskType", "Group");
                startActivity(i);*/
                Intent intent = new Intent(getContext(), Contactlistpage.class);
                intent.putExtra("contact", String.valueOf(group));
                intent.putExtra("userId", group.getId());
                intent.putExtra("groupname", group.getName());
                Log.i("contact", "groupid " + group.getId());
                intent.putExtra("taskType", "Group");
                startActivity(intent);
            }
        });


        expandableListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                int itemType = ExpandableListView.getPackedPositionType(id);

                if (itemType == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
                    Log.d("Group", "ItemLongClicked child true  == " + ExpandableListView.PACKED_POSITION_TYPE_CHILD);
                    return false;
                } else if (itemType == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {
                    Group group = ExpListItems.get(position);
                    final String assing_New = VideoCallDataBase.getDB(context).getGroupMemberAccess("select GroupTask from listUserGroupMemberAccess where groupid ='" + group.getId() + "'");
                    if (assing_New != null && assing_New.contains("0")) {
                        Toast.makeText(classContext, "You are not rights to GroupTaskAccess ", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.i("contactfragment", "groupMemberAccess getGroup_Task " + position);
                        Intent intent = new Intent(classContext, NewTaskConversation.class);
                        Log.d("task", "toUserId" + group.getId());
                        TaskDetailsBean taskDetailsBean = new TaskDetailsBean();
                        taskDetailsBean.setToUserId(group.getId());
                        taskDetailsBean.setToUserName(group.getName());
                        taskDetailsBean.setTaskType("Group");
                        intent.putExtra("task", "Newtask");
                        intent.putExtra("newTaskBean", taskDetailsBean);
                        startActivity(intent);
                        Log.d("Group", "ItemLongClicked group true ==  " + ExpandableListView.PACKED_POSITION_TYPE_GROUP);
                        Log.d("Group", "ItemLongClicked group true itemType ==  " + itemType);
                    }
                    return true;
                } else {
                    return true;
                }

            }
        });


//        buddyListView.setOnTouchListener(swipeDetector);
        buddyListView.setTextFilterEnabled(true);
       /* buddyListView.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {
            @Override
            public void onSwipeStart(int position) {
                Log.i("swipe", "pos start--->" + position);
                swipe_touch = false;
                swipe_position = position;
                ContactBean item1 = buddyList.get(position);
                if (item1 != null) {
                    item1.setIsprofile(true);
                }
                buddyArrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onSwipeEnd(int position) {
                Log.i("swipe", "pos end--->" + position);
                swipe_touch = true;
                if (getNegativeOrPositiveValue(position).equalsIgnoreCase("negative")) {
                    swipe_touch = false;
                    ContactBean item1 = buddyList.get(swipe_position);
                    if (item1 != null) {
                        item1.setIsprofile(false);
                    }
                    buddyArrayAdapter.notifyDataSetChanged();
                }
            }
        });*/

       /* buddyListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.i("swipe", "---Ontouch event---");
                if (swipe_touch) {
                    ContactBean item1 = buddyList.get(swipe_position);
                    if (item1 != null) {
                        item1.setIsprofile(false);
                    }
                    buddyArrayAdapter.notifyDataSetChanged();
                }
                return false;
            }
        });*/

        buddyListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                buddyListSelectedIdx = position;
                ContactBean item1 = buddyList.get(position);
                String buddy_uri_video = item1.getUsername();
//               buddyList.get(position).setIscheck(true);
                Log.i("swipe call ", "name video ** " + buddy_uri_video);
//               item1.setIsprofile(false);
//               Log.i("statusIcon", "visibility**** " + contactBean.getIsprofile());
                if (menu.getMenuItem(index).getTitle().equalsIgnoreCase("Video call")) {
                    Log.i("list ----------- > ", buddyList.get(position) + "");
                    ContactBean item = buddyList.get(position);
                    String buddy_uri = item.getUsername();
                    Log.i("swipe call ", "name video " + buddy_uri);
                    buddyList.get(position).setIscheck(true);
                    if (buddyList.size() > 0) {
                        check = false;
                        dialog = new ProgressDialog(classContext);
                        dialog.setMessage("Call Connecting...");
                        dialog.setCancelable(false);
                        dialog.show();
//                        for (ContactBean contactBean : buddyList) {
//                            ContactBean item1 = buddyList.get(position);
//                            if (contactBean.getIscheck()) {
                        MainActivity.isAudioCall = false;
//                        callNotification(item1.userid, Appreference.loginuserdetails.getId());
//                        Appreference.broadcast_call = false;
                        ArrayList<Integer> group_list_id = new ArrayList<Integer>();
                        group_list_id.add(item.userid);
                        if(group_list_id.size() > 0) {
                            callNotification(group_list_id, Appreference.loginuserdetails.getId());
                            Appreference.broadcast_call = false;
                        }
//                           }
                    }
                    buddyArrayAdapter.notifyDataSetChanged();
//                    }
                }
                if (menu.getMenuItem(index).getTitle().equalsIgnoreCase("Audio call")) {
                    Log.i("list ----------- > ", buddyList.get(position) + "");
                    ContactBean item = buddyList.get(position);
                    String buddy_uri_audio = item.getUsername();
                    buddyList.get(position).setIscheck(true);
                    Log.i("swipe call ", "name audio ** " + buddy_uri_audio);
                    Log.i("swipe call ", "name audio call---->  " + buddy_uri_audio);
                    if (MainActivity.gsmCallState == TelephonyManager.CALL_STATE_IDLE) {
                        if (buddyList.size() > 0) {
                            check = false;
                            dialog = new ProgressDialog(classContext);
                            dialog.setMessage("Call Connecting...");
                            dialog.setCancelable(false);
                            dialog.show();
//                            for (ContactBean contactBean : buddyList) {
//                                ContactBean item1 = buddyList.get(position);
//                                if (contactBean.getIscheck()) {
                            MainActivity.isAudioCall = true;
//                            callNotification(item1.userid, Appreference.loginuserdetails.getId());
//                            Log.i("swipe call ", "name audio item1.userid " + item1.userid);
//                            Appreference.broadcast_call = false;
                            ArrayList<Integer> group_list_id = new ArrayList<Integer>();
                            group_list_id.add(item.userid);
                            if(group_list_id.size() > 0) {
                                callNotification(group_list_id, Appreference.loginuserdetails.getId());
                                Appreference.broadcast_call = false;
                            }
//                                }
                        }
                        buddyArrayAdapter.notifyDataSetChanged();
//                        }
                    }
                }
                if (menu.getMenuItem(index).getTitle().equalsIgnoreCase("Chat")) {
                    ArrayList<String> chatUsers = new ArrayList<String>();
//                    for (int pos = 0; pos < buddyList.size(); pos++) {
                    ContactBean item = buddyList.get(position);
                    String buddy_uri = item.getUsername();
                    chatUsers.add(buddy_uri);
                    Log.i("chat", "chat Selected user-->" + buddy_uri);
//                    }
                    if (chatUsers != null && chatUsers.size() > 0) {
                        Log.i("chat", "chat user size-->" + chatUsers.size());

                        TreeSet<String> names = new TreeSet<>();
                        names.addAll(chatUsers);
                        names.add(Appreference.loginuserdetails.getUsername());
                        String chatuser = null;
                        for (String nm : names) {
                            if (chatuser == null) {
                                chatuser = nm;
                            } else {
                                chatuser = chatuser + "," + nm;
                            }
                        }
                        Log.i("chat", "chtuser-->" + chatuser);
                        String[] chatDetails = VideoCallDataBase.getDB(getContext()).getChatHistoryAvailabeUser(chatuser);
                        Log.i("chat", "Chatetails size--->" + chatDetails.length);
                        Intent i = new Intent(getContext(), ChatActivity.class);
                        if (chatDetails != null && chatDetails.length > 0 && chatDetails[0] != null) {
                            Log.i("chat", "db datetime-->" + chatDetails[0]);
                            Log.i("chat", "db cahtid--->" + chatDetails[1]);
                            i.putExtra("users", chatUsers);
                            i.putExtra("datetime", chatDetails[0]);
                            i.putExtra("chatid", chatDetails[1]);
                        } else {
                            SimpleDateFormat dateformat = new SimpleDateFormat(
                                    "yyyy-MM-dd hh:mm:ss");
                            String dateforrow = dateformat.format(new Date()
                                    .getTime());
                            // String cid = Utility.getSessionID();
                            i.putExtra("users", chatUsers);
                            i.putExtra("datetime", dateforrow);
                            i.putExtra("chatid", chatuser);
                            Log.i("chat", "contact fragment chatid-->" + chatuser);
                            Log.i("chat", "contact fragment  chatName-->" + dateforrow);

                        }
                        i.putExtra("chattype", "SecureChat");
                        startActivity(i);
                    }
                    buddyArrayAdapter.notifyDataSetChanged();
                }
                if (menu.getMenuItem(index).getTitle().equalsIgnoreCase("Tasks")) {
                    buddyListSelectedIdx = position;
                    Log.i("ContactsFragment ", "buddyListView.setOnItemClickListener *********" + buddyList.get(position) + "");
                    ContactBean item = buddyList.get(position);
                    String buddy_uri = item.getUsername();
                    Intent intent = new Intent(getContext(), TaskHistory.class);
                    intent.putExtra("contact", item);
                    intent.putExtra("userId", item.getUsername());
                    intent.putExtra("taskType", "Individual");
                    startActivity(intent);
                }
                return false;
            }
        });
        buddyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    buddyListSelectedIdx = position;
                    Log.i("ContactsFragment ", "buddyListView.setOnItemClickListener *********" + buddyList.get(position) + "");
                    ContactBean item = buddyList.get(position);
                    String buddy_uri = item.getUsername();

//                Intent intent = new Intent(getContext(), TaskHistory.class);
                    Intent intent = new Intent(getContext(), Contactlistpage.class);
                    intent.putExtra("contact", item);
                    intent.putExtra("userId", item.getUsername());
                    intent.putExtra("taskType", "Individual");
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
//                if (!item.getIscheck()) {
//                    Log.e("sipTest", "Inside True");
//                    item.setIscheck(true);
////                    if (swipeDetector.swipeDetected()) {
////                        if (swipeDetector.getAction() == SwipeDetector.Action.RL) {
////                            Log.d("swipe", "swipe right to left");
////
////                        } else {
////
////                            Log.d("swipe", "swipe left to right");
////                        }
////                    }
//                } else {
//                    Log.e("sipTest", "Inside False");
//                    item.setIscheck(false);
//                }

//                MainActivity.positions.add(position);
//                addRemoveAlert();
//                makeCall(null);
//                buddyArrayAdapter.notifyDataSetChanged();
            }
        });
        buddyListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                try {
                    Log.i("ContactsFragment", "buddyListView.setOnItemLongClickListener ");
                    buddyListSelectedIdx = position;
//              swipe_touch = false;
//              makeCall(null);
//              addRemoveAlert();
                    ContactBean contactBean = buddyList.get(buddyListSelectedIdx);
                    Intent intent = new Intent(classContext, NewTaskConversation.class);
                    TaskDetailsBean taskDetailsBean = new TaskDetailsBean();
                    taskDetailsBean.setToUserId(String.valueOf(contactBean.getUserid()));
                    taskDetailsBean.setToUserName(contactBean.getUsername());
                    taskDetailsBean.setTaskType("Individual");
                    intent.putExtra("task", "Newtask");
                    intent.putExtra("newTaskBean", taskDetailsBean);
                    startActivity(intent);
//              Intent intent = new Intent(classContext, NewTaskConversation.class);
//              TaskDetailsBean taskDetailsBean = new TaskDetailsBean();
//              taskDetailsBean.setToUserId(String.valueOf(contactBean.getUserid()));
//              taskDetailsBean.setToUserName(contactBean.getUsername());
//              taskDetailsBean.setTaskType("Individual");
//              intent.putExtra("task", "Newtask");
//              intent.putExtra("newTaskBean", taskDetailsBean);
//                startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            }
        });


     /*   search_contact.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String[] value = new String[count];
//                buddyArrayAdapter.getFilter().filter(s);
                ContactsFragment.this.buddyArrayAdapter.getFilter().filter(s);
            }


            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });*/

        ll_statusChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context wrapper = new ContextThemeWrapper(getContext(), R.style.myPopupMenuStyle);
                PopupMenu popup = new PopupMenu(wrapper, ll_statusChange);
                //Inflating the Popup using xml file
//               popup.getMenu().getItem(1).setChecked(true);
                popup.getMenuInflater().inflate(R.menu.loginuserstatus_menu, popup.getMenu());
                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getTitle().equals("Online")) {
                            ChangeLoginUserStatus("Online", "online");
                            loginuserStatus.setText("Online");
                            iv_txtstatus.setBackground(getResources().getDrawable(R.drawable.online1));
                            Appreference.loginuser_status = loginuserStatus.getText().toString().trim();
                            Log.i("presence", "Online " + Appreference.loginuser_status);
                        } else if (item.getTitle().equals("Away")) {
                            ChangeLoginUserStatus("Away", "online");
                            loginuserStatus.setText("Away");
                            iv_txtstatus.setBackground(getResources().getDrawable(R.drawable.away));
                            Appreference.loginuser_status = loginuserStatus.getText().toString().trim();
                            Log.i("presence", "Away " + Appreference.loginuser_status);
                        } else if (item.getTitle().equals("Busy")) {
                            ChangeLoginUserStatus("Busy", "online");
                            loginuserStatus.setText("Busy");
                            iv_txtstatus.setBackground(getResources().getDrawable(R.drawable.busy));
                            Appreference.loginuser_status = loginuserStatus.getText().toString().trim();
                        }/* else if (item.getTitle().equals("Offline")) {
                            ChangeLoginUserStatus("Offline", "Offline");
                            loginuserStatus.setText("Offline");
                            iv_txtstatus.setBackground(getResources().getDrawable(R.drawable.off_line1));
                            Appreference.loginuser_status = loginuserStatus.getText().toString().trim();
                        }*/
                        return true;
                    }
                });
                popup.show();
            }


        });
        Log.i("task", "checkedcontact");
        return rootView;
    }


    public void alphaSelectionEnable() {
//        if(alphaSelected.equalsIgnoreCase("false")) {
//            alphaSelected = "true";
//            onlineSelected="false";
//        }else{
//            onlineSelected="false";
//            alphaSelected="false";
//        }
//        alpha_sort.setTextColor(getResources().getColor(R.color.white));
//        online_sort.setTextColor(getResources().getColor(R.color.black));
//                sorting = "alpha";
//        int type=alpha_sort.getId();
        hand.post(new Runnable() {
            @Override
            public void run() {
                if (alpha_sort.getDrawable() != null && alpha_sort.getDrawable().equals(R.drawable.z_to_a)) {
//                    alpha_sort.setImageResource(0);
                    Log.i("ContactsFragment", "inside if condition alpha check");
                    alpha_sort.setBackgroundResource(R.drawable.a_to_z);
                    Collections.sort(buddyList, new CustomComparator());
                    buddyArrayAdapter.notifyDataSetChanged();
//            alpha_sort.setTextColor(Color.BLACK);
                } else {
                    alpha_sort.setBackgroundResource(R.drawable.z_to_a);
                    Log.i("ContactsFragment", "inside else condition alpha check");
                    Collections.reverse(buddyList);
                    buddyArrayAdapter.notifyDataSetChanged();
//            alpha_sort.setTextColor(Color.BLACK);
                }
            }
        });
    }

    public void onlineSelectionEnable() {
//        if (onlineSelected.equalsIgnoreCase("false")){
//            alphaSelected="false";
//            onlineSelected="true";
//        }else{
//            alphaSelected="false";
//            onlineSelected="false";
//        }
//                onlineSelected="true";
        ArrayList<ContactBean> onlinelist = new ArrayList<ContactBean>();
        onlinelist.clear();
        ArrayList<ContactBean> offlinelist = new ArrayList<ContactBean>();
        offlinelist.clear();
        ArrayList<ContactBean> awaylist = new ArrayList<ContactBean>();
        awaylist.clear();
        ArrayList<ContactBean> busylist = new ArrayList<ContactBean>();
        busylist.clear();
        ArrayList<ContactBean> emptylist = new ArrayList<ContactBean>();
        emptylist.clear();
        for (ContactBean contactBean : buddyList) {
            if (contactBean.getStatus() != null && contactBean.getStatus().equalsIgnoreCase("Online")) {
                onlinelist.add(contactBean);
            }
            if (contactBean.getStatus() != null && contactBean.getStatus().equalsIgnoreCase("Busy")) {
                busylist.add(contactBean);
            }
            if (contactBean.getStatus() != null && contactBean.getStatus().equalsIgnoreCase("Offline")) {
                offlinelist.add(contactBean);
            }
            if (contactBean.getStatus() != null && contactBean.getStatus().equalsIgnoreCase("Away")) {
                awaylist.add(contactBean);
            }
            if (contactBean.getStatus() == null || contactBean.getStatus().equalsIgnoreCase("")) {
                emptylist.add(contactBean);
            }
        }
        buddyList.clear();
        buddyList.addAll(onlinelist);
        buddyList.addAll(awaylist);
        buddyList.addAll(busylist);
        buddyList.addAll(offlinelist);
        buddyList.addAll(emptylist);
        buddyArrayAdapter.notifyDataSetChanged();
    }


    private void displayIndex() {
        adapter = new ArrayAdapter<String>(getContext(), R.layout.item, alphaindex);
        alpha.setAdapter(adapter);
        alpha1.setAdapter(adapter);
    }

    private void getIndex(String[] alphabaticalList) {
        alphaindex = new ArrayList<String>();
        for (int i = 0; i < alphabaticalList.length; i++) {
            alphaindex.add(alphabaticalList[i]);
        }
    }

    private int GetPixelFromDips(float pixels) {
        // Get the screen's density scale
        final float scale = getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (pixels * scale + 0.5f);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 202) {
            Log.d("receive", "done");
/*          Fragment fragment = new TasksFragment();
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();*/
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        contact_count = 0;
        group_count = 0;
        contactTab_1 = true;
        Log.d("LifeCycle", "Fragment ==  onResume");
        showNetworkStateUI();
        if (Appreference.context_table.containsKey("mainactivity")) {
            Log.d("Task1", "Inside if in contactFragment onResume");
            MainActivity mainActivity = (MainActivity) Appreference.context_table.get("mainactivity");
            mainActivity.BadgeReferece();
        }

        try{
            String s = "select * from taskDetailsInfo where readStatus='1'";
            ArrayList<ProjectDetailsBean> projectDetailsBeen = VideoCallDataBase.getDB(classContext).getExclationdetails(s);
            if(projectDetailsBeen.size() > 0)
                exclation_counter.setVisibility(View.VISIBLE);
            else
                exclation_counter.setVisibility(View.GONE);

        }catch (Exception e){
            e.printStackTrace();
        }

        if (Appreference.loginuser_status != null) {
            Log.i("ss", "ContactFragment OncreateView!=null");
            Log.i("presence", "OnResume " + Appreference.loginuser_status);
            loginuserStatus.setText(Appreference.loginuser_status);
            switch (Appreference.loginuser_status) {
                case "Online":
                    loginuserStatus.setText(Appreference.loginuser_status);
                    iv_txtstatus.setBackground(getResources().getDrawable(R.drawable.online1));
                    break;
                case "Away":
                    loginuserStatus.setText(Appreference.loginuser_status);
                    iv_txtstatus.setBackground(getResources().getDrawable(R.drawable.away));
                    break;
                case "Busy":
                    loginuserStatus.setText(Appreference.loginuser_status);
                    iv_txtstatus.setBackground(getResources().getDrawable(R.drawable.busy));
                    Log.i("presence", "OnResume busy " + Appreference.loginuser_status);
                    break;
                case "Offline":
                    loginuserStatus.setText(Appreference.loginuser_status);
                    iv_txtstatus.setBackground(getResources().getDrawable(R.drawable.off_line1));
                    break;
                default:
                    break;

            }
        }

        if (VideoCallDataBase.getDB(classContext).getContact(Appreference.loginuserdetails.getUsername()) != null) {
            list = "Contact";
            Log.i("Contact", "Database");
            if (Appreference.loginuserdetails.getFirstName() != null && Appreference.loginuserdetails.getLastName() != null) {
                buddyList1 = VideoCallDataBase.getDB(classContext).getContact(Appreference.loginuserdetails.getUsername());
                if (buddyList1 != null && buddyList1.size() > 0)
                    for (ContactBean contactBean : buddyList1) {
                        int msgCount = VideoCallDataBase.getDB(classContext).getContactsUnReadMsgCount(String.valueOf(contactBean.getUserid()), "Individual");
                        contactBean.setMsg_count(msgCount);
                        buddyList.add(contactBean);
                        totalbuddy.add(contactBean);
                    }
            }
           /* else if (Appreference.loginuserdetails.getFirstName()!=null)
            {
                buddyList=VideoCallDataBase.getDB(classContext).getContact(Appreference.loginuserdetails.getFirstName());
            }
            else
            {
                buddyList=VideoCallDataBase.getDB(classContext).getContact(Appreference.loginuserdetails.getEmail());
            }*/
            //buddyList=VideoCallDataBase.getDB(classContext).getContact(Appreference.loginuserdetails.getUsername());
            Log.i("ContactValue", "Arraysize" + buddyList.size());
            int cnt = buddyList.size();

            for (int i = 0; i < cnt; i++) {
                bean = buddyList.get(i);
                String s1 = buddyList.get(i).username;
                list1.add(s1);
            }

            Log.i("List", "arraylist " + list1);
            Collections.sort(buddyList, new CustomComparator());
            if (buddyList.size() == 0)
                contact.performClick();

/*            int msgCount = VideoCallDataBase.getDB(getActivity().getApplicationContext()).getContactsUnReadMsgCount(contactBean.getUsername());
            for (ContactBean contactBean:buddyList) {
                contactBean.setMsg_count(msgCount);
            }*/
            buddyArrayAdapter = new BuddyArrayAdapter(getActivity(), buddyList);
            buddyListView.setAdapter(buddyArrayAdapter);
            buddyArrayAdapter.notifyDataSetChanged();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Collections.sort(buddyList, new CustomComparator());
                    buddyArrayAdapter.notifyDataSetChanged();
                }
            });
        }

       /* if (Appreference.sipRegister_flag) {
            if (VideoCallDataBase.getDB(classContext).getContact(Appreference.loginuserdetails.getUsername()) != null) {
                list = "Contact";
                Log.i("Contact", "Database");
                buddyList = VideoCallDataBase.getDB(classContext).getContact(Appreference.loginuserdetails.getUsername());
                Log.i("ContactValue", "Arraysize" + buddyList.size());

                for (ContactBean contactBean : buddyList) {

                    final BuddyConfig cfg = new BuddyConfig();

                    String b_uri = "sip:" + contactBean.getUsername()
                            + "@" + getResources().getString(R.string.server_ip);
                    cfg.setUri(b_uri);
                }
            }
        }*/
        refresh();
        Log.i("contact", "group_count before " + group_count);
        groupListRefresh();
//        group_count = 0;
        Log.i("contact", "group_count before " + group_count);

    }

    private HashMap<String, String> putData(String uri, String status, String isSelected) {
        HashMap<String, String> item = new HashMap<String, String>();
        item.put("uri", uri);
        item.put("status", status);
        item.put("selected", isSelected);
        return item;
    }

    public void addRemoveAlert() {

        final Dialog dialog = new Dialog(classContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.buddy_add_remove_alert);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        TextView del = (TextView) dialog.findViewById(R.id.del);
        TextView edit = (TextView) dialog.findViewById(R.id.edit);
        TextView task = (TextView) dialog.findViewById(R.id.new_task);

        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
//                delBuddy();
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                BuddyConfig old_cfg = MainActivity.account.buddyList.get(buddyListSelectedIdx).cfg;
                dlgAddEditBuddy(old_cfg);
            }
        });

        task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ContactBean contactBean = buddyList.get(buddyListSelectedIdx);

                Intent intent = new Intent(classContext, NewTaskActivity.class);
                Log.d("task", "toUserId" + contactBean.getUserid());
                intent.putExtra("toUserId", contactBean.getUserid());
                startActivity(intent);

            }
        });
        dialog.show();
    }
//    public void makeCallAlert() {
//
//        final Dialog dialog = new Dialog(classContext);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setContentView(R.layout.buddy_add_remove_alert);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//        TextView del = (TextView) dialog.findViewById(R.id.del);
//        del.setText("Audio Call");
//        TextView edit = (TextView) dialog.findViewById(R.id.edit);
//        edit.setText("Video Call");
//        TextView chat=(TextView)dialog.findViewById(R.id.chat);
//        chat.setVisibility(View.VISIBLE);
//        TextView task = (TextView) dialog.findViewById(R.id.new_task);
//        task.setVisibility(View.GONE);
//        chat.setText("Chat");
//
//        del.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//                makeCall(null, true);
//            }
//        });
//
//        edit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//                makeCall(null, false);
//            }
//        });
//        chat.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ArrayList<String> chatUsers=new ArrayList<String>();
//                for (int position = 0; position < buddyList.size(); position++) {
//                    ContactBean item = buddyList.get(position);
//                    if (item.getIscheck()) {
//
//                        String buddy_uri = item.getUsername();
//                        chatUsers.add(buddy_uri);
//                        Log.i("chat", "chat Selected user-->" + buddy_uri);
////                    }
//                    }
//                }
//                if(chatUsers!=null && chatUsers.size()>0){
//                    Log.i("chat","chat user size-->"+chatUsers.size());
//                    SimpleDateFormat dateformat = new SimpleDateFormat(
//                            "yyyy-MM-dd hh:mm:ss");
//                    String dateforrow = dateformat.format(new Date()
//                            .getTime());
//                    String cid = Utility.getSessionID();
//                    Intent i = new Intent(getContext(), ChatActivity.class);
//                    i.putExtra("users", chatUsers);
//                    i.putExtra("datetime", dateforrow);
//                    i.putExtra("chatid", cid);
//                    i.putExtra("chattype", "SecureChat");
//                    startActivity(i);
//
//                }
//                buddyArrayAdapter.notifyDataSetChanged();
//                dialog.dismiss();
//            }
//        });
//        dialog.show();
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add:
                dlgAddEditBuddy(null);
                break;

            case R.id.template:
                Intent intent = new Intent(classContext, TemplateList.class);
                intent.putExtra("task", "template");
                startActivity(intent);
                break;
            case R.id.notes:
                Intent intent_notes = new Intent(classContext, NotesList.class);
//                intent_notes.putExtra("task", "notes");
                startActivity(intent_notes);
                break;
            case R.id.allreport:
                ArrayList<String> job_no=VideoCallDataBase.getDB(context).getJobNo();
                ArrayList<TaskDetailsBean> report=new ArrayList<>();
                if(job_no!=null && job_no.size()>0) {
                    Log.i("getreport","Treeset size-->"+job_no.size());
                    for(String id:job_no) {
                        if (id != null) {
                            TaskDetailsBean bean = new TaskDetailsBean();
                            ArrayList<TaskDetailsBean> Report_List = VideoCallDataBase.getDB(context).getAllReport(id,null,false);
                            if (Report_List != null && Report_List.size() > 0) {
                                Log.i("getreport", "Report_List size-->" + Report_List.size());
                                for (TaskDetailsBean taskDetailsBean : Report_List) {
                                    if (taskDetailsBean.getTaskNo() != null && !taskDetailsBean.getTaskNo().equalsIgnoreCase("") && !taskDetailsBean.getTaskNo().equalsIgnoreCase("null")
                                            && taskDetailsBean.getTaskNo().length() > 0) {
                                        bean.setTaskNo(taskDetailsBean.getTaskNo());
                                    }
                                    if (taskDetailsBean.getTaskName() != null && !taskDetailsBean.getTaskName().equalsIgnoreCase("") && !taskDetailsBean.getTaskName().equalsIgnoreCase("null")
                                            && taskDetailsBean.getTaskName().length() > 0) {
                                        bean.setTaskName(taskDetailsBean.getTaskName());
                                    }
                                    if (taskDetailsBean.getTaskId() != null && !taskDetailsBean.getTaskId().equalsIgnoreCase("") && !taskDetailsBean.getTaskId().equalsIgnoreCase("null")
                                            && taskDetailsBean.getTaskId().length() > 0) {
                                        bean.setTaskId(taskDetailsBean.getTaskId());
                                    }
                                    if (taskDetailsBean.getTaskDescription() != null && !taskDetailsBean.getTaskDescription().equalsIgnoreCase("") && !taskDetailsBean.getTaskDescription().equalsIgnoreCase("null")
                                            && taskDetailsBean.getTaskDescription().length() > 0) {
                                        bean.setTaskDescription(taskDetailsBean.getTaskDescription());
                                    }
                                    if (taskDetailsBean.getToUserName() != null && !taskDetailsBean.getToUserName().equalsIgnoreCase("") && !taskDetailsBean.getToUserName().equalsIgnoreCase("null")
                                            && taskDetailsBean.getToUserName().length() > 0) {
                                        bean.setToUserName(taskDetailsBean.getToUserName());
                                    }
                                    if (taskDetailsBean.getDateTime() != null && !taskDetailsBean.getDateTime().equalsIgnoreCase("") && !taskDetailsBean.getDateTime().equalsIgnoreCase("null")
                                            && taskDetailsBean.getDateTime().length() > 0) {
                                        String[] DateTask = taskDetailsBean.getDateTime().split(" ");
                                        Log.i("string12", "Date and time-->" + DateTask[0]);
                                        bean.setDateTime(DateTask[0]);
                                    }
                                    if (taskDetailsBean.getEstimatedTravel() != null && !taskDetailsBean.getEstimatedTravel().equalsIgnoreCase("") && !taskDetailsBean.getEstimatedTravel().equalsIgnoreCase("null")
                                            && taskDetailsBean.getEstimatedTravel().length() > 0) {
                                        Log.i("string12", "travel-->" + taskDetailsBean.getEstimatedTravel());
                                        bean.setEstimatedTravel(taskDetailsBean.getEstimatedTravel());
                                    }
                                    if (taskDetailsBean.getEstimatedActivity() != null && !taskDetailsBean.getEstimatedActivity().equalsIgnoreCase("") && !taskDetailsBean.getEstimatedActivity().equalsIgnoreCase("null")
                                            && taskDetailsBean.getEstimatedActivity().length() > 0) {
                                        Log.i("string12", "Activity-->" + taskDetailsBean.getEstimatedActivity());
                                        bean.setEstimatedActivity(taskDetailsBean.getEstimatedActivity());
                                    }
                                    if (taskDetailsBean.getTotalTravel() != null && !taskDetailsBean.getTotalTravel().equalsIgnoreCase("") && !taskDetailsBean.getTotalTravel().equalsIgnoreCase("null")
                                            && taskDetailsBean.getTotalTravel().length() > 0) {
                                        Log.i("string12", "total travel-->" + taskDetailsBean.getTotalTravel());
                                        bean.setTotalTravel(taskDetailsBean.getTotalTravel());
                                    }
                                    if (taskDetailsBean.getTotalActivity() != null && !taskDetailsBean.getTotalActivity().equalsIgnoreCase("") && !taskDetailsBean.getTotalActivity().equalsIgnoreCase("null")
                                            && taskDetailsBean.getTotalActivity().length() > 0) {
                                        Log.i("string12", "total activity-->" + taskDetailsBean.getTotalActivity());
                                        bean.setTotalActivity(taskDetailsBean.getTotalActivity());
                                    }
                                    if (taskDetailsBean.getStartDate() != null && !taskDetailsBean.getStartDate().equalsIgnoreCase("") && !taskDetailsBean.getStartDate().equalsIgnoreCase("null")
                                            && taskDetailsBean.getStartDate().length() > 0) {
                                        String[] startTime = taskDetailsBean.getStartDate().split(" ");
                                        Log.i("string12", "start date-->" + startTime[1]);
                                        bean.setStartDate(startTime[1]);
                                    }
                                    if (taskDetailsBean.getEndDate() != null && !taskDetailsBean.getEndDate().equalsIgnoreCase("") && !taskDetailsBean.getEndDate().equalsIgnoreCase("null")
                                            && taskDetailsBean.getEndDate().length() > 0) {
                                        String[] endTime = taskDetailsBean.getEndDate().split(" ");
                                        Log.i("string12", "end date-->" + endTime[1]);
                                        bean.setEndDate(endTime[1]);
                                    }
                                }
                                report.add(bean);
                            }
                        }
                    }
                }
                if (report != null && report.size() > 0) {
//                            Toast.makeText(getContext(), "PDF created successfully", Toast.LENGTH_LONG).show();
                    CreateExcelSheetForAllReport(report);
//                            createpdfforall(report);
                    Log.i("report", "----ReportView call---");
                    Intent intent1 = new Intent(getContext(), ReportView.class);
                    intent1.putExtra("fromContacts", true);
                    startActivity(intent1);

                } else {
                    Toast.makeText(getContext(), "You Didn't Assign Any Task", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.new_task:
                Intent tent = new Intent(classContext, AllTaskList.class);
                tent.putExtra("AllTaskList", "true");
                startActivity(tent);
                break;
            case R.id.chat_button:
                ArrayList<String> chatUsers = new ArrayList<String>();
                for (int position = 0; position < buddyList.size(); position++) {
                    ContactBean item = buddyList.get(position);
                    if (item.getIscheck()) {
                        String buddy_uri = item.getUsername();
                        chatUsers.add(buddy_uri);
                        Log.i("chat", "chat Selected user-->" + buddy_uri);
//                    }
                    }
                }
                if (chatUsers != null && chatUsers.size() > 0) {
                    Log.i("chat", "chat user size-->" + chatUsers.size());

                    TreeSet<String> names = new TreeSet<>();
                    names.addAll(chatUsers);
                    names.add(Appreference.loginuserdetails.getUsername());
                    String chatuser = null;
                    for (String nm : names) {
                        if (chatuser == null) {
                            chatuser = nm;
                        } else {
                            chatuser = chatuser + "," + nm;
                        }
                    }
                    Log.i("chat", "chtuser-->" + chatuser);
                    String[] chatDetails = VideoCallDataBase.getDB(getContext()).getChatHistoryAvailabeUser(chatuser);
                    Intent i = new Intent(getContext(), ChatActivity.class);
                    if (chatDetails != null && chatDetails.length > 0) {
                        Log.i("chat", "db datetime-->" + chatDetails[0]);
                        Log.i("chat", "db cahtid--->" + chatDetails[1]);
                        i.putExtra("users", chatUsers);
                        i.putExtra("datetime", chatDetails[0]);
                        i.putExtra("chatid", chatDetails[1]);
                    } else {
                        SimpleDateFormat dateformat = new SimpleDateFormat(
                                "yyyy-MM-dd hh:mm:ss");
                        String dateforrow = dateformat.format(new Date()
                                .getTime());
                        String cid = Utility.getSessionID();
                        i.putExtra("users", chatUsers);
                        i.putExtra("datetime", dateforrow);
                        i.putExtra("chatid", cid);

                    }
                    i.putExtra("chattype", "SecureChat");
                    startActivity(i);

                }
                buddyArrayAdapter.notifyDataSetChanged();
                break;

            case R.id.call:

                if (MainActivity.gsmCallState == TelephonyManager.CALL_STATE_IDLE) {
                    boolean select = false;
                    boolean groupIsSelect = false;
                    Log.i("audiocall", "call button click");
                    if (contactTab) {
                        for (ContactBean contactBean : buddyList) {
                            if (contactBean.getIscheck()) {
                                select = true;
                            }
                        }
                    } else {
                        for (Group group : ExpListItems) {
                            if (group.getIscheck()) {
                                groupIsSelect = true;
                            }
                        }
                    }
/*                    if (select) {
                        final Dialog dialog1 = new Dialog(classContext);
                        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog1.setContentView(R.layout.dialogforcall);
                        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                        lp.copyFrom(dialog1.getWindow().getAttributes());
                        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                        lp.horizontalMargin = 15;
                        Window window = dialog1.getWindow();
                        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        window.setAttributes(lp);
                        window.setGravity(Gravity.CENTER);
                        LinearLayout audio_call_ll = (LinearLayout) dialog1.findViewById(R.id.audio_call_ll);
                        final LinearLayout audio_broadcast_call_ll = (LinearLayout) dialog1.findViewById(R.id.audio_broadcast_call_ll);
                        LinearLayout video_call_ll = (LinearLayout) dialog1.findViewById(R.id.video_call_ll);
                        LinearLayout cancel_ll = (LinearLayout) dialog1.findViewById(R.id.cancel_ll);

                        TextView audiocall = (TextView) dialog1.findViewById(R.id.audio_call);
                        TextView audioBroadcastcall = (TextView) dialog1.findViewById(R.id.audio_broadcast_call);
                        TextView videocall = (TextView) dialog1.findViewById(R.id.video);
                        TextView cancel = (TextView) dialog1.findViewById(R.id.cancel);
                        audioBroadcastcall.setVisibility(View.GONE);
                        cancel_ll.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog1.dismiss();
                            }
                        });
                        audio_call_ll.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog1.dismiss();
                                if (buddyList.size() > 0) {
                                    check = false;
                                    dialog = new ProgressDialog(classContext);
                                    dialog.setMessage("Call Connecting...");
                                    dialog.setCancelable(false);
                                    dialog.show();
                                    for (ContactBean contactBean : buddyList) {
                                        if (contactBean.getIscheck()) {
                                            MainActivity.isAudioCall = true;
                                            callNotification(contactBean.userid, Appreference.loginuserdetails.getId());
                                            Appreference.broadcast_call = false;

                                        }
                                    }

                                }
                            }
                        });

                        audio_broadcast_call_ll.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog1.dismiss();
                                if (buddyList.size() > 0) {
                                    check = false;
                                    dialog = new ProgressDialog(classContext);
                                    dialog.setMessage("Call Connecting...");
                                    dialog.setCancelable(false);
                                    dialog.show();
                                    for (ContactBean contactBean : buddyList) {
                                        if (contactBean.getIscheck()) {
                                            MainActivity.isAudioCall = true;
                                            callNotification(contactBean.userid, Appreference.loginuserdetails.getId());
                                            Appreference.broadcast_call = true;

                                        }
                                    }

                                }
                            }
                        });
                        video_call_ll.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
//                            video_call.performClick();
                                dialog1.dismiss();
                                if (buddyList.size() > 0) {
                                    check = false;
                                    dialog = new ProgressDialog(classContext);
                                    dialog.setMessage("Call Connecting...");
                                    dialog.setCancelable(false);
                                    dialog.show();
                                    for (ContactBean contactBean : buddyList) {
                                        if (contactBean.getIscheck()) {
                                            MainActivity.isAudioCall = false;
                                            callNotification(contactBean.userid, Appreference.loginuserdetails.getId());
                                            Appreference.broadcast_call = false;
                                        }
                                    }
                                }
//                        Appreference.broadcast_call = !contactTab;
                            }
                        });

                        try {
                            //show dialog
//                        if(!((Activity) classContext).isFinishing())
//                        {
                            //show dialog
                            dialog1.show();
//                        }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    } else*/
                    if (groupIsSelect) {
                        final Dialog dialog1 = new Dialog(classContext);
                        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog1.setContentView(R.layout.dialogforcall);
                        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                        lp.copyFrom(dialog1.getWindow().getAttributes());
                        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                        lp.horizontalMargin = 15;
                        Window window = dialog1.getWindow();
                        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        window.setAttributes(lp);
                        window.setGravity(Gravity.CENTER);
                        LinearLayout audio_call_ll = (LinearLayout) dialog1.findViewById(R.id.audio_call_ll);
                        LinearLayout audio_broadcast_call_ll = (LinearLayout) dialog1.findViewById(R.id.audio_broadcast_call_ll);
                        LinearLayout video_call_ll = (LinearLayout) dialog1.findViewById(R.id.video_call_ll);
                        LinearLayout cancel_ll = (LinearLayout) dialog1.findViewById(R.id.cancel_ll);

                        TextView audiocall = (TextView) dialog1.findViewById(R.id.audio_call);
                        TextView videocall = (TextView) dialog1.findViewById(R.id.video);
                        TextView audioBroadcastcall = (TextView) dialog1.findViewById(R.id.audio_broadcast_call);
                        audiocall.setText("Conference Call");
                        videocall.setText("BroadCast Video Call");
                        audioBroadcastcall.setText("BroadCast Audio Call");
                        audio_broadcast_call_ll.setVisibility(View.VISIBLE);
                        TextView cancel = (TextView) dialog1.findViewById(R.id.cancel);
                        cancel_ll.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog1.dismiss();
                            }
                        });
                        audio_call_ll.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog1.dismiss();
                                if (ExpListItems.size() > 0) {
                                    check = false;
                                    dialog = new ProgressDialog(classContext);
                                    dialog.setMessage("Call Connecting...");
                                    dialog.setCancelable(false);
                                    dialog.show();
                                    ArrayList<String> grouplist = new ArrayList<String>();
                                    for (Group group : ExpListItems) {
                                        if (group.getIscheck()) {
                                            MainActivity.isAudioCall = true;
                                            grouplist = VideoCallDataBase.getDB(context).selectGroupmembers("select * from groupmember where groupid= '" + group.getId() + "'", "userid");
                                            /*for (String groupId : grouplist) {
                                                callNotification(Integer.parseInt(groupId), Appreference.loginuserdetails.getId());
                                                Appreference.broadcast_call = false;
                                            }*/
                                        }
                                    }
                                    ArrayList<Integer> group_list_id = new ArrayList<Integer>();
                                    for (String groupId : grouplist) {
                                        group_list_id.add(Integer.parseInt(groupId));
                                    }
                                    if(group_list_id.size() > 0) {
                                        callNotification(group_list_id, Appreference.loginuserdetails.getId());
                                        Appreference.broadcast_call = false;
                                    }

                                }
                            }
                        });

                        audio_broadcast_call_ll.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog1.dismiss();
                                if (ExpListItems.size() > 0) {
                                    check = false;
                                    dialog = new ProgressDialog(classContext);
                                    dialog.setMessage("Call Connecting...");
                                    dialog.setCancelable(false);
                                    dialog.show();
                                    ArrayList<String> grouplist = new ArrayList<String>();
                                    for (Group group : ExpListItems) {
                                        if (group.getIscheck()) {
                                            MainActivity.isAudioCall = true;
                                            grouplist = VideoCallDataBase.getDB(context).selectGroupmembers("select * from groupmember where groupid= '" + group.getId() + "'", "userid");
                                            /*for (String groupId : grouplist) {
                                                callNotification(Integer.parseInt(groupId), Appreference.loginuserdetails.getId());
                                                Appreference.broadcast_call = true;
                                            }*/
                                        }
                                    }
                                    ArrayList<Integer> group_list_id = new ArrayList<Integer>();
                                    for (String groupId : grouplist) {
                                        group_list_id.add(Integer.parseInt(groupId));
                                    }
                                    if(group_list_id.size() > 0) {
                                        callNotification(group_list_id, Appreference.loginuserdetails.getId());
                                        Appreference.broadcast_call = true;
                                    }
                                }
                            }
                        });
                        video_call_ll.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

//                            video_call.performClick();
                                dialog1.dismiss();
                                if (ExpListItems.size() > 0) {
                                    check = false;
                                    dialog = new ProgressDialog(classContext);
                                    dialog.setMessage("Call Connecting...");
                                    dialog.setCancelable(false);
                                    dialog.show();
                                    ArrayList<String> grouplist = new ArrayList<String>();
                                    for (Group group : ExpListItems) {
                                        if (group.getIscheck()) {
                                            MainActivity.isAudioCall = false;
                                            grouplist = VideoCallDataBase.getDB(context).selectGroupmembers("select * from groupmember where groupid= '" + group.getId() + "'", "userid");
                                            /*for (String groupId : grouplist) {
                                                callNotification(Integer.parseInt(groupId), Appreference.loginuserdetails.getId());
                                                Appreference.broadcast_call = true;
                                            }*/
                                        }
                                    }

                                    ArrayList<Integer> group_list_id = new ArrayList<Integer>();
                                    for (String groupId : grouplist) {
                                        group_list_id.add(Integer.parseInt(groupId));
                                    }
                                    if(group_list_id.size() > 0) {
                                        callNotification(group_list_id, Appreference.loginuserdetails.getId());
                                        Appreference.broadcast_call = true;
                                    }
                                }


//                            Appreference.broadcast_call = true;
                            }
                        });
                        try {
                            dialog1.show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else
                        Toast.makeText(getContext(), "Select user to make a call", Toast.LENGTH_LONG).show();


//                Intent intent = new Intent();
//                startActivity(intent);
                } else {

                }
                break;
            case R.id.video_call:
//                Intent intent1 = new Intent();
//                startActivity(intent1);
                makeCall(null, false);
//                    makeCall(null, false);
                Context wrapper1 = new ContextThemeWrapper(getContext(), R.style.myPopupMenuStyle);
                PopupMenu popup1 = new PopupMenu(wrapper1, ll_statusChange);
                //Inflating the Popup using xml file
                popup1.getMenuInflater().inflate(R.menu.videocall_types, popup1.getMenu());

                //registering popup with OnMenuItemClickListener
                popup1.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getTitle().equals("Video Call")) {
                            makeCall(null, false);
                        } else if (item.getTitle().equals("Video Broadcast Call")) {
                            Appreference.broadcast_call = true;
                            makeCall(null, false);
                        }
                        return true;
                    }
                });
                popup1.show();

                break;
        }
    }

    private void CreateExcelSheetForAllReport(ArrayList<TaskDetailsBean> allReportDetails) {
        //New Workbook
        HSSFWorkbook wb = new HSSFWorkbook();

        org.apache.poi.ss.usermodel.Cell c = null;

        //Cell style for header row
        CellStyle cs = wb.createCellStyle();
//        cs.setFillForegroundColor(HSSFColor.LIME.index);
        org.apache.poi.ss.usermodel.Font font=wb.createFont();
        font.setBoldweight(org.apache.poi.ss.usermodel.Font.BOLDWEIGHT_BOLD);
//        cs.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        cs.setFont(font);

        //New Sheet
        HSSFSheet sheet1 = null;
        sheet1 = wb.createSheet();


        // Generate column headings
        Row heading = sheet1.createRow(2);
        c = heading.createCell(5);
        c.setCellValue("AL SHIRAWI ENTERPRISES L.L.C.");
        c.setCellStyle(cs);

        Row sub_head = sheet1.createRow(5);
        c = sub_head.createCell(5);
        c.setCellValue("                TNA Report");
        c.setCellStyle(cs);

        Row row = sheet1.createRow(8);
        c = row.createCell(0);
        c.setCellValue("Job No");
        c.setCellStyle(cs);

        c = row.createCell(1);
        c.setCellValue("Customer Name");
        c.setCellStyle(cs);

        c = row.createCell(2);
        c.setCellValue("Task ID");
        c.setCellStyle(cs);

        c = row.createCell(3);
        c.setCellValue("Task Description");
        c.setCellStyle(cs);

        c = row.createCell(4);
        c.setCellValue("Date");
        c.setCellStyle(cs);

        c = row.createCell(5);
        c.setCellValue("Start Time");
        c.setCellStyle(cs);

        c = row.createCell(6);
        c.setCellValue("End Time");
        c.setCellStyle(cs);

        c = row.createCell(7);
        c.setCellValue("Employee Name");
        c.setCellStyle(cs);

        c = row.createCell(8);
        c.setCellValue("Estimated Travel Hrs");
        c.setCellStyle(cs);

        c = row.createCell(9);
        c.setCellValue("Estimated Activity Hrs");
        c.setCellStyle(cs);

        c = row.createCell(10);
        c.setCellValue("Total Travel Hrs");
        c.setCellStyle(cs);

        c = row.createCell(11);
        c.setCellValue("Total Activity Hrs");
        c.setCellStyle(cs);
        for(int i=0;i<allReportDetails.size();i++){
            TaskDetailsBean bean = (TaskDetailsBean) allReportDetails.get(i);
            Row row1 = sheet1.createRow(9+i);
            if(bean.getTaskNo()!=null) {
                c = row1.createCell(0);
                c.setCellValue(bean.getTaskNo());
            }else{
                c = row1.createCell(0);
                c.setCellValue("");
            }
            if(bean.getTaskName()!=null) {
                c = row1.createCell(1);
                c.setCellValue(bean.getTaskName());
            }else{
                c = row1.createCell(1);
                c.setCellValue("");
            }
            if(bean.getTaskId()!=null) {
                c = row1.createCell(2);
                c.setCellValue(bean.getTaskId());
            }else{
                c = row1.createCell(2);
                c.setCellValue("");
            }
            if(bean.getTaskDescription()!=null) {
                c = row1.createCell(3);
                c.setCellValue(bean.getTaskDescription());
            }else{
                c = row1.createCell(3);
                c.setCellValue("");
            }
            if(bean.getDateTime()!=null) {
                c = row1.createCell(4);
                c.setCellValue(bean.getDateTime());
            }else{
                c = row1.createCell(4);
                c.setCellValue("");
            }
            if(bean.getStartDate()!=null) {
                c = row1.createCell(5);
                c.setCellValue(bean.getStartDate());
            }else{
                c = row1.createCell(5);
                c.setCellValue("");
            }
            if(bean.getEndDate()!=null) {
                c = row1.createCell(6);
                c.setCellValue(bean.getEndDate());
            }else{
                c = row1.createCell(6);
                c.setCellValue("");
            }
            if(bean.getToUserName()!=null) {
                c = row1.createCell(7);
                c.setCellValue(bean.getToUserName());
            }else{
                c = row1.createCell(7);
                c.setCellValue("");
            }
            if(bean.getEstimatedTravel()!=null) {
                c = row1.createCell(8);
                c.setCellValue(bean.getEstimatedTravel());
            }else{
                c = row1.createCell(8);
                c.setCellValue("");
            }
            if(bean.getEstimatedActivity()!=null) {
                c = row1.createCell(9);
                c.setCellValue(bean.getEstimatedActivity());
            }else{
                c = row1.createCell(9);
                c.setCellValue("");
            }
            if(bean.getTotalTravel()!=null) {
                c = row1.createCell(10);
                c.setCellValue(bean.getTotalTravel());
            }else{
                c = row1.createCell(10);
                c.setCellValue("");
            }
            if(bean.getTotalActivity()!=null) {
                c = row1.createCell(11);
                c.setCellValue(bean.getTotalActivity());
            }else{
                c = row1.createCell(11);
                c.setCellValue("");
            }
        }
        sheet1.setColumnWidth(0, (15 * 300));
        sheet1.setColumnWidth(1, (15 * 300));
        sheet1.setColumnWidth(2, (15 * 300));
        sheet1.setColumnWidth(3, (15 * 300));
        sheet1.setColumnWidth(4, (15 * 300));
        sheet1.setColumnWidth(5, (15 * 300));
        sheet1.setColumnWidth(6, (15 * 300));
        sheet1.setColumnWidth(7, (15 * 300));
        sheet1.setColumnWidth(8, (15 * 300));
        sheet1.setColumnWidth(9, (15 * 300));
        sheet1.setColumnWidth(10, (15 * 300));
        sheet1.setColumnWidth(11, (15 * 300));
        sheet1.setColumnWidth(12, (15 * 300));


//        sheet1.setColumnWidth(1, (15 * 500));
//        sheet1.setColumnWidth(2, (15 * 500));

        // Create a path where we will place our List of objects on external storage
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/High Message";

        File dir = new File(path);
        if (!dir.exists())
            dir.mkdirs();

        File file = new File(dir, "ShareAllTaskFiles.xls");
        FileOutputStream os = null;

        try {
            os = new FileOutputStream(file);
            wb.write(os);
            Log.w("FileUtils", "Writing file" + file);
        } catch (IOException e) {
            Log.w("FileUtils", "Error writing " + file, e);
            e.printStackTrace();
        } catch (Exception e) {
            Log.w("FileUtils", "Failed to save file", e);
            e.printStackTrace();
        } finally {
            try {
                if (null != os)
                    os.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

    }

    private void dlgAddEditBuddy(BuddyConfig initial) {
        final BuddyConfig cfg = new BuddyConfig();
        final BuddyConfig old_cfg = initial;
        final boolean is_add = initial == null;

        LayoutInflater li = LayoutInflater.from(getActivity());
        View view = li.inflate(R.layout.dlg_add_buddy, null);

        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
        adb.setView(view);

        final EditText etUri = (EditText) view.findViewById(R.id.editTextUri);
        final CheckBox cbSubs = (CheckBox) view
                .findViewById(R.id.checkBoxSubscribe);

        if (is_add) {
            adb.setTitle("Add Buddy");
        } else {
            String edituserURI = initial.getUri();
            edituserURI = edituserURI.substring(4);
            edituserURI = edituserURI.split("@")[0];
            adb.setTitle("Edit Buddy");
            etUri.setText(edituserURI);
            cbSubs.setChecked(initial.getSubscribe());
        }

        adb.setCancelable(false);
        adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // cfg.setUri(etUri.getText().toString());
                String b_uri = "sip:" + etUri.getText().toString()
                        + "@" + getResources().getString(R.string.server_ip);
                cfg.setUri(b_uri);
                cfg.setSubscribe(cbSubs.isChecked());
                Log.i("buddyadd", "add button click");
                if (is_add) {
                    MainActivity.account.addBuddy(cfg);
                    total_buddyList.add(putData(cfg.getUri(), "", "unselected"));
                    Log.i("contact", "add is_add");
                    filterbuddy = (ArrayList<ContactBean>) totalbuddy.clone();
                    filter_buddyList = (ArrayList<Map<String, String>>) total_buddyList.clone();
                    buddyArrayAdapter.clear();
                    for (int i = 0, l = filter_buddyList.size(); i < l; i++)
                        buddyArrayAdapter.add(filterbuddy.get(i));
                    buddyArrayAdapter.notifyDataSetChanged();

                    buddyListSelectedIdx = -1;
                } else {
                    if (!old_cfg.getUri().equals(cfg.getUri())) {
                        MainActivity.account.delBuddy(buddyListSelectedIdx);
                        MainActivity.account.addBuddy(cfg);
                        total_buddyList.remove(buddyListSelectedIdx);
                        total_buddyList.add(putData(cfg.getUri(), "", "unselected"));
                        // buddyListAdapter.notifyDataSetChanged();
                        Log.i("contact", "add is_add else");
                        filterbuddy = (ArrayList<ContactBean>) totalbuddy.clone();
                        filter_buddyList = (ArrayList<Map<String, String>>) total_buddyList.clone();
                        buddyArrayAdapter.clear();
                        for (int i = 0, l = filter_buddyList.size(); i < l; i++)
                            buddyArrayAdapter.add(filterbuddy.get(i));
                        buddyArrayAdapter.notifyDataSetChanged();
//                        buddyArrayAdapter.notifyDataSetChanged();
                        buddyListSelectedIdx = -1;
                    } else if (old_cfg.getSubscribe() != cfg.getSubscribe()) {
                        Log.i("contact", "add is_add else if");
                        MyBuddy bud = MainActivity.account.buddyList
                                .get(buddyListSelectedIdx);
                        try {
                            bud.subscribePresence(cfg.getSubscribe());
                        } catch (Exception e) {
                        }
                    }
                }
                try {
                    Message m = Message.obtain(handler, 10);
                    m.sendToTarget();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        AlertDialog ad = adb.create();
        ad.show();
    }

    public void delBuddy() {
        if (buddyListSelectedIdx == -1)
            return;

        final HashMap<String, String> item = (HashMap<String, String>) expandableListView
                .getItemAtPosition(buddyListSelectedIdx);
        String buddy_uri = item.get("uri");

        DialogInterface.OnClickListener ocl = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        MainActivity.account.delBuddy(buddyListSelectedIdx);
                        total_buddyList.remove(item);
                        // buddyListAdapter.notifyDataSetChanged();
                        Log.i("contact", "delete buddy");
                        filterbuddy = (ArrayList<ContactBean>) totalbuddy.clone();
                        filter_buddyList = (ArrayList<Map<String, String>>) total_buddyList.clone();
                        buddyArrayAdapter.clear();
                        for (int i = 0, l = filter_buddyList.size(); i < l; i++)
                            buddyArrayAdapter.add(filterbuddy.get(i));
                        buddyArrayAdapter.notifyDataSetChanged();
                        buddyListSelectedIdx = -1;
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };

        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
        adb.setTitle(buddy_uri);
        adb.setMessage("\nDelete this buddy?\n");
        adb.setPositiveButton("Yes", ocl);
        adb.setNegativeButton("No", ocl);
        adb.show();
    }

    @Override
    public boolean handleMessage(Message m) {
        if (m.what == 0) {

            MainActivity.app.deinit();
//            finish();
            Runtime.getRuntime().gc();
            android.os.Process.killProcess(android.os.Process.myPid());

        } else if (m.what == MSG_TYPE.CALL_STATE) {

            CallInfo ci = (CallInfo) m.obj;

			/* Forward the message to CallActivity */
            if (CallActivity.handler_ != null) {
                Message m2 = Message.obtain(CallActivity.handler_,
                        MSG_TYPE.CALL_STATE, ci);
                m2.sendToTarget();
            }

        } else if (m.what == MSG_TYPE.CALL_MEDIA_STATE) {

			/* Forward the message to CallActivity */
            if (CallActivity.handler_ != null) {
                Message m2 = Message.obtain(CallActivity.handler_,
                        MSG_TYPE.CALL_MEDIA_STATE, null);
                m2.sendToTarget();
            }

        } else if (m.what == MSG_TYPE.BUDDY_STATE) {

            MyBuddy buddy = (MyBuddy) m.obj;
            int idx = MainActivity.account.buddyList.indexOf(buddy);
            Log.i("buddystate", "handle message buddy state--->" + buddy.getStatusText());

			/*
             * Update buddy status text, if buddy is valid and the buddy lists
			 * in account and UI are sync-ed.
			 */
            if (idx >= 0 && MainActivity.account.buddyList.size() == total_buddyList.size()) {
                total_buddyList.get(idx).put("status", buddy.getStatusText());
                // buddyListAdapter.notifyDataSetChanged();
                buddyArrayAdapter.notifyDataSetChanged();
                // TODO: selection color/mark is gone after this,
                // dont know how to return it back.
                // buddyListView.setSelection(buddyListSelectedIdx);
                // buddyListView.performItemClick(buddyListView,
                // buddyListSelectedIdx,
                // buddyListView.
                // getItemIdAtPosition(buddyListSelectedIdx));

				/* Return back Call activity */
                notifyCallState(MainActivity.currentCall);
            }

        } else if (m.what == MSG_TYPE.REG_STATE) {

            String msg_str = (String) m.obj;
            lastRegStatus = msg_str;

        } else if (m.what == MSG_TYPE.INCOMING_CALL) {

			/* Incoming call */
            final MyCall call = (MyCall) m.obj;
            CallOpParam prm = new CallOpParam();

			/* Only one call at anytime */
            if (MainActivity.currentCall != null) {
                /*
                 * prm.setStatusCode(pjsip_status_code.PJSIP_SC_BUSY_HERE); try
				 * { call.hangup(prm); } catch (Exception e) {}
				 */
                // TODO: set status code
                Log.i("SipVideo", " delete: b1");
                call.delete();
                return true;
            }

			/* Answer with ringing */
            prm.setStatusCode(pjsip_status_code.PJSIP_SC_RINGING);
            try {
                call.answer(prm);
            } catch (Exception e) {
            }
            MainActivity.currentCallArrayList.add(call);
//            currentCall = call;
            Log.i("SipVideo", "ContactFragment INCOMING CALL MainActivity.currentCall");
            MainActivity.currentCall = call;
            showCallActivity();

        } else if (m.what == 10) {
            MainActivity.app.saveDetails();
        } else {

			/* Message not handled */
            return false;

        }

        return true;
    }

    public void notifyCallState(MyCall call) {
        Log.i("SipVideo", "came to notifyCallState in ContactsFragment");
        if (MainActivity.currentCall == null || call.getId() != MainActivity.currentCall.getId())
            return;

        CallInfo ci;
        try {
            ci = call.getInfo();
        } catch (Exception e) {
            ci = null;
        }
        Message m = Message.obtain(handler, MSG_TYPE.CALL_STATE, ci);
        m.sendToTarget();


        if (ci != null
                && ci.getState() == pjsip_inv_state.PJSIP_INV_STATE_DISCONNECTED) {
            CallerDetailsBean cb = new CallerDetailsBean();
            cb.setPresence("Disconnected");
//            currentCall = null;
            for (int i = 0; i < MainActivity.currentCallArrayList.size(); i++) {
                if (MainActivity.currentCallArrayList.get(i).getId() == call.getId()) {
                    MainActivity.currentCallArrayList.remove(i);
                    break;
                }
            }
            if (MainActivity.audioMediaHashMap.containsKey(call.getId())) {
                MainActivity.audioMediaHashMap.remove(call.getId());
            }
            MainActivity.currentCall = null;
            Log.i("SipVideo", "ContactFragment notifyCallState MainActivity.currentCall=null");
        } else if (ci != null && ci.getState() == pjsip_inv_state.PJSIP_INV_STATE_CONNECTING) {
            CallerDetailsBean cb = new CallerDetailsBean();
            cb.setPresence("Connecting");
        } else if (ci != null && ci.getState() == pjsip_inv_state.PJSIP_INV_STATE_CONFIRMED) {
            CallerDetailsBean cb = new CallerDetailsBean();
            cb.setPresence("Connected");
        }
    }

    private void showCallActivity() {
        Log.i("SipVideo", "showCallActivity method");
        Intent intent = new Intent(getActivity(), CallActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        String par_name = Appreference.loginuserdetails.getFirstName() + " " + Appreference.loginuserdetails.getLastName();
        intent.putExtra("host", par_name);
        startActivity(intent);
    }


    @Override
    public void ResponceMethod(Object object) {

        CommunicationBean communicationBean = (CommunicationBean) object;
        String s1 = communicationBean.getEmail();
        String s2 = communicationBean.getFirstname();

        try {
            cancelDialog();
            Log.d("Call Response", "s1 is == " + s1 + " s2 is == " + s2);
            /*if (s2 != null && s2.equalsIgnoreCase("listUserGroupMemberAccess") && s1.isEmpty() == false) {
                JsonElement jelement = new JsonParser().parse(s1);
                Log.i("expand", " Responce" + s1);
                if (jelement.getAsJsonObject() != null) {
                    Log.i("expand", "ContactFragment -->1 if");
                    JsonObject jobject = jelement.getAsJsonObject();
                    if (jobject != null && jobject.has("Audio Access")) {
                        Log.i("expand", "ContactFragment -->2 if");
                        Audio_Access = jobject.get("Audio Access").toString();
                        Log.i("expand", "Audio Access -->3  " + Audio_Access);
                        Chat_Access = jobject.get("Chat Access").toString();
                        Log.i("expand", "Chat Access -->4  " + Chat_Access);
                        Video_Access = jobject.get("Video Access").toString();
                        Log.i("expand", "Video Access -->5  " + Video_Access);
                        Admin_Access = jobject.get("Admin Access").toString();
                        Log.i("expand", "Admin Access -->6  " + Admin_Access);
                        HidingIcons(Audio_Access, Chat_Access, Video_Access, Admin_Access);
                        isgroupicon = true;
                    }
                }
            } else*/
            if (s2 != null && s2.equalsIgnoreCase("callNotification")) {
                final JSONObject jsonObject = new JSONObject(communicationBean.getEmail());
                if (((int) jsonObject.get("result_code") == 0)) {
                    String result = (String) jsonObject.get("result_text");
                    Log.i("response", "result_text in response " + result);
                    //Toast.makeText(getContext(),result,Toast.LENGTH_LONG).show();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                if (((String) jsonObject.get("result_text")).equalsIgnoreCase("Your Call Notification Send Successfully.") && !check) {
                                    cancelDialog();
                                    Log.d("Remove", "cancel dialog");
                                    activateCall();
                                    Log.d("Remove", "active call dialog");
                                    check = true;
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });

                } else {
                    cancelDialog();
                    Toast.makeText(getContext(), "call conecting failure", Toast.LENGTH_SHORT).show();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void ErrorMethod(Object object) {
        try {
            cancelDialog();
            showToast("Please try again....");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void HidingIcons(String audio_access, String chat_access, String video_access, String admin_access) {
        final String audio, Chat, video, admin;
        audio = audio_access;
        Chat = chat_access;
        video = video_access;
        admin = admin_access;
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (audio.equalsIgnoreCase("0")) {
                    Log.i("expand", "audio if  " + audio);
                    audio_call.setVisibility(View.INVISIBLE);
                } else {
                    Log.i("expand", "audio else  " + audio);
                    if (!isgroupicon)
                        audio_call.setVisibility(View.VISIBLE);
                }
                if (Chat.equalsIgnoreCase("0")) {
                    Log.i("expand", "chat if  " + Chat);
                    chat.setVisibility(View.INVISIBLE);
                } else {
                    Log.i("expand", "chat else  " + Chat);
                    if (!isgroupicon)
                        chat.setVisibility(View.VISIBLE);
                }
                if (video.equalsIgnoreCase("0")) {
                    Log.i("expand", "video if  " + video);
//                    video_call.setVisibility(View.INVISIBLE);
                } else {
                    Log.i("expand", "video else  " + video);
//                    video_call.setVisibility(View.VISIBLE);
                }
            }
        });
    }


    public class MSG_TYPE {
        public final static int INCOMING_CALL = 1;
        public final static int CALL_STATE = 2;
        public final static int REG_STATE = 3;
        public final static int BUDDY_STATE = 4;
        public final static int CALL_MEDIA_STATE = 5;
    }

    public void activateCall() {
        Log.d("Remove", "inside active call dialog ");
        Log.i("Call Response", "activecall ");
        if (MainActivity.isAudioCall) {
            makeCall(null, true);
        } else {
            makeCall(null, false);
        }
    }

    public void makeCall(View view, boolean audio_call) {
        Log.i("SipVideo", "ContactsFrgament makecall ");
        if (buddyListSelectedIdx == -1)
            return;
        Log.i("SipVideo", "Came to makeCallMainActivity ");
        /* Only one call at anytime */
//        MainActivity.currentCall = null;
        if (MainActivity.currentCall != null) {

            Log.i("SipVideo", "Came to makeCallMain : Activity.currentCall != nul ");
//            showToast("Plesse Wait Some Times And Make Call");
/*            ArrayList<MyCall> curCall = (ArrayList<MyCall>) MainActivity.currentCallArrayList.clone();
            for (int i = 0; i < curCall.size(); i++) {
                if (curCall.get(i).getId() != 0) {
                    CallOpParam prm = new CallOpParam();
                    prm.setStatusCode(pjsip_status_code.PJSIP_SC_DECLINE);
                    try {
//                        Endpoint.instance().hangupAllCalls();
                        MainActivity.currentCallArrayList.get(i).hangup(prm);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    MainActivity.currentCallArrayList.remove(i);
//                        break;
//                }
                }
                if (MainActivity.currentCallArrayList.size() == 0) {
                    MainActivity.currentCall = null;
                    MainActivity.audioMediaHashMap.clear();
                }
            }*/
        }
//        for (int i = 0; i < MainActivity.positions.size(); i++) {
//            HashMap<String, String> item = (HashMap<String, String>) buddyListView
//                    .getItemAtPosition(buddyListSelectedIdx);
//            HashMap<String, String> item = (HashMap<String, String>) buddyListView
//                    .getItemAtPosition(MainActivity.positions.get(i));

          /*  try {
                Endpoint.instance().hangupAllCalls();
            } catch (Exception e) {
                e.printStackTrace();
            }*/

        MainActivity.currentCallArrayList.clear();

       /* if(MainActivity.currentCallArrayList.size() == 0){
            MainActivity.currentCall = null;

        }
*/
//        for (int position = 0; position < total_buddyList.size(); position++) {
//            Map<String, String> item = total_buddyList.get(position);
//            if (item.get("selected").equals("selected")) {
//                item.remove("selected");
//                item.put("selected", "unselected");
//        String buddy_uri = item.get("uri");
        if (contactTab) {
            if (buddyList != null && buddyList.size() > 0) {

                sendConferencecallInfomessage();
                for (ContactBean contactBean : buddyList) {
                    Log.i("swipe list", "buddyList " + buddyList.size());
                    if (contactBean.getIscheck()) {
                        if (contactBean.getUsername() != null) {
                            String buddy_uri = "sip:" + contactBean.getUsername() + "@" + getResources().getString(R.string.server_ip);
                            MyCall call = new MyCall(MainActivity.account, -1);
                            CallOpParam prm = new CallOpParam(true);
                            CallSetting opt = prm.getOpt();
                            opt.setAudioCount(1);
                            if (audio_call) {
                                opt.setVideoCount(0);
                                MainActivity.isAudioCall = true;
                            } else {
                                opt.setVideoCount(1);
                                MainActivity.isAudioCall = false;
                            }
                            Log.i("SipVideo", "make call : " + buddy_uri);
                            try {
                                call.makeCall(buddy_uri, prm);
//                    OnCallSdpCreatedParam param = new OnCallSdpCreatedParam();
//                    SdpSession pot = param.getSdp();
//                    call.onCallSdpCreated(param);
                            } catch (Exception e) {
                                e.printStackTrace();
                                Log.i("SipVideo", " delete: b2");
                                call.delete();
                                return;
                            }
                            MainActivity.currentCallArrayList.add(call);
//        currentCall = call;
                            MainActivity.currentCall = call;
                            Log.i("SipVideo", "ContactFragment Make Call MainActivity.currentCall");
                        }
                    }
                }
            }
        } else {
            if (ExpListItems != null && ExpListItems.size() > 0) {

                sendConferencecallInfomessage();
                for (Group group : ExpListItems) {
                    if (group.getIscheck()) {
                        if (group.getName() != null) {
                            Log.d("Remove", "name === " + group.getName());
                            ArrayList<String> grouplist = VideoCallDataBase.getDB(context).selectGroupmembers("select * from groupmember where groupid= '" + group.getId() + "'", "username");
                            for (String buddyName : grouplist) {


                                if (!Appreference.loginuserdetails.getUsername().equalsIgnoreCase(buddyName)) {
                                    String buddy_uri = "sip:" + buddyName + "@" + getResources().getString(R.string.server_ip);
                                    MyCall call = new MyCall(MainActivity.account, -1);
                                    CallOpParam prm = new CallOpParam(true);
                                    CallSetting opt = prm.getOpt();
                                    opt.setAudioCount(1);
                                    if (audio_call) {
                                        opt.setVideoCount(0);
                                        MainActivity.isAudioCall = true;
                                    } else {
                                        opt.setVideoCount(1);
                                        MainActivity.isAudioCall = false;
                                    }
                                    Log.i("SipVideo", "make group call : " + buddy_uri);
                                    try {
                                        call.makeCall(buddy_uri, prm);
//                    OnCallSdpCreatedParam param = new OnCallSdpCreatedParam();
//                    SdpSession pot = param.getSdp();
//                    call.onCallSdpCreated(param);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        Log.i("SipVideo", " delete: b21");
                                        call.delete();
                                        return;
                                    }
                                    MainActivity.currentCallArrayList.add(call);
//        currentCall = call;
                                    MainActivity.currentCall = call;
                                    Log.i("SipVideo", "ContactFragment Make Call MainActivity.currentCall");
                                }
                            }
                        }
                    }
                }
            }
        }
        buddyArrayAdapter.notifyDataSetChanged();
        Log.i("SipVideo", "MainActivity.currentCallArrayList.size()--->" + MainActivity.currentCallArrayList.size());
        if (MainActivity.currentCallArrayList.size() > 0) {
            Appreference.play_call_dial_tone = true;
            showCallActivity();
        } else {

        }
    }

    public void callNotification(ArrayList<Integer> toIds, int fromId) {
        try {
            Log.i("callNotification", "fromId " + String.valueOf(fromId));
//            Log.i("callNotification", "toid " + String.valueOf(toId));
            /*List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("fromId", String.valueOf(fromId)));
            nameValuePairs.add(new BasicNameValuePair("toId", String.valueOf(toId)));*/
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("fromId",String.valueOf(fromId));
            JSONObject jsonObject1=new JSONObject();
            JSONArray jsonArray=new JSONArray();
            for(int i = 0; i<toIds.size();i++) {
                int toId = toIds.get(i);
                jsonObject1.put("id", String.valueOf(toId));
//            JSONArray jsonArray=new JSONArray();
                jsonArray.put(i, jsonObject1);
            }
            jsonObject.put("to",jsonArray);
            Appreference.jsonRequestSender.callNotification(EnumJsonWebservicename.callNotification, jsonObject, ContactsFragment.this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void notifybuddystatus(MyBuddy buddy) {

        int idx = MainActivity.account.buddyList.indexOf(buddy);
        if (idx >= 0 && total_buddyList != null && MainActivity.account.buddyList.size() == total_buddyList.size()) {
            total_buddyList.get(idx).put("status", buddy.getStatusText());
            // buddyListAdapter.notifyDataSetChanged();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    buddyArrayAdapter.notifyDataSetChanged();
                }
            });

            // TODO: selection color/mark is gone after this,
            // dont know how to return it back.
            // buddyListView.setSelection(buddyListSelectedIdx);
            // buddyListView.performItemClick(buddyListView,
            // buddyListSelectedIdx,
            // buddyListView.
            // getItemIdAtPosition(buddyListSelectedIdx));

				/* Return back Call activity */
//            notifyCallState(currentCall);
        }
        try {
            if (buddy != null && buddyList != null) {
                BuddyInfo buddyInfo = buddy.getInfo();
                Log.i("buddystatus", "buddyuri-->" + buddyInfo.getUri());
                Log.i("buddystatus", "MyBuddy statusText-->" + buddy.getStatusText());
                for (ContactBean contactBean : buddyList) {
                    if (contactBean.getUsername() != null) {
                        String name = "sip:" + contactBean.getUsername() + "@" + getResources().getString(R.string.server_ip);
                        if (name.equalsIgnoreCase(buddyInfo.getUri())) {
                            if (buddyInfo.getSubState() == pjsip_evsub_state.PJSIP_EVSUB_STATE_ACTIVE) {
                                Log.i("buddystatus", "buddyuri-->PJSIP_EVSUB_STATE_ACTIVE if");
                                if (buddyInfo.getPresStatus().getStatus() ==
                                        pjsua_buddy_status.PJSUA_BUDDY_STATUS_ONLINE) {
                                    contactBean.setStatus("Online");
                                    Log.i("buddystatus", "buddystatus-->Online");
                                    if (buddyInfo.getPresStatus().getActivity().toString() != null && buddyInfo.getPresStatus().getActivity().toString().equalsIgnoreCase("PJRPID_ACTIVITY_AWAY")) {
                                        if (buddyInfo.getPresStatus().getNote() != null && buddyInfo.getPresStatus().getNote().equalsIgnoreCase("Away")) {
                                            contactBean.setStatus("Away");
                                        }
                                    } else if (buddyInfo.getPresStatus().getActivity().toString() != null && buddyInfo.getPresStatus().getActivity().toString().equalsIgnoreCase("PJRPID_ACTIVITY_BUSY")) {
                                        if (buddyInfo.getPresStatus().getNote() != null && buddyInfo.getPresStatus().getNote().equalsIgnoreCase("Busy")) {
                                            contactBean.setStatus("Busy");
                                        }
                                    }
                                } else if (buddyInfo.getPresStatus().getStatus() ==
                                        pjsua_buddy_status.PJSUA_BUDDY_STATUS_OFFLINE) {
                                    contactBean.setStatus("Offline");
                                    Log.i("buddystatus", "buddystatus-->Offline");
                                }
                            } else {
                                Log.i("buddystatus", "buddyuri-->PJSIP_EVSUB_STATE_ACTIVE else");
                                contactBean.setStatus("Offline");
                            }
                            VideoCallDataBase.getDB(classContext).updateUserPresense(contactBean, MainActivity.username);

                            break;
                        }
                    }
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        buddyArrayAdapter.notifyDataSetChanged();
                    }
                });

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showToast(final String content) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(classContext, content, Toast.LENGTH_LONG).show();
            }
        });
    }

    public class BuddyArrayAdapter extends ArrayAdapter<ContactBean> {

        ArrayList<ContactBean> arrayBuddyList;
        LayoutInflater inflater = null;
        Context adapContext;
        ImageLoader imageLoader1;

        public BuddyArrayAdapter(Context context, ArrayList<ContactBean> buddyList1) {
            super(context, R.layout.buddy_adapter_row, buddyList1);
            // TODO Auto-generated constructor stub
            arrayBuddyList = buddyList1;
            adapContext = context;
            imageLoader1 = new ImageLoader(adapContext);
        }

        @Override
        public int getCount() {
            return arrayBuddyList.size();
        }

        public ContactBean getItem(int position) {
            return arrayBuddyList.get(position);
        }

        @Override
        public long getItemId(int position) {

            return position;
        }

        public int getItemViewType(int position) {
            int value = 0;

            return value;
        }

        public View getView(final int pos, View conView, ViewGroup group) {
            try {


                if (conView == null) {
                    inflater = (LayoutInflater) adapContext
                            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    conView = inflater.inflate(R.layout.buddy_adapter_row, null,
                            false);
                }

                final View finalConView = conView;


                final ContactBean contactBean = arrayBuddyList.get(pos);
                TextView userName = (TextView) finalConView.findViewById(R.id.username);
                TextView count = (TextView) finalConView.findViewById(R.id.item_counter);
                ImageView imageView = (ImageView) finalConView.findViewById(R.id.view4);
                TextView Organization = (TextView) finalConView.findViewById(R.id.Organization);
//                TextView statusshower = (TextView) finalConView.findViewById(R.id.statusshower);
                ImageView statusIcon = (ImageView) finalConView.findViewById(R.id.status_icon);
                ImageView dependency_icon = (ImageView) finalConView.findViewById(R.id.dependency_icon);
//                View arrow_layout=(RelativeLayout)conView.findViewById(R.id.arrow_layout);
                TextView buddy_status = (TextView) finalConView.findViewById(R.id.status);
                ImageView state = (ImageView) finalConView.findViewById(R.id.iv_txtstatus1);
//                RelativeLayout contact_listViewBuddy=(RelativeLayout) finalConView.findViewById(R.id.contact_listViewBuddy);
//                if (contactBean.getRoles() != null)
                Log.i("ContactFragment", "usertype  " + contactBean.getUserType());
//                if (contactBean.getUserType() != null && !contactBean.getUserType().equalsIgnoreCase("")) {
//                    statusshower.setText(contactBean.getUserType());
//                }
//                if (contactBean.getProfession() != null && contactBean.getOrganization() != null) {
//                statusshower.setVisibility(View.VISIBLE);
                Organization.setVisibility(View.VISIBLE);
                Log.i("ContactFragment", "Organization " + contactBean.getOrganization() + "   Organization " + contactBean.getProfession());
                Log.i("ContactFragment", "Organization " + contactBean.getOrganization() + "   Organization " + contactBean.getSpecialization());
                Log.i("ContactFragment", "Organization " + contactBean.getOrganization() + "   Organization " + contactBean.getUserType());
                if (contactBean.getOrganization() != null && contactBean.getUserType() != null && !contactBean.getOrganization().equalsIgnoreCase("") && !contactBean.getUserType().equalsIgnoreCase("")) {
                    {
                        Organization.setText(contactBean.getUserType() + "," + contactBean.getOrganization());
                    }
                }
//                }
                int msgCount = contactBean.getMsg_count();

                if (msgCount == 0) {
                    count.setVisibility(View.INVISIBLE);
                } else {
                    count.setVisibility(View.VISIBLE);
                    count.setText(String.valueOf(msgCount));
                }
                Log.i("TaskStatus", "username " + contactBean.getUsername());
                Log.i("TaskStatus", "username " + contactBean.getGroupName());

//                userName.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent intent = new Intent(getContext(), TaskHistory.class);
//                        intent.putExtra("userId", contactBean.getUsername());
//                        intent.putExtra("taskType", "Individual");
//                        startActivity(intent);
//                    }
//                });
//                userName.setOnLongClickListener(new View.OnLongClickListener() {
//                    @Override
//                    public boolean onLongClick(View v) {
//                        Log.i("listview","on longclick layout ");
//                        Intent intent = new Intent(classContext, NewTaskConversation.class);
//                        TaskDetailsBean taskDetailsBean = new TaskDetailsBean();
//                        taskDetailsBean.setToUserId(String.valueOf(contactBean.getUserid()));
//                        taskDetailsBean.setToUserName(contactBean.getUsername());
//                        taskDetailsBean.setTaskType("Individual");
//                        intent.putExtra("task", "Newtask");
//                        intent.putExtra("newTaskBean", taskDetailsBean);
//                        startActivity(intent);
//                        return false;
//                    }
//                });
                /*if(contactBean.getIsprofile()){
                    Log.i("statusIcon", "visibility " + contactBean.getIsprofile());
                    statusIcon.setVisibility(View.GONE);

                }else{
                    Log.i("statusIcon", "visibility else " + contactBean.getIsprofile());
                    statusIcon.setVisibility(View.VISIBLE);
                }*/
                statusIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        contactBean = buddyList.get(position);
                        Log.i("ContactsFragment", "statusIcon.setOnClickListener ");
                        Intent intent = new Intent(getContext().getApplicationContext(), ViewProfile.class);
                        intent.putExtra("value", contactBean.getEmail());
                        startActivity(intent);
                    }
                });

                count.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.i("ContactsFragment", "count.setOnClickListener ");
                        Intent intent = new Intent(getContext(), TaskHistory.class);
                        intent.putExtra("userId", contactBean.getUsername());
                        intent.putExtra("taskType", "Individual");
                        startActivity(intent);
                    }
                });


                if (contactBean.getStatus() != null && !contactBean.getStatus().equalsIgnoreCase("")) {
                    Log.i("buddy", "Getstatus" + contactBean.getStatus());
                    Log.i("buddy", "GetFirstname " + contactBean.getFirstname());
                    if (contactBean.getStatus().equals("Online")) {
                        state.setBackground(getResources().getDrawable(R.drawable.online1));
                    } else if (contactBean.getStatus().equals("Offline")) {
                        state.setBackground(getResources().getDrawable(R.drawable.off_line1));
                    } else if (contactBean.getStatus().equals("Away")) {
                        state.setBackground(getResources().getDrawable(R.drawable.away));
                    } else if (contactBean.getStatus().equals("Busy")) {
                        state.setBackground(getResources().getDrawable(R.drawable.busy));
                    } else {
                        state.setBackground(getResources().getDrawable(R.drawable.off_line1));
                    }
                    buddy_status.setText(contactBean.getStatus());
//                    state.setBackground(getResources().getDrawable(R.drawable.off_line1));
                } else {
                    state.setBackground(getResources().getDrawable(R.drawable.off_line1));
                }

                if (contactBean.getProfileImage() != null) {
                    Log.i("file", "image name " + contactBean.getProfileImage());
                    File myFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/High Message/profilePic/" + contactBean.getProfileImage());
//                    imageLoader1.DisplayImage(myFile.toString(), imageView, R.drawable.personimage);
                    if (myFile.exists()) {
                        Log.i("file", "image name " + myFile.toString());
                        imageLoader1.DisplayImage(myFile.toString(), imageView, R.drawable.personimage);
                    } else {
                        MainActivity mainActivity = (MainActivity) Appreference.context_table.get("mainactivity");
                        mainActivity.callDownloadProfile(getResources().getString(R.string.user_upload) + contactBean.getProfileImage());
                        imageLoader1.DisplayImage(myFile.toString(), imageView, R.drawable.personimage);
                    }

                } else {
                    imageView.setBackgroundResource(R.drawable.personimage);
                }
                File myFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/High Message/profilePic/" + contactBean.getProfileImage());
                if (myFile.exists()) {
                    Log.i("file", "image name " + myFile.toString());
                    imageLoader1.DisplayImage(myFile.toString(), imageView, R.drawable.personimage);
                } else {
                    MainActivity mainActivity = (MainActivity) Appreference.context_table.get("mainactivity");
                    mainActivity.callDownloadProfile(getResources().getString(R.string.user_upload) + contactBean.getProfileImage());
                    imageLoader1.DisplayImage(myFile.toString(), imageView, R.drawable.personimage);
                }
                if (list.equals("Contact")) {
                    if (contactBean.getFirstname() != null && contactBean.getLastname() != null) {
                        userName.setText(contactBean.getFirstname() + " " + contactBean.getLastname());
                    }
                } else {
                    if (contactBean.getGroupName() != null) {
                        userName.setText(contactBean.getGroupName());
                    }
                }

                if (Appreference.is_reload == true && contactBean.getIscheck() == true) {
                    contactBean.setIscheck(false);
                    Appreference.is_reload = false;
                }

//                if (contactBean.getIscheck()) {
//                    userName.setTextColor(Color.BLACK);
//                } else {
                userName.setTextColor(Color.BLACK);
//                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return conView;

        }

        @Override
        public Filter getFilter() {
            if (filter == null) {
                filter = new ContactsFilter();
            }
            return filter;
        }
    }

    private class ContactsFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            //constraint = constraint.toString();
            String s1 = constraint.toString();

            Log.d("constraint", "filter : " + constraint.toString());

            Filter.FilterResults result = new FilterResults();
            if (constraint != null && constraint.toString().length() > 0) {
                Log.i("  ", "  " + constraint.toString().length());
                ArrayList<ContactBean> contactList = new ArrayList<>();
                ArrayList<ContactBean> s = new ArrayList<ContactBean>();

                for (int i = 0, l = totalbuddy.size(); i < l; i++) {
                    Log.i("inside", "insidefor" + totalbuddy.size());
                    s.add(totalbuddy.get(i));
                    //Log.i("sizes","sizes"+s.toString());
                    String s2 = s.get(i).getFirstname().toString() + " " + s.get(i).getLastname().toString();

                    if (s2.contains(s1.toString())) {
                        Log.i("insideif", "insideif" + String.valueOf(constraint));
                        contactList.add(s.get(i));
                        Log.i("contact", "List" + contactList.toString());
                    }

                }
                result.values = contactList;
                Log.i("result", "result" + result.values.toString());
                result.count = contactList.size();
                Log.i("resultcount", "resultcount" + result.count);

            } else {
                synchronized (this) {
                    result.values = totalbuddy;
                    Log.i("resultelse", "resultelse" + result.values);
                    result.count = totalbuddy.size();
                    Log.i("resultelsecount", "resultelsecount" + result.count);

                }
            }
            return result;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {

            Log.d("filter", "JNDSEJBJW  " + results.count);
            buddyList.clear();
            filterbuddy = (ArrayList<ContactBean>) results.values;
//            Log.i("filterbuddy", "filterbuddy" + filterbuddy.toString());
//            buddyArrayAdapter.clear();

//            buddyList.clear();
            for (int i = 0, l = filterbuddy.size(); i < l; i++) {
                //Log.i("result","filter"+filterbuddy.get(i).toString());
                // buddyList.add(filterbuddy.get(i));
                buddyList.add(filterbuddy.get(i));
//                Log.i("buddy","buddy"+buddyList.toString());
//                Log.i("buddy","size"+buddyList.size());
//                buddyArrayAdapter.add(filterbuddy.get(i));
//            buddyArrayAdapter=new BuddyArrayAdapter(getContext(),buddyList);
//                buddyArrayAdapter.clear();
//                buddyArrayAdapter = new BuddyArrayAdapter(getActivity(),filterbuddy);
//                buddyListView.setAdapter(buddyArrayAdapter);
//            }
//                buddyArrayAdapter.notifyDataSetChanged();

            }
            handler.post(new Runnable() {
                @Override
                public void run() {
                    buddyArrayAdapter.notifyDataSetChanged();
                }
            });

        }
    }

  /*  private void getIndexList(String[] fruits) {
        mapIndex = new LinkedHashMap<String, Integer>();

        for (int i = 0; i < fruits.length; i++) {
            //ContactBean contactBean = fruits.get(i);
            try {
                String fruit = fruits[i];
                Log.i("String", "Value" + fruit);
                String index = fruit.substring(0, 1);
                Log.i("String", "Value" + index);
                if (mapIndex.get(index) == null)
                    mapIndex.put(index, i);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }*/


   /* private void displayIndex() {

        LinearLayout indexLayout = (LinearLayout) getView().findViewById(R.id.side_index);
        TextView textView;
        List<String> indexList = new ArrayList<String>(mapIndex.keySet());
        Log.i("Display","Letter"+indexList.size());
        for (String index : indexList) {
            Log.i("Display","Letter"+index);
            textView = (TextView) getLayoutInflater(null).inflate(R.layout.item, null);
            textView.setText(index);
            textView.setOnClickListener(this);
            indexLayout.addView(textView);
        }
    }*/

    public ArrayList<Group> expand() {
        ArrayList<Group> list = new ArrayList<Group>();
        group_count = 0;
        ArrayList<Child> ch_list;
        grp = new ArrayList<Integer>();
        grp = VideoCallDataBase.getDB(classContext).getGroups(Appreference.loginuserdetails.getUsername());
        for (int j = 0; j < grp.size(); j++) {
            Group gru = new Group();
            String id = String.valueOf(grp.get(j));
            gru.setId(id);
            String group_name = VideoCallDataBase.getDB(classContext).getGroupName(id);
            gru.setName(group_name);
//            gru.setImage(grp.get(j));
            Log.i("Picasso", "image" + grp.get(j));
            String url = VideoCallDataBase.getDB(classContext).getGroupImage(id);
            Log.i("Picasso", "image" + url);
            gru.setImage(url);
            int msgCount = VideoCallDataBase.getDB(classContext).getContactsUnReadMsgCount(id, "Group");
            gru.setMsg_count(msgCount);
            Log.i("Picasso", "msgCount" + msgCount);
            if (msgCount > 0) {
                group_count = group_count + 1;
            }
            Log.i("Picasso", "group_count" + group_count);
//            int groupId = VideoCallDataBase.getDB(classContext).getGroupId(group_name);
//            Log.i("Picasso", "groupId is == " + groupId);
//            gru.setId(String.valueOf(groupId));
            child = new ArrayList<String>();
            child = VideoCallDataBase.getDB(classContext).getGroupMember(id);
            images = new ArrayList<String>();
            images = VideoCallDataBase.getDB(classContext).getMemberImage(id);
            String app_fst_lst = Appreference.loginuserdetails.getFirstName() + " " + Appreference.loginuserdetails.getLastName();
            Log.i("loginuser", "app_fst_lst " + app_fst_lst);
            ch_list = new ArrayList<Child>();
            for (int i = 0; i < child.size(); i++) {
                Child ch = new Child();
                Log.i("loginuser", "current user " + child.get(i));
                if (app_fst_lst != null && !app_fst_lst.equalsIgnoreCase(child.get(i))) {
                    ch.setName(child.get(i));
                    ch.setImage(images.get(i));
                    ch_list.add(ch);
                }
            }
            Collections.sort(ch_list, new CustomComparatorgroupContacts());
            gru.setItems(ch_list);
            list.add(gru);
        }
        return list;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("LifeCycle", "Fragment ==  onDestory");
        Appreference.context_table.remove("contactsfragment");
    }

    public void referesh() {
        Log.i("contacts", "referesh Method");
        if (buddyList != null && buddyList.size() == 0) {
            if (VideoCallDataBase.getDB(classContext).getContact(Appreference.loginuserdetails.getUsername()) != null) {
                list = "Contact";
                buddyList.clear();
                buddyList = VideoCallDataBase.getDB(classContext).getContact(Appreference.loginuserdetails.getUsername());
                Log.i("contacts", "Arraysize" + buddyList.size());
                buddyArrayAdapter = new BuddyArrayAdapter(getActivity(), buddyList);
                buddyListView.setAdapter(buddyArrayAdapter);
                Collections.sort(buddyList, new CustomComparator());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        buddyArrayAdapter.notifyDataSetChanged();
                    }
                });

            }
        }

    }

   /* public void replaceTaskFragment()
    {
        Fragment fragment = new TasksFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commitAllowingStateLoss();
    }*/

    public void sendConferencecallInfomessage() {
        try {

            Date strt_dt = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat(
                    "yyyy-MM-dd hh:mm:ss");

            Appreference.callStart_time = sdf.format(strt_dt);

            String conf_callinfo[] = new String[5];
            conf_callinfo[0] = MainActivity.username;
            if (Appreference.callStart_time != null)
                conf_callinfo[1] = Appreference.callStart_time;
            conf_callinfo[2] = "";
            conf_callinfo[3] = Utility.getSessionID() + "@testconferenceuri";
            if (Appreference.broadcast_call) {
                conf_callinfo[4] = "broadcastcall";
            } else {
                conf_callinfo[4] = "normalcall";
            }
            Appreference.conference_uri = conf_callinfo[3];
            String[] participantInfo;
            int noofparticipants = 0;
            int z = 1;
            if (contactTab) {
                for (ContactBean bean : buddyList) {
                    if (bean.getIscheck()) {
                        noofparticipants = z;
                        z++;
                    }
                }
            } else {
                for (Group bean : ExpListItems) {
//                    participantInfo[i] = bean.getUserName() + ","
//                            + bean.getToNnumber();
                    if (bean.getIscheck()) {
                        ArrayList<String> grouplist = VideoCallDataBase.getDB(context).selectGroupmembers("select * from groupmember where groupid= '" + bean.getId() + "'", "username");

                        for (String Name : grouplist) {
                            noofparticipants = z;
                            z++;

                        }


                    }
                }
            }
            participantInfo = new String[noofparticipants];

            int j = 0;
            if (contactTab) {
                for (ContactBean bean : buddyList) {
//                    participantInfo[i] = bean.getUserName() + ","
//                            + bean.getToNnumber();
                    if (bean.getIscheck()) {
                        participantInfo[j] = bean.getUsername();
                        Log.i("FT", "callerBean_Array" + bean.getUsername());

                        j++;
                    }
                }
            } else {
                for (Group bean : ExpListItems) {
//                    participantInfo[i] = bean.getUserName() + ","
//                            + bean.getToNnumber();
                    if (bean.getIscheck()) {
                        ArrayList<String> grouplist = VideoCallDataBase.getDB(context).selectGroupmembers("select * from groupmember where groupid= '" + bean.getId() + "'", "username");

                        for (String Name : grouplist) {
                            participantInfo[j] = Name;
                            Log.i("FT", "callerBean_Array" + Name);
                            j++;

                        }


                    }
                }
            }


            String chatinfo[] = new String[9];
            chatinfo[0] = "CallGroupChat";
            chatinfo[1] = "";
            chatinfo[2] = "";
            chatinfo[3] = "";
            chatinfo[4] = "";

            SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            String dateforrow = dateformat.format(new Date().getTime());
            chatinfo[5] = dateforrow;

            chatinfo[6] = "";

            String confURI = Appreference.conference_uri;
//            String[] chatmembers;

//                    if (isScheduled.equalsIgnoreCase("yes")) {
//                        if (AppReferences.schedulealert_name.containsKey(AppReferences.conferenceURI_Line1))
            chatinfo[1] = "";
//                    }

//                    if (chatinfo[1] == null || chatinfo[1].equalsIgnoreCase("")) {
//                        SimpleDateFormat dateformat = new SimpleDateFormat(
//                                "yyyy-MM-dd hh:mm:ss");
//                        String dateforrow = dateformat.format(new Date()
//                                .getTime());
            chatinfo[1] = "";
//                    }
            if (chatinfo[2] == null || chatinfo[2].equalsIgnoreCase("")) {
                chatinfo[2] = Utility.getSessionID();
            }
//                    if (AppReferences.isLine_1) {
//                        String chatmember = AppReferences.user_name;
//                        int i = 0;
//                        for (CallerBean bean : callerBean_Array) {
//                            Log.i("FT", "callerBean_Array" + bean.getToNnumber());
//                            chatmember = chatmember + "," + bean.getToNnumber();
//                        }

            chatinfo[3] = "";
//                    }
            chatinfo[4] = MainActivity.username;
            chatinfo[6] = "callchat";
            chatinfo[7] = Appreference.conference_uri;
            chatinfo[8] = "";
            Log.i("offlinecrash", "sendconfrenceCall method 1 line");

            String confInfoXml = xmlcomposer.composeConferenceInfoXML(
                    conf_callinfo, participantInfo, chatinfo);

            if (contactTab) {
                if (buddyList != null && buddyList.size() > 0) {
                    Log.i("call", "buddyList!=null && buddyList.size()>0");
                    for (int i = 0; i < MainActivity.account.buddyList.size(); i++) {
                        String name = MainActivity.account.buddyList.get(i).cfg.getUri();
                        Log.i("confinfo", "buddyname-->" + name);
                        for (ContactBean bean : buddyList) {
                            if (bean.getIscheck()) {
                                if (bean.getUsername() != null) {
                                    String nn = "sip:" + bean.getUsername() + "@" + getResources().getString(R.string.server_ip);
                                    Log.i("confinfo", "selected user-->" + nn);
                                    if (nn.equalsIgnoreCase(name)) {
                                        Log.i("call", "both users are same");
                                        Log.i("confinfo", "both users are same");
                                        MyBuddy myBuddy = MainActivity.account.buddyList.get(i);
                                        SendInstantMessageParam prm = new SendInstantMessageParam();
                                        prm.setContent(confInfoXml);

                                        boolean valid = myBuddy.isValid();
                                        Log.i("confinfo", "valid ======= " + valid);
                                        try {
                                            myBuddy.sendInstantMessage(prm);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                if (ExpListItems != null && ExpListItems.size() > 0) {
                    Log.i("call", "buddyList!=null && buddyList.size()>0");
                    for (int i = 0; i < MainActivity.account.buddyList.size(); i++) {
                        String name = MainActivity.account.buddyList.get(i).cfg.getUri();
                        Log.i("confinfo", "buddyname-->" + name);
                        for (Group group : ExpListItems) {
                            if (group.getIscheck()) {
                                if (group.getName() != null) {

                                    ArrayList<String> grouplist = VideoCallDataBase.getDB(context).selectGroupmembers("select * from groupmember where loginuser= '" + group.getName() + "'", "username");

                                    for (String Name : grouplist) {
                                        String nn = "sip:" + Name + "@" + getResources().getString(R.string.server_ip);
                                        Log.i("confinfo", "selected user-->" + nn);
                                        if (nn.equalsIgnoreCase(name)) {
                                            Log.i("call", "both users are same" + nn + "Name ==>>" + Name);
                                            Log.i("call", "both users are same and xml is == >" + confInfoXml);
                                            Log.i("confinfo", "both users are same");
                                            MyBuddy myBuddy = MainActivity.account.buddyList.get(i);
                                            SendInstantMessageParam prm = new SendInstantMessageParam();
                                            prm.setContent(confInfoXml);

                                            boolean valid = myBuddy.isValid();
                                            Log.i("confinfo", "valid ======= " + valid);
                                            try {
                                                myBuddy.sendInstantMessage(prm);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }

                                    }


                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void callrefresh() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                buddyArrayAdapter.notifyDataSetChanged();
            }
        });
    }

    public void ChangeLoginUserStatus(String status, String state) {
        if (MainActivity.app != null && MainActivity.account != null && MainActivity.accCfg != null && status != null)
            if (MainActivity.app.accList.size() != 0) {
//                MainActivity.account = MainActivity.app.accList.get(0);
                MainActivity.reRegister();
                MainActivity.accCfg = MainActivity.account.cfg;
                Log.i("status", "ChangeLoginUserStatus login user uri--->" + MainActivity.accCfg.getIdUri());
                try {

                    PresenceStatus presenceStatus = new PresenceStatus();
                    if (state.equalsIgnoreCase("online")) {
                        presenceStatus.setStatus(pjsua_buddy_status.PJSUA_BUDDY_STATUS_ONLINE);
                        if (status.equalsIgnoreCase("Online")) {
                            Appreference.currentPresenceStateIs = "Online";
                            presenceStatus.setNote(Appreference.currentPresenceStateIs);
                        } else if (status.equalsIgnoreCase("Away")) {
                            presenceStatus.setActivity(pjrpid_activity.PJRPID_ACTIVITY_AWAY);
                            Appreference.currentPresenceStateIs = "Away";
                            presenceStatus.setNote(Appreference.currentPresenceStateIs);
                        } else if (status.equalsIgnoreCase("Busy")) {
                            presenceStatus.setActivity(pjrpid_activity.PJRPID_ACTIVITY_BUSY);
                            Appreference.currentPresenceStateIs = "Busy";
                            presenceStatus.setNote(Appreference.currentPresenceStateIs);
                        }
                    } else {
                        presenceStatus.setStatus(pjsua_buddy_status.PJSUA_BUDDY_STATUS_OFFLINE);
                    }
                    MainActivity.account.setOnlineStatus(presenceStatus);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
    }

    /*public ArrayList<ContactBean> getContactList()
    {
        buddyList=VideoCallDataBase.getDB(classContext).getContact(Appreference.loginuserdetails.getUsername());
        for (ContactBean contactBean:buddyList) {
            int msgCount = VideoCallDataBase.getDB(classContext).getContactsUnReadMsgCount(contactBean.getUsername());
            contactBean.setMsg_count(msgCount);
            buddyList.add(contactBean);
        }
        return buddyList;
    }*/

    public void refresh() {
        if (buddyList != null && buddyList.size() > 0)
            buddyList.clear();

        if (buddyList1.size() == 0) {
            buddyList1 = VideoCallDataBase.getDB(classContext).getContact(Appreference.loginuserdetails.getUsername());
        }

        if (buddyList1.size() > 0) {
            for (ContactBean contactBean : buddyList1) {

         /*   if(contactBean.getUsername().equalsIgnoreCase(taskDetailsBean.getToUserId())){

            }*/
                int msgCount = VideoCallDataBase.getDB(classContext).getContactsUnReadMsgCount(String.valueOf(contactBean.getUserid()), "Individual");

                if (msgCount > 0) {
                    contact_count = contact_count + 1;
                }
                contactBean.setMsg_count(msgCount);

                Log.d("WhiteScreen", " name is == " + contactBean.getUsername() + " count is  == " + contactBean.getMsg_count());
                buddyList.add(contactBean);

            }

        }
        Log.d("WhiteScreen", " list size  is == " + buddyList.size());
        if (buddyList != null && buddyList.size() > 0)
            Collections.sort(buddyList, new CustomComparator());

        handler.post(new Runnable() {
            @Override
            public void run() {
                buddyArrayAdapter.notifyDataSetChanged();
                Log.d("WhiteScreen", " list notifydata set changed  is == " + buddyList.size());
            }
        });

        MainActivity mainActivity = (MainActivity) Appreference.context_table.get("mainactivity");
        mainActivity.BadgeReferece();
        groupListRefresh();

    }

    public void refresh_gcm() {
        buddyList1 = new ArrayList<>();
        Log.i("gcm", "buddylist size 0 " + buddyList.size());
        if (buddyList != null && buddyList.size() > 0)
            buddyList.clear();
        Log.i("gcm", "buddylist size 1 " + buddyList.size());

        if (buddyList1.size() == 0) {
            Log.i("gcm", "buddylist size 2 " + buddyList.size());
            buddyList1 = VideoCallDataBase.getDB(classContext).getContact(Appreference.loginuserdetails.getUsername());
            Log.i("gcm", "buddylist size 3 " + buddyList.size());
        }
        Log.i("gcm", "buddylist size 4 " + buddyList1.size());
        if (buddyList1.size() > 0) {
            Log.i("gcm", "buddylist size 5 " + buddyList1.size());
            for (ContactBean contactBean : buddyList1) {
                Log.i("gcm", "****** 1");

         /*   if(contactBean.getUsername().equalsIgnoreCase(taskDetailsBean.getToUserId())){

            }*/
                int msgCount = VideoCallDataBase.getDB(classContext).getContactsUnReadMsgCount(String.valueOf(contactBean.getUserid()), "Individual");
                Log.i("", "");
                if (msgCount > 0) {
                    Log.i("gcm", "****** 2");
                    contact_count = contact_count + 1;
                    Log.i("gcm", "****** 3");
                }
                contactBean.setMsg_count(msgCount);
                Log.i("gcm", "****** 4");
                /*if (Appreference.isPN && contact_count > 0) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            contact_batch.setVisibility(View.VISIBLE);
                            contact_batch.setText(String.valueOf(contact_count));
                            Appreference.isPN = false;
                        }
                    });

                }*/
                Log.d("WhiteScreen", " name is == " + contactBean.getUsername() + " count is  == " + contactBean.getMsg_count());
                buddyList.add(contactBean);
                Log.i("gcm", "****** 5");

            }

        }
        Log.d("WhiteScreen", " list size  is == " + buddyList.size());
        if (buddyList != null && buddyList.size() > 0)
            Collections.sort(buddyList, new CustomComparator());
        Log.i("gcm", "****** 6");


        MainActivity mainActivity = (MainActivity) Appreference.context_table.get("mainactivity");
        mainActivity.BadgeReferece();
        Log.i("gcm", "****** 8");
        groupListRefresh();
        Log.i("gcm", "****** 9");
        handler.post(new Runnable() {
            @Override
            public void run() {
                buddyArrayAdapter.notifyDataSetChanged();
                Log.i("gcm", "****** 10");
                Log.d("WhiteScreen", " list notifydata set changed  is == " + buddyList.size());
            }
        });
//        refresh_contact();


    }

    public void refresh_contact_new() {

        handler.post(new Runnable() {
            @Override
            public void run() {
                buddyArrayAdapter.notifyDataSetChanged();
                Log.d("WhiteScreen", " list notifydata set changed  is == " + buddyList.size());
            }
        });
    }


    public void refresh_contact() {


        handler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    buddyList.clear();


                    if (buddyList1.size() > 0) {
                        for (ContactBean contactBean : buddyList1) {

             /*   if(contactBean.getUsername().equalsIgnoreCase(taskDetailsBean.getToUserId())){

                }*/
                            int msgCount = VideoCallDataBase.getDB(classContext).getContactsUnReadMsgCount(String.valueOf(contactBean.getUserid()), "Individual");

                            if (msgCount > 0) {
                                contact_count = contact_count + 1;
                            }
                            contactBean.setMsg_count(msgCount);
                           /* if(getTaskObservers(contactBean.getUsername())){
                                contactBean.setMsg_count(0);
                            }*/

                            Log.d("WhiteScreen", " name is == " + contactBean.getUsername() + " count is  == " + contactBean.getMsg_count());
                            buddyList.add(contactBean);

                        }

                        Collections.sort(buddyList, new CustomComparator());
                        buddyArrayAdapter.notifyDataSetChanged();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public void groupListRefresh() {

        handler.post(new Runnable() {
            @Override
            public void run() {
                ExpListItems = expand();
                Log.i("contact", "contactTab_1 " + contactTab_1);
                if (contactTab_1) {
                    Log.i("contact", "group_count after " + group_count);
                    if (group_count != 0 && group_count > 0) {
                        group_batch.setVisibility(View.VISIBLE);
                        group_batch.setText(String.valueOf(group_count));
                    } else {
                        group_batch.setVisibility(View.GONE);
                    }
                    Log.i("contact", "group_count before " + group_count);
                }
                Collections.sort(ExpListItems, new CustomComparatorgroup());
                ExpAdapter = new ExpandableAdapter(getActivity(), ExpListItems);
                expandableListView.setAdapter(ExpAdapter);
            }
        });

    }

    public void showNetworkStateUI() {
        if (ll_networkUI != null && tv_networkstate != null) {
            if (Appreference.networkState) {
                if (Appreference.sipRegistrationState) {
                } else if (!Appreference.sipRegistrationState) {
                    ll_networkUI.setVisibility(View.VISIBLE);
                    ll_networkUI.setBackgroundColor(getResources().getColor(R.color.orange));
                    tv_networkstate.setText("Connecting...");
                    iv_txtstatus.setBackground(getResources().getDrawable(R.drawable.away));
                }
            } else if (!Appreference.networkState) {
                ll_networkUI.setVisibility(View.VISIBLE);
                ll_networkUI.setBackgroundColor(getResources().getColor(R.color.red_color));
                tv_networkstate.setText("No Internet Connection");
                iv_txtstatus.setBackground(getResources().getDrawable(R.drawable.away));
            }
        }
    }

    public boolean getTaskObservers(String userName) {
        String query1 = "select * from taskDetailsInfo where loginuser ='" + Appreference.loginuserdetails.getEmail() + "' and sendStatus = '3' order by id DESC LIMIT 1";

        Log.d("TaskObserver", "get Observer query  " + query1);

        ArrayList<TaskDetailsBean> arrayList;
        ArrayList<String> listOfObservers = null;
        String value = null;
        boolean observerCheck = false;
        arrayList = VideoCallDataBase.getDB(context).getTaskHistory(query1);

        Log.d("TaskObserver", "Task Observer list size is == " + arrayList.size());
        if (arrayList.size() > 0) {
            TaskDetailsBean taskDetailsBean = arrayList.get(0);

            String taskObservers = taskDetailsBean.getTaskObservers();
            Log.d("TaskObserver", "Task Observer  == " + taskObservers);
            boolean check = false;
            if (taskObservers.contains(Appreference.loginuserdetails.getUsername())) {
                check = false;
            } else if (taskDetailsBean.getOwnerOfTask().equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                check = false;
            } else
                check = !taskDetailsBean.getTaskReceiver().equalsIgnoreCase(Appreference.loginuserdetails.getUsername());

            int counter = 0;
            for (int i = 0; i < taskObservers.length(); i++) {
                if (taskObservers.charAt(i) == ',') {
                    counter++;
                }
            }
            Log.d("TaskObserver", "Task Observer counter size is == " + counter);

            for (int j = 0; j < counter + 1; j++) {
                if (Appreference.loginuserdetails.getUsername().equalsIgnoreCase(taskObservers.split(",")[j])) {
                    value = taskObservers.split(",")[j];
                    if (taskDetailsBean.getOwnerOfTask().equalsIgnoreCase(userName)) {
                        observerCheck = false;
                    } else {
                        observerCheck = true;
                    }
                    Log.d("TaskObserver", "Task Observer name not in same user== " + taskObservers.split(",")[j]);
                }
            }


        }
        return observerCheck;
    }

    public void showOnlineState() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                iv_txtstatus.setBackground(getResources().getDrawable(R.drawable.online1));
                Log.d("Presence", "after connected state");
            }
        });
    }

    public void showBusyState() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                iv_txtstatus.setBackground(getResources().getDrawable(R.drawable.away));
                Log.d("Presence", "before connected state");
            }
        });
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
                            iv_txtstatus.setBackground(getResources().getDrawable(R.drawable.online1));
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


    public class ExpandableAdapter extends BaseExpandableListAdapter {

        private Context context;
        private ArrayList<Group> groups;
        ImageLoader imageLoader = new ImageLoader(context);


        public ExpandableAdapter(Context context, ArrayList<Group> groups) {
            this.context = context;
            this.groups = groups;
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            ArrayList<Child> chList = groups.get(groupPosition)
                    .getItems();
            return chList.get(childPosition);
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            // TODO Auto-generated method stub
            return childPosition;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition,
                                 boolean isLastChild, View convertView, ViewGroup parent) {

            Child child = (Child) getChild(groupPosition,
                    childPosition);
            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.child_item, null);
            }
            TextView tv = (TextView) convertView.findViewById(R.id.country_name);
            ImageView iv = (ImageView) convertView.findViewById(R.id.flag);
            Log.i("Picasso", "Child" + child.getImage());

            File myFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/High Message/profilePic/" + child.getImage());
            if (child.getImage() != null) {
                if (myFile.exists()) {
                    imageLoader.DisplayImage(myFile.toString(), iv, R.drawable.personimage);
                    //Picasso.with(context).load("http://122.165.92.171:8080/uploads/highmessaging/user/" + child.getImage()).into(iv);
                } else {
                    MainActivity mainActivity = (MainActivity) Appreference.context_table.get("mainactivity");
                    mainActivity.callDownloadProfile(getResources().getString(R.string.user_upload) + child.getImage());
                    imageLoader.DisplayImage(myFile.toString(), iv, R.drawable.personimage);
                }
            } else {
                iv.setBackgroundResource(R.drawable.personimage);
            }

            tv.setText(child.getName().toString());
            if (myFile.exists()) {
                imageLoader.DisplayImage(myFile.toString(), iv, R.drawable.personimage);
//                Picasso.with(context).load("http://122.165.92.171:8080/uploads/highmessaging/user/" + child.getImage()).into(iv);
            } else {
                MainActivity mainActivity = (MainActivity) Appreference.context_table.get("mainactivity");
                mainActivity.callDownloadProfile(getResources().getString(R.string.user_upload) + child.getImage());
                imageLoader.DisplayImage(myFile.toString(), iv, R.drawable.personimage);
            }
            //Picasso.with(context).load("http://122.165.92.171:8080/uploads/highmessaging/user/" + child.getImage()).into(iv);
//            imageLoader.DisplayImage(Environment.getExternalStorageDirectory().getAbsolutePath() + "/High Message/profilePic/"+child.getImage(),iv,R.drawable.default_person_circle);
            // tv.setText(child.getName().toString()+"::"+child.getTag());
            // tv.setTag(child.getTag());
            // TODO Auto-generated method stub
            return convertView;

        }

        @Override
        public int getChildrenCount(int groupPosition) {
            ArrayList<Child> chList = groups.get(groupPosition)
                    .getItems();

            return chList.size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            // TODO Auto-generated method stub
            return groups.get(groupPosition);
        }

        @Override
        public int getGroupCount() {
            // TODO Auto-generated method stub
            return groups.size();
        }

        @Override
        public long getGroupId(int groupPosition) {
            // TODO Auto-generated method stub
            return groupPosition;
        }

        @Override
        public View getGroupView(final int groupPosition, boolean isExpanded,
                                 View convertView, ViewGroup parent) {
            final Group group = (Group) getGroup(groupPosition);
            if (convertView == null) {
                LayoutInflater inf = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inf.inflate(R.layout.group_item, null);
            }
            final TextView tv = (TextView) convertView.findViewById(R.id.group_name);
            ImageView iv = (ImageView) convertView.findViewById(R.id.flag1);
            final ImageView call_state = (ImageView) convertView.findViewById(R.id.groupmenucall);
//            final ImageView conference_call = (ImageView) convertView.findViewById(R.id.conference_call);
//            final ImageView broadcast_audio_call = (ImageView) convertView.findViewById(R.id.broadcast_audio_call);
//            final ImageView broadcast_video_call = (ImageView) convertView.findViewById(R.id.broadcast_video_call);
//            final ImageView group_chat = (ImageView) convertView.findViewById(R.id.chat_icon);
            final Boolean[] call_visibility = {false};
//            ImageView group_history = (ImageView) convertView.findViewById(R.id.group_icon);
//            group_history.setVisibility(View.VISIBLE);
//            conference_call.setVisibility(View.GONE);
//            broadcast_audio_call.setVisibility(View.GONE);
//            broadcast_video_call.setVisibility(View.GONE);
//            group_chat.setVisibility(View.GONE);
            TextView count_icon = (TextView) convertView.findViewById(R.id.item_counter1);
            call_state.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    buddyListSelectedIdx = groupPosition;
                    if (group.getIscheck()) {
                        Log.i("ContactFragment", "group call_state 1 if click");
                        group.setIscheck(false);
                    } else {
                        Log.i("ContactFragment", "group call_state  1 else click");
                        group.setIscheck(true);
                    }
                    final String AudioAccess = VideoCallDataBase.getDB(context).getGroupMemberAccess("select audioAccess from listUserGroupMemberAccess where groupid ='" + group.getId() + "'");
                    final String VideoAccess = VideoCallDataBase.getDB(context).getGroupMemberAccess("select videoAccess from listUserGroupMemberAccess where groupid ='" + group.getId() + "'");
                    final String conferenceAccess = VideoCallDataBase.getDB(context).getGroupMemberAccess("select respondConfCall from listUserGroupMemberAccess where groupid ='" + group.getId() + "'");
                    final String chatAccess = VideoCallDataBase.getDB(context).getGroupMemberAccess("select chatAccess from listUserGroupMemberAccess where groupid ='" + group.getId() + "'");
                    final String assingNew = VideoCallDataBase.getDB(context).getGroupMemberAccess("select GroupTask from listUserGroupMemberAccess where groupid ='" + group.getId() + "'");
//                    final String ExistingTasks = VideoCallDataBase.getDB(context).getGroupMemberAccess("select chatAccess from listUserGroupMemberAccess where groupid ='" + group.getId() + "'");
//                    final String viewProfile = VideoCallDataBase.getDB(context).getGroupMemberAccess("select chatAccess from listUserGroupMemberAccess where groupid ='" + group.getId() + "'");

                    Log.i("GroupAccess", " Access Value !!! " + AudioAccess + VideoAccess + conferenceAccess + chatAccess);
                    Context wrapper = new ContextThemeWrapper(getContext(), R.style.myPopupMenuStyle);
                    group_popup = call_state;
                    final PopupMenu popup = new PopupMenu(wrapper, group_popup);
                    //Inflating the Popup using xml file
//               popup.getMenu().getItem(1).setChecked(true);
                    popup.getMenuInflater().inflate(R.menu.group_call_menu_items, popup.getMenu());
                    Menu m = popup.getMenu();
                    MenuItem item_assingNew = m.findItem(R.id.item_assingNew);
                    MenuItem View_ExistingTasks = m.findItem(R.id.View_ExistingTasks);
                    MenuItem item_video = m.findItem(R.id.item_broadcast_video_call);
                    MenuItem item_audio = m.findItem(R.id.item_broadcast_audio_call);
                    MenuItem item_conf = m.findItem(R.id.item_conference_call);
                    MenuItem item_chat = m.findItem(R.id.item_Chat);

                    if (assingNew != null && assingNew.contains("0")) {
                        item_assingNew.setVisible(false);
                        Log.i("groupMemberAccess", "assingNew !!! ");
                    }
                    if (AudioAccess != null && AudioAccess.contains("0")) {
                        item_audio.setVisible(false);
                        Log.i("groupMemberAccess", "AudioAccess !!! ");
                    }
                    if (VideoAccess != null && VideoAccess.contains("0")) {
                        item_video.setVisible(false);
                        Log.i("groupMemberAccess", "VideoAccess !!! ");
                    }
                    if (conferenceAccess != null && conferenceAccess.contains("0")) {
                        item_conf.setVisible(false);
                        Log.i("groupMemberAccess", "conferenceAccess !!! ");
                    }
                    if (chatAccess != null && chatAccess.contains("0")) {
                        item_chat.setVisible(false);
                        Log.i("groupMemberAccess", "chatAccess !!! ");
                    }
                    //registering popup with OnMenuItemClickListener
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {

                            if (item.getTitle().equals("Assign New Task")) {
                                Log.i("ContactsFragment", "Assign New Task ");
                                Intent intent = new Intent(context, NewTaskConversation.class);
                                TaskDetailsBean taskDetailsBean = new TaskDetailsBean();
                                taskDetailsBean.setToUserId(group.getId());
                                taskDetailsBean.setToUserName(group.getName());
                                taskDetailsBean.setTaskType("Group");
                                intent.putExtra("task", "Newtask");
                                intent.putExtra("newTaskBean", taskDetailsBean);
                                startActivity(intent);
                            } else if (item.getTitle().equals("View Existing Tasks")) {
                                Log.i("ContactsFragment", "View Existing Tasks " + group.getId());
                                Intent i = new Intent(context, TaskHistory.class);
                                i.putExtra("userId", group.getId());
                                i.putExtra("taskType", "Group");
                                startActivity(i);
                            } else if (item.getTitle().equals("Conference Call")) {
                                conferencecall();
                            } else if (item.getTitle().equals("Broadcast Audio Call")) {
                                broadcastaudiocall();
                            } else if (item.getTitle().equals("Broadcast Video Call")) {
                                broadcastvideocall();
                            } else if (item.getTitle().equals("Chat")) {
                                Intent i = new Intent(context, NewTaskConversation.class);
                                ArrayList<TaskDetailsBean> taskDetailsBean = VideoCallDataBase.getDB(classContext).getChatnames(group.getName());
                                if (taskDetailsBean != null && taskDetailsBean.size() > 0 && taskDetailsBean.get(0) != null) {
                                    Log.i("chat", "Chatetails size--->" + taskDetailsBean.get(0).getToUserId());
                                    Log.i("chat", "db datetime-->" + taskDetailsBean.get(0));
                                    Log.i("chat", "db cahtid--->" + taskDetailsBean.get(0));
                                    i.putExtra("chatid", taskDetailsBean.get(0).getTaskId());
                                    i.putExtra("task", "chathistory");
                                    i.putExtra("chatHistoryBean", taskDetailsBean.get(0));
                                    i.putExtra("catagory", taskDetailsBean.get(0).getCatagory());
                                } else {
                                    i.putExtra("task", "chat");
                                    i.putExtra("type", "group");
                                    i.putExtra("touser", group.getName());
                                    i.putExtra("touserid", group.getId());
                                }
                                startActivity(i);
                            } else if (item.getTitle().equals("Cancel")) {
                                popup.dismiss();
                            }
                            return true;
                        }
                    });
                    popup.show();
                }
            });


           /* if (group != null)
                if (group.getIscheck()) {
                    tv.setTextColor(Color.BLUE);
                } else {
                    tv.setTextColor(Color.BLACK);
                }*/

            if (group.getMsg_count() == 0) {
                count_icon.setVisibility(View.GONE);
            } else {
                count_icon.setVisibility(View.VISIBLE);
                count_icon.setText(String.valueOf(group.getMsg_count()));
            }


            ContactBean cb = null;
            if (cb != null) {
                Log.i("TaskStatus", "value 1 " + cb.getUsername());
                Log.i("TaskStatus", "value 1 " + cb.getGroupName());
                Log.i("TaskStatus", "value 1 " + cb.getLoginuser());
                Log.i("TaskStatus", "value 1 " + cb.getGroupOwner());
            }
//            group_history.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent i = new Intent(context, TaskHistory.class);
//                    i.putExtra("userId", group.getName());
//                    Log.i("TaskStatus", "value 1 " + group.getName() + group.getId());
//                    i.putExtra("taskType", "Group");
//                    startActivity(i);
//                }
//            });

            /*tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   *//* Intent i = new Intent(context, TaskHistory.class);
                    i.putExtra("userId", group.getName());
                    i.putExtra("taskType", "Group");
                    startActivity(i);*//*
                    Log.i("ContactFragment", "tv.setOnClickListener ");
                    Intent intent = new Intent(getContext(), Contactlistpage.class);
                    intent.putExtra("userId", group.getId());
                    intent.putExtra("taskType", "Group");
                    startActivity(intent);
                }
            });*/
            tv.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Log.i("contactfragment", "groupMemberAccess 1 " + group.getId());
                    GroupMemberAccess groupMemberAccess;
                    groupMemberAccess = VideoCallDataBase.getDB(context).getMemberAccessList(String.valueOf(group.getId()));
                    Log.i("contactfragment", "groupMemberAccess ---> 1 " + groupMemberAccess.getGroup_Task());
                    final String assign_New = VideoCallDataBase.getDB(context).getGroupMemberAccess("select GroupTask from listUserGroupMemberAccess where groupid ='" + group.getId() + "'");
                    if (assign_New != null && assign_New.contains("0")) {
                        Toast.makeText(context, "You are not rights to GroupTaskAccess ", Toast.LENGTH_SHORT).show();
                        Log.i("contactfragment", "groupMemberAccess getGroup_Task 2  " + groupMemberAccess.getGroup_Task());
                    } else {
                        Log.i("contactfragment", "groupMemberAccess getGroup_Task 3  " + groupMemberAccess.getGroup_Task());
                        assignnewtask();
                    }
                    return true;
                }

                public void assignnewtask() {
                    Log.d("Group", "ItemLongClicked child true  == " + ExpandableListView.PACKED_POSITION_TYPE_CHILD);
                    Group group = ExpListItems.get(groupPosition);
                    Intent intent = new Intent(classContext, NewTaskConversation.class);
                    Log.d("task", "toUserId" + group.getId());
                    TaskDetailsBean taskDetailsBean = new TaskDetailsBean();
                    taskDetailsBean.setToUserId(group.getId());
                    taskDetailsBean.setToUserName(group.getName());
                    taskDetailsBean.setTaskType("Group");
                    intent.putExtra("task", "Newtask");
                    intent.putExtra("newTaskBean", taskDetailsBean);
                    startActivity(intent);
                    Log.d("Group", "ItemLongClicked group true ==  " + ExpandableListView.PACKED_POSITION_TYPE_GROUP);
                }
            });

            count_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, TaskHistory.class);
                    i.putExtra("userId", group.getId());
                    i.putExtra("taskType", "Group");
                    startActivity(i);
                }
            });
            /*if (group.getIscheck()){
                tv.setTextColor(Color.BLUE);
            }else
            {
                tv.setTextColor(Color.BLACK);
            }*/
            Log.i("Picasso", "Group " + group.getImage());
            File myFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/High Message/groupPic/" + group.getImage());
            if (group.getImage() != null) {
                if (myFile.exists()) {
                    Log.i("setting", "setting");
                    //Picasso.with(context).load("http://122.165.92.171:8080/uploads/highmessaging/group/" + group.getImage()).into(iv);
                    imageLoader.DisplayImage(myFile.toString(), iv, R.drawable.default_person_circle);
                } else {
                    MainActivity mainActivity = (MainActivity) Appreference.context_table.get("mainactivity");
                    mainActivity.callDownloadProfile("http://122.165.92.171:8080/uploads/highmessaging/group/" + group.getImage());
                    imageLoader.DisplayImage(myFile.toString(), iv, R.drawable.default_person_circle);
                }
            }
            tv.setText(group.getName());
//            imageLoader.DisplayImage(Environment.getExternalStorageDirectory().getAbsolutePath() + "/High Message/groupPic/"+group.getImage(),iv,R.drawable.default_person_circle);
            if (myFile.exists()) {
                Log.i("setting", "setting");
                //Picasso.with(context).load("http://122.165.92.171:8080/uploads/highmessaging/group/" + group.getImage()).into(iv);
                imageLoader.DisplayImage(myFile.toString(), iv, R.drawable.default_person_circle);
            } else {
                MainActivity mainActivity = (MainActivity) Appreference.context_table.get("mainactivity");
                mainActivity.callDownloadgroupProfile("http://122.165.92.171:8080/uploads/highmessaging/group/" + group.getImage());
                imageLoader.DisplayImage(myFile.toString(), iv, R.drawable.default_person_circle);
            }

            //Picasso.with(context).load("http://122.165.92.171:8080/uploads/highmessaging/group/" + group.getImage()).into(iv);
            // TODO Auto-generated method stub
            return convertView;

        }


        @Override
        public boolean hasStableIds() {
            // TODO Auto-generated method stub
            return true;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            // TODO Auto-generated method stub
            return true;
        }

        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
        }
    }

    public void broadcastvideocall() {
        Log.i("ContactFragment", "group broadcast_video_call click");
        if (MainActivity.gsmCallState == TelephonyManager.CALL_STATE_IDLE) {
            boolean select = false;
            boolean groupIsSelect = false;
            Log.i("audiocall", "call button click");

//                                    for (Group group : ExpListItems) {
//                                        if (group.getIscheck()) {
            groupIsSelect = true;
            Log.i("ContactFragment", "group broadcast_video_call click groupIsSelect " + groupIsSelect);
//                                        }
//                                    }
            if (groupIsSelect) {
                if (ExpListItems.size() > 0) {
                    check = false;
                    dialog = new ProgressDialog(classContext);
                    dialog.setMessage("Call Connecting...");
                    dialog.setCancelable(false);
                    dialog.show();
                    for (Group group : ExpListItems) {
                        if (group.getIscheck()) {
                            MainActivity.isAudioCall = false;
                            ArrayList<String> grouplist = VideoCallDataBase.getDB(context).selectGroupmembers("select * from groupmember where groupid= '" + group.getId() + "'", "userid");
//                            for (String groupId : grouplist) {
//                                callNotification(Integer.parseInt(groupId), Appreference.loginuserdetails.getId());
//                                Appreference.broadcast_call = true;
//                            }
                            ArrayList<Integer> group_list_id = new ArrayList<Integer>();
                            for (String groupId : grouplist) {
                                group_list_id.add(Integer.parseInt(groupId));
                            }
                            if(group_list_id.size() > 0) {
                                callNotification(group_list_id, Appreference.loginuserdetails.getId());
                                Appreference.broadcast_call = true;
                            }
                        }
                    }
                } else {
                    Toast.makeText(getContext(), "Select user to make a call", Toast.LENGTH_LONG).show();
                }
            }
        } else {

        }
    }

    public void broadcastaudiocall() {
        Log.i("ContactFragment", "Broadcast Audio Call 1 if click");
        if (MainActivity.gsmCallState == TelephonyManager.CALL_STATE_IDLE) {
            boolean select = false;
            boolean groupIsSelect = false;
            Log.i("audiocall", "call button click");
            groupIsSelect = true;
            Log.i("ContactFragment", "group broadcast_audio_call click groupIsSelect " + groupIsSelect);
            if (groupIsSelect) {
                Log.i("ContactFragment", "Inside if");
                if (ExpListItems.size() > 0) {
                    check = false;
                    dialog = new ProgressDialog(classContext);
                    dialog.setMessage("Call Connecting...");
                    dialog.setCancelable(false);
                    dialog.show();
                    for (Group group : ExpListItems) {
                        Log.i("ContactFragment", "inside for");
                        Log.i("ContactFragment", String.valueOf(group.getIscheck()));
                        if (group.getIscheck()) {
                            Log.i("ContactFragment", "inside group if");
                            MainActivity.isAudioCall = true;
                            ArrayList<String> grouplist = VideoCallDataBase.getDB(context).selectGroupmembers("select * from groupmember where groupid= '" + group.getId() + "'", "userid");
                            /*for (String groupId : grouplist) {
                                Log.i("ContactFragment", groupId);
                                Log.i("ContactFragment", String.valueOf(Appreference.loginuserdetails.getId()));
                                callNotification(Integer.parseInt(groupId), Appreference.loginuserdetails.getId());
                                Appreference.broadcast_call = true;
                            }*/
                            ArrayList<Integer> group_list_id = new ArrayList<Integer>();
                            for (String groupId : grouplist) {
                                group_list_id.add(Integer.parseInt(groupId));
                            }
                            if(group_list_id.size() > 0) {
                                callNotification(group_list_id, Appreference.loginuserdetails.getId());
                                Appreference.broadcast_call = false;
                            }
                        }
                    }
                } else {
                    Toast.makeText(getContext(), "Select user to make a call", Toast.LENGTH_LONG).show();
                }

            }
        }
    }

    public void conferencecall() {
        {
            Log.i("ContactFragment", "Conference Call 1 if click");
            if (MainActivity.gsmCallState == TelephonyManager.CALL_STATE_IDLE) {
//                        boolean select = false;
                boolean groupIsSelect = false;
                Log.i("audiocall", "call button click");
//                            for (Group group : ExpListItems) {
//                                if (group.getIscheck()) {
                groupIsSelect = true;
//                                    Log.i("ContactFragment","group conference_call click groupIsSelect " +groupIsSelect);
//                                }
//                            }
                if (groupIsSelect) {
                    Log.i("ContactFragment", "Conference Call 2 if click");
                    if (ExpListItems.size() > 0) {
                        Log.i("ContactFragment", "Conference Call 3 if click");
                        check = false;
                        dialog = new ProgressDialog(classContext);
                        dialog.setMessage("Call Connecting...");
                        dialog.setCancelable(false);
                        dialog.show();
                        for (Group group : ExpListItems) {
                            if (group.getIscheck()) {
                                Log.i("ContactFragment", "Conference Call 4 if click");
                                MainActivity.isAudioCall = true;
                                ArrayList<String> grouplist = VideoCallDataBase.getDB(context).selectGroupmembers("select * from groupmember where groupid= '" + group.getId() + "'", "userid");
//                                for (String groupId : grouplist) {
//                                    Log.i("ContactFragment", "Conference Call 5 if click");
//                                    callNotification(Integer.parseInt(groupId), Appreference.loginuserdetails.getId());
//                                    Appreference.broadcast_call = false;

//                                }
                                ArrayList<Integer> group_list_id = new ArrayList<Integer>();
                                for (String groupId : grouplist) {
                                    group_list_id.add(Integer.parseInt(groupId));
                                }
                                if(group_list_id.size() > 0) {
                                    callNotification(group_list_id, Appreference.loginuserdetails.getId());
                                    Appreference.broadcast_call = false;
                                }
                            }
                        }
                    } else {
                        Toast.makeText(getContext(), "Select user to make a call", Toast.LENGTH_LONG).show();
                    }
                } else {

                }
            }
        }
    }

    private void showprogress() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.i("expand", "inside show progress--------->");

                    progress = new ProgressDialog(context);
                    progress.setCancelable(false);
                    progress.setMessage("Restrictions");
                    progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progress.setProgress(0);
                    progress.setMax(1000);
                    progress.show();
                } catch (Exception e) {
                }
            }


        });
    }

    public void hideCallIcon() {
        audio_call.setVisibility(View.INVISIBLE);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d("CallUI", "MainActivity hideCallIcon contacts ");
                audio_call.setVisibility(View.VISIBLE);
            }
        }, 3000);
    }

    public String getNegativeOrPositiveValue(int number) {
        if (number < 0) {
            return "negative";
        } else {
            return "positive";
        }
    }

}