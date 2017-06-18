package com.myapplication3;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.myapplication3.Bean.ListTaskTransaction;
import com.myapplication3.Bean.ListallProjectBean;
import com.myapplication3.Bean.ProjectDetailsBean;
import com.myapplication3.RandomNumber.Utility;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import json.CommunicationBean;
import json.EnumJsonWebservicename;
import json.WebServiceInterface;

/**
 * Created by Preethi on 18-06-2017.
 */

public class MediaSearch extends Activity implements WebServiceInterface {

    private LinearLayout all_media_Search;
    private LinearLayout media_input;
    private Button Search_image;
    private TextView back;
    private ListView listView;
    private EditText user_id;
    private EditText job_id;
    private EditText machine_model;
    private EditText machine_make;
    private EditText machine_number;
    int count;
    ProgressDialog dialog;
    static MediaSearch mediaSearch;
    private Handler handler;
    private MediaListAdapter mediaListAdapter;



    public static MediaSearch getInstance() {
        return mediaSearch;
    }


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_search);
        mediaSearch = this;
        handler = new Handler();
        all_media_Search = (LinearLayout) findViewById(R.id.all_media_Search);
        media_input = (LinearLayout) findViewById(R.id.media_input);
        Search_image = (Button) findViewById(R.id.Search_image);
        back = (TextView) findViewById(R.id.back);
        listView = (ListView) findViewById(R.id.media_listview);
        user_id = (EditText) findViewById(R.id.user_id);
        job_id = (EditText) findViewById(R.id.job_id);
        machine_model = (EditText) findViewById(R.id.machine_model);
        machine_make = (EditText) findViewById(R.id.machine_make);
        machine_number = (EditText) findViewById(R.id.machine_number);

        media_input.setVisibility(View.GONE);

        all_media_Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count == 0) {
                    media_input.setVisibility(View.VISIBLE);
                    count = 1;
                } else {
                    media_input.setVisibility(View.GONE);
                    count = 0;
                }
            }

        });
        Search_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                media_input.setVisibility(View.GONE);
                sendMedia_webservice();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });
    }

    private void sendMedia_webservice() {
        showDialog();
        media_input.setVisibility(View.GONE);

        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        if (user_id != null)
            nameValuePairs.add(new BasicNameValuePair("userId", user_id.getText().toString()));
        if (job_id != null)
            nameValuePairs.add(new BasicNameValuePair("oracleProjectId", job_id.getText().toString()));
        if (machine_model != null)
            nameValuePairs.add(new BasicNameValuePair("mcModel", machine_model.getText().toString()));
        if (machine_make != null)
            nameValuePairs.add(new BasicNameValuePair("machineMake", machine_make.getText().toString()));
        if (machine_number != null)
            nameValuePairs.add(new BasicNameValuePair("mcSrNo", machine_number.getText().toString()));
        Appreference.jsonRequestSender.getMediaSearch(EnumJsonWebservicename.searchMedia, nameValuePairs, MediaSearch.this);
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
            Appreference.printLog("AddTaskReassign cancelDialog ", "Exception " + e.getMessage(), "WARN", null);
        }
    }

    public void showDialog() {
        try {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    dialog = new ProgressDialog(MediaSearch.this);
                    dialog.setMessage("Media searching....");
                    dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    dialog.setProgress(0);
                    dialog.setMax(100);
                    dialog.show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("MediaSearch ShowDialog ", "Exception " + e.getMessage(), "WARN", null);
        }

    }

    public void showToast(final String result) {
        try {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("MediaSearch showToast ", "Exception " + e.getMessage(), "WARN", null);
        }

    }

    @Override
    public void ResponceMethod(final Object object) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                CommunicationBean communicationBean = (CommunicationBean) object;
                cancelDialog();
                try {
                    String sig_id = Utility.getSessionID();
                    String server_Response_string = communicationBean.getEmail();
                    Log.d("media123", "Response Email" + server_Response_string);
                    String WebServiceEnum_Response = communicationBean.getFirstname();
                    Log.d("media123", "name   == " + WebServiceEnum_Response);
                    if (WebServiceEnum_Response != null && WebServiceEnum_Response.equalsIgnoreCase("searchMedia")) {
                        Log.i("media123", "searchMedia responceReceived");
                        boolean Success = true;
                        try {
                            JSONArray jr = new JSONArray(server_Response_string);
                            if (jr.length() > 0) {
                                JSONObject jb = jr.getJSONObject(0);
                                if (jb.has("listallProject")) {
                                    Type collectionType = new TypeToken<List<ListallProjectBean>>() {
                                    }.getType();
                                    List<ListallProjectBean> pdb = new Gson().fromJson(server_Response_string, collectionType);
                                    getAllMediaForUser(pdb);
                                }
                            }
                        } catch (JSONException e2) {
                            e2.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("media123", "sip responce  a jsonobject Exception*******" + e);
                }
            }
        });
    }


    private void getAllMediaForUser(List<ListallProjectBean> pdb) {
        Log.i("media123", "Media_listgetAllMediaForUser");

        ArrayList<ListTaskTransaction> media_list = new ArrayList<>();
        for (int i = 0; i < pdb.size(); i++) {
            ListallProjectBean listallProjectBean = pdb.get(i);
            listallProjectBean.getListallProject().size();
            for (int j = 0; j < listallProjectBean.getListallProject().size(); j++) {
                ProjectDetailsBean projectDetailsBean = listallProjectBean.getListallProject().get(j);
                ArrayList<ListAllgetTaskDetails> listAllgetTaskDetailses;
                listAllgetTaskDetailses = projectDetailsBean.getListSubTask();

                if (listAllgetTaskDetailses.size() > 0) {
                    for (int j1 = 0; j1 < listAllgetTaskDetailses.size(); j1++) {
                        ListAllgetTaskDetails listAllgetTaskDetails = listAllgetTaskDetailses.get(j1);
                        ArrayList<ListTaskTransaction> listAlltaskTransactions;
                        listAlltaskTransactions = listAllgetTaskDetails.getListTaskTransaction();
                        if (listAlltaskTransactions.size() > 0) {
                            for (int k = 0; k < listAlltaskTransactions.size(); k++) {
                                ListTaskTransaction listTaskTransaction = listAlltaskTransactions.get(k);
                              /*  if (listTaskTransaction.getSignature() != null && listTaskTransaction.getSignature().length() > 0
                                        || listTaskTransaction.getPhoto() != null && listTaskTransaction.getPhoto().length() > 0
                                        || listTaskTransaction.getTechnicianSignature() != null && listTaskTransaction.getTechnicianSignature().length() > 0) {
                                    media_list.add(listTaskTransaction);
                                }*/
                                media_list.add(listTaskTransaction);
                            }
                        }
                    }
                }
            }
        }

        Log.i("media123", "Media_list bean size" + +media_list.size());
        mediaListAdapter = new MediaListAdapter(this, media_list);
        listView.setAdapter(mediaListAdapter);
        handler.post(new Runnable() {
            @Override
            public void run() {
                mediaListAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void ErrorMethod(Object object) {

    }
    public class MediaListAdapter extends ArrayAdapter<ListTaskTransaction> {

        ArrayList<ListTaskTransaction> arrayMediaList;
        LayoutInflater inflater = null;
        Context adapContext;
        ImageLoader imageLoader1;


        public MediaListAdapter(Context context, ArrayList<ListTaskTransaction> mediaList) {

            super(context, R.layout.media_list_row, mediaList);
            arrayMediaList = mediaList;
            adapContext = context;
            imageLoader1 = new ImageLoader(adapContext);
        }
        @Override
        public int getCount() {
            return arrayMediaList.size();
        }


        @Override
        public long getItemId(int position) {

            return position;
        }

        public int getItemViewType(int position) {
            int value = 0;

            return value;
        }
        @Override
        public View getView(final int position, View view, ViewGroup arg2) {
            View row = view;

            try {
                final ViewHolder holder;
                if (row == null) {
                    holder = new ViewHolder();
                    LayoutInflater inflater = (LayoutInflater) adapContext
                            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    row = inflater.inflate(R.layout.media_list_row, null, false);
                    holder.taskId = (TextView) row.findViewById(R.id.fromvalue);
                    holder.createdDate = (TextView) row.findViewById(R.id.tovalue);
                    holder.filetype = (TextView) row.findViewById(R.id.filetype);
                    holder.fileresource = (TextView) row.findViewById(R.id.fileresource);
                    holder.image = (ImageView) row.findViewById(R.id.image);
                    row.setTag(holder);
                } else {
                    holder = (ViewHolder) row.getTag();
                }
                final ListTaskTransaction pBean = (ListTaskTransaction) arrayMediaList.get(position);
                holder.taskId.setText(pBean.getCreatedDate());
                holder.createdDate.setText(pBean.getCreatedDate());
                holder.fileresource.setText(pBean.getCreatedDate());
                holder.filetype.setText("image");
                holder.image.setBackgroundResource(R.drawable.unknown);



               /* File tech_sign = new File(pBean.getTechnicianSignature());
                if (tech_sign.exists()) {
                    Picasso.with(adapContext).load(tech_sign).into(holder.image);
                } else {
                    holder.image.setImageResource(R.drawable.unknown);
                }*/

             /*   File cust_sign = new File(pBean.getSignature());
                if (cust_sign.exists()) {
                    Picasso.with(adapContext).load(cust_sign).into(holder.image);
                } else {
                    holder.image.setImageResource(R.drawable.unknown);
                }

                File photo_sign = new File(pBean.getSignature());
                if (photo_sign.exists()) {
                    Picasso.with(adapContext).load(photo_sign).into(holder.image);
                } else {
                    holder.image.setImageResource(R.drawable.unknown);
                }*/
            } catch (Exception e) {
                e.printStackTrace();
            }
            return row;
        }

        private class ViewHolder {
            TextView taskId, createdDate, filetype, fileresource;
            ImageView image;
        }
    }
}