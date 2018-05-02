package com.ase;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ase.Bean.Label;
import com.ase.Bean.checkListDetails;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
    TextView custName, custAddress, checklist_date, machine_serial, machine_model, checklist_back,checklist_date_now;
    String strIPath;
    boolean isCommentsImg, isAdviceImg, isTechnicianSign, isCustomerSign;
    String comments_path, advice_path, techSign_path, custSign_path;
    ImageView checklist_commands_img, checklist_advice_img, checklist_tech_sign_img, checklist_cust_sign_img;
    Button checklist_tech_signature, checklist_cust_signature,checklist_date_btn;

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


        stickyView.setText(checklistBean.getCheckListName());

       /* custName.setText(checklistBean.getCustomer());
        custAddress.setText(checklistBean.getCustomer());
        checklist_date.setText(checklistBean.getDate());
        machine_serial.setText(checklistBean.getSerialNumber());
        machine_model.setText(checklistBean.getModel());*/

        custName.setText("Bin Touq Transport");
        custAddress.setText("+971 562107436");
        checklist_date.setText("30-Apr-18");
        machine_serial.setText("NDM454640");
        machine_model.setText("SR200");


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
        final Button checklist_commands = (Button) footerView.findViewById(R.id.checklist_commands);
        final Button checklist_advice = (Button) footerView.findViewById(R.id.checklist_advice);
        checklist_tech_signature = (Button) footerView.findViewById(R.id.checklist_tech_sign);
        checklist_cust_signature = (Button) footerView.findViewById(R.id.checklist_cust_sign);
        final EditText checklist_commands_text = (EditText) footerView.findViewById(R.id.checklist_cmds_text);
        checklist_commands_img = (ImageView) footerView.findViewById(R.id.checklist_cmds_img);
        final EditText checklist_advice_text = (EditText) footerView.findViewById(R.id.checklist_advice_text);
        checklist_advice_img = (ImageView) footerView.findViewById(R.id.checklist_advice_img);
        checklist_cust_sign_img = (ImageView) footerView.findViewById(R.id.checklist_cust_img);
        checklist_tech_sign_img = (ImageView) footerView.findViewById(R.id.checklist_tech_img);
        checklist_date_btn = (Button) footerView.findViewById(R.id.checklist_date_btn);
        checklist_date_now=(TextView)footerView.findViewById(R.id.checklist_date_now);
        checklist_commands.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder saveDialog = new AlertDialog.Builder(checkListContext);
                saveDialog.setTitle("Comments");
                saveDialog.setCancelable(false);
                saveDialog.setMessage("You want to type or draw via sketch " + checklistBean.getCheckListName());
                saveDialog.setPositiveButton("Text", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        checklist_commands_text.setVisibility(View.VISIBLE);
                        checklist_commands_img.setVisibility(View.GONE);
                        isCommentsImg = false;
                        dialog.cancel();
                    }
                });
                saveDialog.setNeutralButton("Sketch",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    checklist_commands_text.setVisibility(View.GONE);
                                    isCommentsImg = true;
                                    isAdviceImg = false;
                                    isTechnicianSign = false;
                                    isCustomerSign = false;
                                    Intent i = new Intent(getApplicationContext(), HandSketchActivity2.class);
                                    i.putExtra("isFromcheckList", true);
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

        checklist_advice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder saveDialog = new AlertDialog.Builder(checkListContext);
                saveDialog.setTitle("Advice to the Customer");
                saveDialog.setCancelable(false);
                saveDialog.setMessage("You want to type or draw via sketch " + checklistBean.getCheckListName());
                saveDialog.setPositiveButton("Text", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        checklist_advice_text.setVisibility(View.VISIBLE);
                        checklist_advice_img.setVisibility(View.GONE);
                        isAdviceImg = false;
                        dialog.cancel();
                    }
                });
                saveDialog.setNeutralButton("Sketch",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    checklist_advice_text.setVisibility(View.GONE);
                                    isAdviceImg = true;
                                    isCommentsImg = false;
                                    isTechnicianSign = false;
                                    isCustomerSign = false;
                                    Intent i = new Intent(getApplicationContext(), HandSketchActivity2.class);
                                    i.putExtra("isFromcheckList", true);
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
                    isTechnicianSign = true;
                    isAdviceImg = false;
                    isCommentsImg = false;
                    isCustomerSign = false;
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
                    isCustomerSign = true;
                    isAdviceImg = false;
                    isCommentsImg = false;
                    isTechnicianSign = false;
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

        checklist_commands_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String ImageName = comments_path;
                    File file = null;
                    if (ImageName != null && !ImageName.equalsIgnoreCase("")) {
                        file = new File(ImageName);
                        if (file.exists()) {
                           /* Intent i = new Intent(CheckListActivity.this, FullScreenViewActivity.class);
                            i.putExtra("position", 0);
                            i.putExtra("pathSketch", ImageName);
                            startActivity(i);*/
                            Intent intent = new Intent(checkListContext, FullScreenImage.class);
                            intent.putExtra("image", file.toString());
                            checkListContext.startActivity(intent);
                        } else {
                            File file1 = null;
                            file1 = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/High Message/" + ImageName);
                            if (file1.exists()) {
                                Intent intent = new Intent(checkListContext, FullScreenImage.class);
                                intent.putExtra("image", file1.toString());
                                checkListContext.startActivity(intent);
                            }
                        }
                    } else
                        Toast.makeText(CheckListActivity.this, "Please Set any Image to View", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Appreference.printLog("EodScreen", "action_taken_1 clicklistener Exception : " + e.getMessage(), "WARN", null);
                }
            }
        });
        checklist_advice_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String ImageName = advice_path;
                    File file = null;
                    if (ImageName != null && !ImageName.equalsIgnoreCase("")) {
                        file = new File(ImageName);
                        if (file.exists()) {
                            /*Intent i = new Intent(CheckListActivity.this, FullScreenViewActivity.class);
                            i.putExtra("position", 0);
                            i.putExtra("pathSketch", ImageName);
                            startActivity(i);*/
                            Intent intent = new Intent(checkListContext, FullScreenImage.class);
                            intent.putExtra("image", file.toString());
                            checkListContext.startActivity(intent);
                        } else {
                            File file1 = null;
                            file1 = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/High Message/" + ImageName);
                            if (file1.exists()) {
                                Intent intent = new Intent(checkListContext, FullScreenImage.class);
                                intent.putExtra("image", file1.toString());
                                checkListContext.startActivity(intent);
                            }
                        }
                    } else
                        Toast.makeText(CheckListActivity.this, "Please Set any Image to View", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Appreference.printLog("EodScreen", "action_taken_1 clicklistener Exception : " + e.getMessage(), "WARN", null);
                }
            }
        });
        checklist_tech_sign_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String ImageName = techSign_path;
                    File file = null;
                    if (ImageName != null && !ImageName.equalsIgnoreCase("")) {
                        file = new File(ImageName);
                        if (file.exists()) {
                           /* Intent i = new Intent(CheckListActivity.this, FullScreenViewActivity.class);
                            i.putExtra("position", 0);
                            i.putExtra("pathSketch", ImageName);
                            startActivity(i);*/
                            Intent intent = new Intent(checkListContext, FullScreenImage.class);
                            intent.putExtra("image", file.toString());
                            checkListContext.startActivity(intent);
                        } else {
                            File file1 = null;
                            file1 = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/High Message/" + ImageName);
                            if (file1.exists()) {
                                Intent intent = new Intent(checkListContext, FullScreenImage.class);
                                intent.putExtra("image", file1.toString());
                                checkListContext.startActivity(intent);
                            }
                        }
                    } else
                        Toast.makeText(CheckListActivity.this, "Please Set any Image to View", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Appreference.printLog("EodScreen", "action_taken_1 clicklistener Exception : " + e.getMessage(), "WARN", null);
                }
            }
        });
        checklist_cust_sign_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String ImageName = custSign_path;
                    File file = null;
                    if (ImageName != null && !ImageName.equalsIgnoreCase("")) {
                        file = new File(ImageName);
                        if (file.exists()) {
                           /* Intent i = new Intent(CheckListActivity.this, FullScreenViewActivity.class);
                            i.putExtra("position", 0);
                            i.putExtra("pathSketch", ImageName);
                            startActivity(i);*/
                            Intent intent = new Intent(checkListContext, FullScreenImage.class);
                            intent.putExtra("image", file.toString());
                            checkListContext.startActivity(intent);
                        } else {
                            File file1 = null;
                            file1 = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/High Message/" + ImageName);
                            if (file1.exists()) {
                                Intent intent = new Intent(checkListContext, FullScreenImage.class);
                                intent.putExtra("image", file1.toString());
                                checkListContext.startActivity(intent);
                            }
                        }
                    } else
                        Toast.makeText(CheckListActivity.this, "Please Set any Image to View", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Appreference.printLog("EodScreen", "action_taken_1 clicklistener Exception : " + e.getMessage(), "WARN", null);
                }
            }
        });
        checklist_date_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();

                DatePickerDialog dpd = new DatePickerDialog(checkListContext,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                try {
                                    String curr_date = null;
                                    try {
                                        SimpleDateFormat simpleDateFormat_1 = new SimpleDateFormat("yyyy-MM-dd");
                                        curr_date = simpleDateFormat_1.format(new Date());
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        Appreference.printLog("ProjectFragment", "activity_start curr_date Exception : " + e.getMessage(), "WARN", null);
                                    }
                                    String months = "";
                                    if ((monthOfYear + 1) < 10) {
                                        months = "0" + (monthOfYear + 1);
                                    } else {
                                        months = String.valueOf(monthOfYear + 1);
                                    }
                                    String days = "";
                                    if (dayOfMonth < 10) {
                                        days = "0" + dayOfMonth;
                                    } else {
                                        days = String.valueOf(dayOfMonth);
                                    }
                                    String start_date = year + "-" + months + "-" + days;
                                    Log.i("TNA", "start_date---> " + start_date);
                                    Log.i("TNA", "curr_date---> " + curr_date);
                                    if (curr_date != null && curr_date.compareTo(start_date) > 0) {
                                        Toast.makeText(checkListContext, "Kindly select valid date ", Toast.LENGTH_SHORT).show();
                                    } else {
                                        checklist_date_now.setText(start_date);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Appreference.printLog("ProjectFragment", "activity_start TNAReportStart  Exception : " + e.getMessage(), "WARN", null);
                                }
                            }
                        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE));
                dpd.show();
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

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        try {
            Log.d("task", "inside activity result page" + RESULT_OK + " resultCode :" + resultCode + " requestCode :" + requestCode);
            super.onActivityResult(requestCode, resultCode, data);
            if (resultCode == RESULT_OK) {
                if (requestCode == 423) {
                    if (data != null) {
                        try {
                            strIPath = data.getStringExtra("path");
                            Log.i("checklist123", "path of sketch====> " + strIPath);
                            if (isCommentsImg) {
                                comments_path = strIPath;
                                if (checklist_commands_img != null) {
                                    File imgFile = new File(comments_path);
                                    if (imgFile.exists()) {
                                        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                                        checklist_commands_img.setImageBitmap(myBitmap);
                                    }
                                    checklist_commands_img.setVisibility(View.VISIBLE);
                                }
                            } else if (isAdviceImg) {
                                advice_path = strIPath;
                                if (checklist_advice_img != null) {
                                    File imgFile = new File(advice_path);
                                    if (imgFile.exists()) {
                                        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                                        checklist_advice_img.setImageBitmap(myBitmap);
                                    }
                                    checklist_advice_img.setVisibility(View.VISIBLE);
                                }
                            } else if (isCustomerSign) {
                                custSign_path = strIPath;
                                if (checklist_cust_sign_img != null) {
                                    File imgFile = new File(custSign_path);
                                    if (imgFile.exists()) {
                                        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                                        checklist_cust_sign_img.setImageBitmap(myBitmap);
                                    }
                                    checklist_cust_sign_img.setVisibility(View.VISIBLE);
                                }
                            } else if (isTechnicianSign) {
                                techSign_path = strIPath;
                                if (checklist_tech_sign_img != null) {
                                    File imgFile = new File(techSign_path);
                                    if (imgFile.exists()) {
                                        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                                        checklist_tech_sign_img.setImageBitmap(myBitmap);
                                    }
                                    checklist_tech_sign_img.setVisibility(View.VISIBLE);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public class CheckListItemAdapter extends ArrayAdapter<Label> {

        List<Label> arrayCheckList;
        LayoutInflater inflater = null;
        Context adapContext;
        Label mylist;


        public CheckListItemAdapter(Context context, List<Label> CheckListDetails) {

            super(context, R.layout.checklist_row, CheckListDetails);
            arrayCheckList = CheckListDetails;
            adapContext = context;
            mylist=new Label();
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
                    holder.checklist_item = (TextView) row.findViewById(R.id.checklist_item);
                    holder.checklist_issue_type = (TextView) row.findViewById(R.id.checklist_issue);
                    row.setTag(holder);
                } else {
                    holder = (ViewHolder) row.getTag();
                }
                final Label pBean = (Label) arrayCheckList.get(position);
                holder.Description.setText(pBean.getJobDescription());
                holder.checklist_item.setText(pBean.getItem());
                holder.checklist_issue_type.setText(pBean.getIssueType());


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
            TextView checklist_issue_type;
            TextView checklist_item;
        }
    }
}

