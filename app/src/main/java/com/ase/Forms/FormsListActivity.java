package com.ase.Forms;

import android.app.Activity;
import android.app.ProgressDialog;
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
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ase.Appreference;
import com.ase.Bean.TaskDetailsBean;
import com.ase.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import json.CommunicationBean;
import json.EnumJsonWebservicename;
import json.WebServiceInterface;

/**
 * Created by saravanakumar on 12/16/2016.
 */
public class FormsListActivity extends Activity implements WebServiceInterface {
    SwipeMenuListView formsList;
    FormsListAdapter formsListAdapter;
    Context context;
    ArrayList<FormsListBean> listBeanArrayList;
    String taskId, taskName, taskNo, webformcheck, isTemplate;
    Handler handler = new Handler();
    String formId;
    TextView back, add_forms, NoData;
    ArrayList<String> stringArrayList;
    ProgressDialog progress;
    static FormsListActivity formsListActivity;
    TaskDetailsBean taskDetailsBean;
    Boolean isformview = false;

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    public static FormsListActivity getInstance() {
        return formsListActivity;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forms_list_activity);
        try {
            progress = new ProgressDialog(this);
            context = this;
            formsList = (SwipeMenuListView) findViewById(R.id.forms_list);
            back = (TextView) findViewById(R.id.back);
            add_forms = (TextView) findViewById(R.id.add_forms);
            NoData = (TextView) findViewById(R.id.NoData);
            listBeanArrayList = new ArrayList<>();

       /* FormsListBean formsListBean = new FormsListBean();
        formsListBean.setFormName("Form Name 1");
        formsListBean.setFormDescription("first Form");
        listBeanArrayList.add(formsListBean);

        FormsListBean formsListBean2 = new FormsListBean();
        formsListBean2.setFormName("Form Name 2");
        formsListBean2.setFormDescription("Second Form");
        listBeanArrayList.add(formsListBean2);*/

//        listBeanArrayList = (ArrayList<FormsListBean>) getIntent().getSerializableExtra("FormsList");
            if (getIntent() != null) {
                isTemplate = getIntent().getExtras().getString("isTemplate");
            }
            Log.i("TemplateList", "template_form.setOnClickListener 2 " + isTemplate);
            taskId = getIntent().getExtras().getString("TaskId");
           /* taskNo = getIntent().getExtras().getString("TaskNo");*/
            taskName = getIntent().getExtras().getString("TaskName");
            stringArrayList = getIntent().getStringArrayListExtra("UserList");
            webformcheck = getIntent().getExtras().getString("webformcheck");
            taskDetailsBean = (TaskDetailsBean) getIntent().getExtras().getSerializable("TaskBean");
            taskNo=taskDetailsBean.getTaskNo().toString();
            if (taskDetailsBean != null && taskDetailsBean.getOwnerOfTask() != null && taskDetailsBean.getOwnerOfTask().equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                add_forms.setVisibility(View.VISIBLE);
            } else {
                add_forms.setVisibility(View.GONE);
            }

            formsList.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                    FormsListBean formsListBean = listBeanArrayList.get(position);
                    formId = String.valueOf(formsListBean.getFormId());
                    if (menu.getMenuItem(index).getTitle().equalsIgnoreCase("Access")) {

                        Intent intent = new Intent(getApplicationContext(), FormAccessControl.class);
                        intent.putExtra("taskid", taskId);
                        intent.putExtra("formid", formId);
                        intent.putExtra("formname", formsListBean.getFormName());
                        intent.putExtra("touserid", taskDetailsBean.getToUserId());

                        startActivity(intent);
                    }
                    return false;
                }
            });
            final SwipeMenuCreator creator = new SwipeMenuCreator() {

                @Override
                public void create(SwipeMenu menu) {

                    switch (menu.getViewType()) {

                        case 1:
                            SwipeMenuItem deleteItem1 = new SwipeMenuItem(
                                    getApplicationContext());
                            // set item background
                            deleteItem1.setBackground(new ColorDrawable(Color.rgb(0x00, 0x00, 0xFf)));
                            // set item width
                            deleteItem1.setWidth(dp2px(90));
                            // set a icon
                            deleteItem1.setTitle("Access");
                            deleteItem1.setTitleSize(18);
                            deleteItem1.setTitleColor(Color.WHITE);
    //            deleteItem.setIcon(R.drawable.ic_delete_32);
                            // add to menu
                            menu.addMenuItem(deleteItem1);
                            break;


                    }
                }
            };
            formsList.setMenuCreator(creator);
            formsList.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);



       /* formsListAdapter = new FormsListAdapter(context, R.layout.forms_list_adapter, listBeanArrayList);
        formsList.setAdapter(formsListAdapter);
        formsListAdapter.notifyDataSetChanged();
*/

            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });


            formsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    FormsListBean formsListBean = listBeanArrayList.get(position);

                    formId = String.valueOf(formsListBean.getFormId());
                    /*List<NameValuePair> tagNameValuePairs = new ArrayList<NameValuePair>();
                    tagNameValuePairs.add(new BasicNameValuePair("formId", "1"));
                    tagNameValuePairs.add(new BasicNameValuePair("taskId", "16148"));

                    Appreference.jsonRequestSender.getFormFieldAndValues(EnumJsonWebservicename.getFormFieldAndValues, tagNameValuePairs, FormsListActivity.this);
    */

                    Intent intent = new Intent(FormsListActivity.this, FormEntryViewActivity.class);
                    //                intent.putExtra("FormsMapValue", formFieldsBeenMap);
                    //                intent.putExtra("FormsListValue", setIdValueList);
                    intent.putExtra("FormsId", formId);
                    intent.putExtra("TaskId", taskId);
                    intent.putExtra("TaskNo", taskNo);
                    intent.putExtra("TaskName", taskName);
                    intent.putExtra("isTemplate", isTemplate);
                    Log.i("TemplateList", "template_form.setOnClickListener 21 23  " + isTemplate);
                    intent.putExtra("EntryMode", formsListBean.getEntryMode());
                    intent.putExtra("UserList", stringArrayList);
                    intent.putExtra("TaskBean", taskDetailsBean);
                    startActivity(intent);
    //                finish();

                }
            });

            add_forms.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("FormlistActivity", "add_forms.setOnClickListener 1 " + taskId);
                    Intent intent = new Intent(FormsListActivity.this, AddFormsInWebActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("taskId", taskId);
                    startActivity(intent);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        java.util.List<NameValuePair> tagNameValuePairs = new ArrayList<NameValuePair>();
        tagNameValuePairs.add(new BasicNameValuePair("taskId", taskId));
        Log.i("FormlistActivity", "onresume 1 " + taskId);
        Appreference.jsonRequestSender.getlistFormsforTask(EnumJsonWebservicename.getAllFormsForTask, tagNameValuePairs, FormsListActivity.this);
        webformcheck = getIntent().getExtras().getString("webformcheck");
        progress = new ProgressDialog(this);
        if (webformcheck != null && webformcheck.equalsIgnoreCase("false")) {
            progress.setMessage("Loading...");
            progress.setIndeterminate(false);
            progress.show();
        } else {
            Toast.makeText(FormsListActivity.this, "list of Forms", Toast.LENGTH_LONG)
                    .show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void ResponceMethod(final Object object) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                Log.i("currentstatus", "Task Details Respons method");
                Log.i("FormlistActivity", "ResponceMethod 1 " + object);
                CommunicationBean communicationBean = (CommunicationBean) object;
                JSONObject jsonObject = null;
                String s1 = communicationBean.getEmail();
                String s2 = communicationBean.getFirstname();
                Log.i("FormlistActivity", "ResponceMethod 2 " + s1);
                Log.i("FormlistActivity", "ResponceMethod 3 " + s2);
                if (s1 != null && s1.contains("entryMode")) {
                    {
                        NoData.setVisibility(View.GONE);
                        Log.i("FormlistActivity", "ResponceMethod 4 " + s2);
                        try {
                            ArrayList<FormsListBean> formsListBeen = new ArrayList<FormsListBean>();
                            listBeanArrayList.clear();
                            JSONArray jsonArray = new JSONArray(s1);
                            for (int i = 0; i < jsonArray.length(); i++) {

                                Type collectionType = new TypeToken<List<FormsListBean>>() {
                                }.getType();

                                Log.i("response ", "objects  " + jsonArray.get(i));
                                Log.i("response ", "objects  " + jsonArray.getString(i));
                                FormsListBean listUserGroupObject = new Gson().fromJson(jsonArray.getString(i), FormsListBean.class);
//                                listUserGroupObject.setClientId(String.valueOf(i+1));
                                formsListBeen.add(listUserGroupObject);
                                Log.i("Forms", "formlist Value123 " + "\n" + listUserGroupObject.getFormId() + "\n" + listUserGroupObject.getFormName() + "\n" + listUserGroupObject.getFormDescription());
                                listBeanArrayList = formsListBeen;
                                Log.i("FormlistActivity", "ResponceMethod 5 " + listBeanArrayList);
                            }
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    formsListAdapter = new FormsListAdapter(context, R.layout.forms_list_adapter, listBeanArrayList);
                                    formsList.setAdapter(formsListAdapter);
                                    formsListAdapter.notifyDataSetChanged();
                                    cancelDialog();
                                }
                            });
                            Log.i("FormlistActivity", "ResponceMethod 6 " + listBeanArrayList);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else if (s1 != null && s1.contains("[]") && s2.equalsIgnoreCase("getAllFormsForTask")) {
//                    showToast("No forms for this task");
                    Log.d("My Profile", "query if ");
                    Log.i("FormlistActivity", "ResponceMethod 7 ");
                    cancelDialog();
                    NoData.setVisibility(View.VISIBLE);

                } else {
                    Log.i("FormlistActivity", "ResponceMethod 8 ");
                    ArrayList<String> setIdValueList = new ArrayList<String>();
                    HashMap<String, ArrayList<com.ase.Forms.List>> formFieldsBeenMap = new HashMap<String, ArrayList<com.ase.Forms.List>>();
                    try {
                        Log.i("FormlistActivity", "ResponceMethod 9 ");
                        JSONArray jr = new JSONArray(s1);
                        if (jr.length() > 0) {

                            Type collectionType = new TypeToken<List<FormEntryValueBean>>() {
                            }.getType();
                            List<FormEntryValueBean> lcs = new Gson().fromJson(s1, collectionType);
                            for (int i = 0; i < lcs.size(); i++) {
                                FormEntryValueBean groubDetails = lcs.get(i);

                                setIdValueList.add(String.valueOf(groubDetails.getSetId()));
                                formFieldsBeenMap.put(String.valueOf(groubDetails.getSetId()), (ArrayList<com.ase.Forms.List>) groubDetails.getList());
                                Log.i("FormlistActivity", "ResponceMethod 10 " + listBeanArrayList);
                            }
                            if (setIdValueList.size() > 0) {
                                Log.i("FormlistActivity", "ResponceMethod 11 " + listBeanArrayList);
                                Intent intent = new Intent(FormsListActivity.this, FormEntryViewActivity.class);
                                intent.putExtra("FormsMapValue", formFieldsBeenMap);
                                intent.putExtra("FormsListValue", setIdValueList);
                                intent.putExtra("FormsId", formId);
                                intent.putExtra("TaskId", taskId);
                                intent.putExtra("TaskNo", taskNo);
                                intent.putExtra("TaskName", taskName);
                                intent.putExtra("UserList", stringArrayList);
                                startActivity(intent);

                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public void ErrorMethod(Object object) {
        cancelDialog();
        showToast("Forms List error. Try again later ");
    }

    public void cancelDialog() {
        try {
            if (progress != null && progress.isShowing()) {
                Log.i("register", "--progress bar end-----");
                progress.dismiss();
                Appreference.isRequested_date = false;
                progress = null;
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void showToast(final String msg) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(FormsListActivity.this, msg, Toast.LENGTH_LONG)
                        .show();
            }
        });
    }


    public class FormsListAdapter extends ArrayAdapter<FormsListBean> {

        Context context;
        ArrayList<FormsListBean> formsListBeen;

        public FormsListAdapter(Context context, int resource, ArrayList<FormsListBean> formsListBeen) {
            super(context, resource, formsListBeen);

            this.context = context;
            this.formsListBeen = formsListBeen;
        }

        @Override
        public long getItemId(int position) {

            return position;
        }

        public int getItemViewType(int position) {


            int value = 0;
            try {
                if (taskDetailsBean.getOwnerOfTask().equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                    value = 1;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return value;
        }


        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                LayoutInflater layoutInflater = ((Activity) context).getLayoutInflater();
                convertView = layoutInflater.inflate(R.layout.forms_list_adapter, parent, false);
            }

            if (formsListBeen != null) {
                FormsListBean formsListBean = formsListBeen.get(position);
                Log.d("Forms", "size     ===     " + formsListBeen.size());
                Log.d("Forms", "name     ===     " + formsListBean.getFormName() + " ==   " + formsListBean.getFormDescription());

                TextView forms_Name = (TextView) convertView.findViewById(R.id.tv_forms_name);
                TextView forms_Description = (TextView) convertView.findViewById(R.id.tv_forms_description);

                forms_Name.setText(formsListBean.getFormName());
                forms_Description.setText(formsListBean.getFormDescription());
                Log.i("FormlistActivity", "getView 1 ");
            }

            return convertView;
        }
    }

}
