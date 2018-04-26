package com.ase;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ase.Bean.Label;
import com.ase.Bean.checkListDetails;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Preethi on 4/25/2018.
 */

public class CheckListActivity extends Activity {
    checkListDetails checklistBean;
    private TextView stickyView;
    private ListView listView;
    private View heroImageView;
    LinearLayout checklist_master;
    Context checkListContext;
    CheckListItemAdapter adapter;
    private View stickyViewSpacer;
    TextView custName, custAddress, checklist_date, machine_serial, machine_model, HMR, checklist_back;

    private int MAX_ROWS = 50;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.servicelist);
        Intent intent = getIntent();
        checkListContext = this;
        checklistBean = (checkListDetails) intent.getSerializableExtra("checklistBean");
         /* Initialise list view, hero image, and sticky view */
        listView = (ListView) findViewById(R.id.listView);
//        heroImageView = findViewById(R.id.heroImageView);
        stickyView = (TextView) findViewById(R.id.stickyView);
        checklist_master = (LinearLayout) findViewById(R.id.checklist_master);

        custName = (TextView) findViewById(R.id.checklist_cust_name);
        custAddress = (TextView) findViewById(R.id.checklist_cust_address);
        checklist_date = (TextView) findViewById(R.id.checklist_date);
        machine_serial = (TextView) findViewById(R.id.checklist_machineNo);
        machine_model = (TextView) findViewById(R.id.checklist_machineMac);
        checklist_back = (TextView) findViewById(R.id.checklist_back);
        HMR = (TextView) findViewById(R.id.checklist_HMR);


        stickyView.setText(checklistBean.getCheckListName());

       /* custName.setText(checklistBean.getCustomer());
        custAddress.setText(checklistBean.getCustomer());
        checklist_date.setText(checklistBean.getDate());
        machine_serial.setText(checklistBean.getSerialNumber());
        machine_model.setText(checklistBean.getModel());
        HMR.setText(checklistBean.getHourMeter());*/

        custName.setText("Bin Touq Transport");
        custAddress.setText("+971 562107436");
        checklist_date.setText("30-Apr-18");
        machine_serial.setText("NDM454640");
        machine_model.setText("SR200");
        HMR.setText("4097");


        adapter = new CheckListItemAdapter(checkListContext, checklistBean.getLabel());

        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        /* Inflate list header layout */
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View listHeader = inflater.inflate(R.layout.checklist_header, null);
        View footerView = getLayoutInflater().inflate(R.layout.checklist_footer, null);

        stickyViewSpacer = listHeader.findViewById(R.id.stickyViewPlaceholder);

        /* Add list view header */
        listView.addHeaderView(listHeader);
        listView.addFooterView(footerView);
        final Button checklist_commands = (Button) footerView.findViewById(R.id.ohecklist_commands);
        final Button checklist_tech_signature = (Button) footerView.findViewById(R.id.ohecklist_tech_sign);
        final Button checklist_cust_signature = (Button) footerView.findViewById(R.id.ohecklist_cust_sign);
        final EditText checklist_commands_text = (EditText) footerView.findViewById(R.id.checklist_cmds_text);
        final ImageView checklist_commands_img = (ImageView) footerView.findViewById(R.id.checklist_cmds_img);


        checklist_commands.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder saveDialog = new AlertDialog.Builder(checkListContext);
                saveDialog.setTitle("Action Taken");
                saveDialog.setCancelable(false);
                saveDialog.setMessage("You want to type or draw via sketch " + checklistBean.getCheckListName());
                saveDialog.setPositiveButton("Text", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        checklist_commands_text.setVisibility(View.VISIBLE);
                        checklist_commands_img.setVisibility(View.GONE);
                        dialog.cancel();
                    }
                });
                saveDialog.setNeutralButton("Sketch",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    checklist_commands_text.setVisibility(View.GONE);
                                    checklist_commands_img.setVisibility(View.VISIBLE);
                                    Intent i = new Intent(getApplicationContext(), HandSketchActivity2.class);
                                    i.putExtra("isFromchecList", true);
                                    startActivityForResult(i, 423);
                                    dialog.cancel();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Appreference.printLog("checklist123", "action_taken_type Exception : " + e.getMessage(), "WARN", null);
                                }
                            }
                        });
                saveDialog.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                saveDialog.show();

            }
        });

        checklist_tech_signature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent i = new Intent(getApplicationContext(), HandSketchActivity2.class);
                    i.putExtra("isFromchecList", false);
                    startActivityForResult(i, 423);
                } catch (Resources.NotFoundException e) {
                    e.printStackTrace();
                    Appreference.printLog("checklist123", "skech_receiver clicklistener Exception : " + e.getMessage(), "WARN", null);
                } catch (Exception e) {
                    e.printStackTrace();
                    Appreference.printLog("checklist123", "skech_receiver clicklistener Exception : " + e.getMessage(), "WARN", null);
                }
            }
        });

        checklist_cust_signature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent i = new Intent(getApplicationContext(), HandSketchActivity2.class);
                    i.putExtra("isFromchecList", false);
                    startActivityForResult(i, 423);
                } catch (Resources.NotFoundException e) {
                    e.printStackTrace();
                    Appreference.printLog("checklist123", "skech_receiver clicklistener Exception : " + e.getMessage(), "WARN", null);
                } catch (Exception e) {
                    e.printStackTrace();
                    Appreference.printLog("checklist123", "skech_receiver clicklistener Exception : " + e.getMessage(), "WARN", null);
                }
            }
        });
        /* Handle list View scroll events */
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                /* Check if the first item is already reached to top.*/
                if (listView.getFirstVisiblePosition() == 0) {
                    View firstChild = listView.getChildAt(0);
                    int topY = 0;
                    if (firstChild != null) {
                        topY = firstChild.getTop();
                    }

                    int heroTopY = stickyViewSpacer.getTop();
                    stickyView.setY(Math.max(0, heroTopY + topY));

                    /* Set the image to scroll half of the amount that of ListView */
                    checklist_master.setY(topY * 0.5f);
                }
            }
        });
        checklist_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
/*
        *//* Populate the ListView with sample data *//*
        List<String> modelList = new ArrayList<>();
        for (int i = 0; i < MAX_ROWS; i++) {
            modelList.add("List item " + i);
        }

        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.checklist_row,R.id.desc_list, modelList);
        listView.setAdapter(adapter);*/

    }


    public class CheckListItemAdapter extends ArrayAdapter<Label> {

        List<Label> arrayCheckList;
        LayoutInflater inflater = null;
        Context adapContext;


        public CheckListItemAdapter(Context context, List<Label> CheckListDetails) {

            super(context, R.layout.checklist_row, CheckListDetails);
            arrayCheckList = CheckListDetails;
            adapContext = context;
        }

        @Override
        public int getCount() {
            return arrayCheckList.size();
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
                    row = inflater.inflate(R.layout.checklist_row, null, false);
                    holder.Description = (TextView) row.findViewById(R.id.desc_list);
                    holder.checklist_menu = (TextView) row.findViewById(R.id.checklist_popup);
                    row.setTag(holder);
                } else {
                    holder = (ViewHolder) row.getTag();
                }
                final Label pBean = (Label) arrayCheckList.get(position);
                holder.Description.setText(pBean.getJobDescription());
//                holder.checklist_menu.setText(String.valueOf(pBean.getJobstatus()));
                if (!holder.checklist_menu.getText().toString().equalsIgnoreCase("")) {
                    holder.checklist_menu.setClickable(false);
                } else {
                    holder.checklist_menu.setClickable(true);
                }
                holder.checklist_menu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PopupMenu popup = new PopupMenu(checkListContext, v);
                        MenuInflater inflater = popup.getMenuInflater();
                        inflater.inflate(R.menu.checklist_popmenu, popup.getMenu());
                        popup.show();
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                switch (item.getItemId()) {
                                    case R.id.checklist_ok:
                                        holder.checklist_menu.setText(item.getTitle().toString());
                                        return true;
                                    case R.id.checklist_NotOk:
                                        holder.checklist_menu.setText(item.getTitle().toString());
                                        return true;
                                    case R.id.checklist_NA:
                                        holder.checklist_menu.setText(item.getTitle().toString());
                                        return true;
                                    case R.id.checklist_cleaned:
                                        holder.checklist_menu.setText(item.getTitle().toString());
                                        return true;
                                    case R.id.checklist_lubricated:
                                        holder.checklist_menu.setText(item.getTitle().toString());
                                        return true;
                                    case R.id.checklist_adjusted:
                                        holder.checklist_menu.setText(item.getTitle().toString());
                                        return true;
                                    case R.id.checklist_replaced:
                                        holder.checklist_menu.setText(item.getTitle().toString());
                                        return true;
                                    case R.id.checklist_topUp:
                                        holder.checklist_menu.setText(item.getTitle().toString());
                                        return true;
                                    case R.id.checklist_repaired:
                                        holder.checklist_menu.setText(item.getTitle().toString());
                                        return true;
                                    case R.id.checklist_parts:
                                        holder.checklist_menu.setText(item.getTitle().toString());
                                        return true;
                                    default:
                                        return false;
                                }
                            }
                        });
                    }
                });


            } catch (Exception e) {
                e.printStackTrace();
            }
            return row;
        }

        private class ViewHolder {
            TextView Description;
            TextView checklist_menu;
        }
    }
}

