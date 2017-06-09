package com.myapplication3;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.myapplication3.Bean.TaskDetailsBean;
import com.myapplication3.DB.VideoCallDataBase;
import com.myapplication3.chat.ChatBean;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Ramdhas on 15-09-2016.
 */
public class FileListAdapter extends Activity {

    ListView fileListView;
    TextView back,title;
    Context context;
    ListofFileAdapter listofFileAdapter;
    ArrayList<TaskDetailsBean> arrayList = new ArrayList<TaskDetailsBean>();
    ArrayList<ChatBean> arrayList1 = new ArrayList<ChatBean>();
    Handler handler = new Handler();
    File directory;
    String dir_path = Environment.getExternalStorageDirectory() + "/High Message/downloads/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listoffiles);
        String query = null;
        context = this;
        fileListView = (ListView) findViewById(R.id.file_list);
        back=(TextView)findViewById(R.id.back);
        title=(TextView)findViewById(R.id.title);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        String file = getIntent().getExtras().getString("list");
        title.setText(file);
        Log.i("file", "type " + file);
        switch (file) {
            case "Chat File List":
                query = "select * from chat where  (username='" + Appreference.loginuserdetails.getUsername() + "') and (mimeType <>'text' ) ;";
                //query = "select * from chat where  (username='" + Appreference.loginuserdetails.getUsername() + "');";
                Log.i("file", query);
                arrayList1 = VideoCallDataBase.getDB(context).getChatHistoty(query);
                for(ChatBean chat :arrayList1)
                {
                    TaskDetailsBean bean=new TaskDetailsBean();
                    bean.setFromUserName(chat.getFromUser());
                    bean.setToUserName(chat.getToname());
                    bean.setTaskType(chat.getType());
                    bean.setMimeType(chat.getMsgtype());
                    bean.setTaskDescription(chat.getPath());
                    arrayList.add(bean);
                }
                if(arrayList.size()<=0)
                    Toast.makeText(context,"There is NO Task Files",Toast.LENGTH_SHORT).show();
                break;
            case "All File List":
                query = "select * from taskDetailsInfo where (loginuser='" + Appreference.loginuserdetails.getEmail() + "') and (mimeType <>'text');";
                arrayList = VideoCallDataBase.getDB(context).getTaskHistory(query);
                query = "select * from chat where  (username='" + Appreference.loginuserdetails.getUsername() + "') and (mimeType <>'text' ) ;";
                //query = "select * from chat where  (username='" + Appreference.loginuserdetails.getUsername() + "');";
                arrayList1 = VideoCallDataBase.getDB(context).getChatHistoty(query);
                for(ChatBean chat :arrayList1)
                {
                    TaskDetailsBean bean=new TaskDetailsBean();
                    bean.setFromUserName(chat.getFromUser());
                    bean.setToUserName(chat.getToname());
                    bean.setTaskType(chat.getType());
                    bean.setMimeType(chat.getMsgtype());
                    bean.setTaskDescription(chat.getPath());
                    arrayList.add(bean);
                }
                if(arrayList.size()<=0)
                    Toast.makeText(context,"There is NO Task Files",Toast.LENGTH_SHORT).show();
                break;
            case "Task File List":
                query = "select * from taskDetailsInfo where (loginuser='" + Appreference.loginuserdetails.getEmail() + "') and (mimeType <>'text' and mimeType <> 'date' );";
                arrayList = VideoCallDataBase.getDB(context).getTaskHistory(query);
                Log.i("file", "size " + arrayList.size());
                if(arrayList.size()<=0)
                    Toast.makeText(context,"There is NO Task Files",Toast.LENGTH_SHORT).show();
                break;
            case "Other File List":
                if(arrayList.size()<=0)
                    Toast.makeText(context,"There is NO Task Files",Toast.LENGTH_SHORT).show();
                break;
        }
        listofFileAdapter = new ListofFileAdapter(context, arrayList);
        fileListView.setAdapter(listofFileAdapter);
        handler.post(new Runnable() {
            @Override
            public void run() {
                listofFileAdapter.notifyDataSetChanged();
            }
        });


        fileListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TaskDetailsBean bean = arrayList.get(position);

                Log.e("entered", "entered............");
                switch (bean.getMimeType().toLowerCase().trim()) {
                    case "video":
                        Log.i("group123", "icon clicked video");
                        if (bean.getFromUserName().equalsIgnoreCase(Appreference.loginuserdetails.getUsername()))
                            directory = new File(bean.getTaskDescription());

                        else
                            directory = new File(dir_path + bean.getTaskDescription());

                        if (directory.exists()) {
                            Intent intent = new Intent(context, VideoPlayer.class);
                            intent.putExtra("video", directory.getAbsolutePath());
                            context.startActivity(intent);
                        } else
                            Toast.makeText(context, "File not available", Toast.LENGTH_SHORT).show();
                        break;
                    case "image":
                        Log.i("group123", "icon clicked image");
                        if (bean.getFromUserName().equalsIgnoreCase(Appreference.loginuserdetails.getUsername()))
                            directory = new File(bean.getTaskDescription());

                        else
                            directory = new File(dir_path + bean.getTaskDescription());
                        if (directory.exists()) {
                            Intent intent = new Intent(context, FullScreenImage.class);

                            intent.putExtra("image", directory.getAbsolutePath());
                            context.startActivity(intent);
                        } else
                            Toast.makeText(context, "File not available", Toast.LENGTH_SHORT).show();
                        break;
                    case "sketch":
                        Log.i("group123", "icon clicked image");
                        if (bean.getFromUserName().equalsIgnoreCase(Appreference.loginuserdetails.getUsername()))
                            directory = new File(bean.getTaskDescription());

                        else
                            directory = new File(dir_path + bean.getTaskDescription());
                        if (directory.exists()) {
                            Intent intentsketch = new Intent(context, FullScreenImage.class);
                            intentsketch.putExtra("image", directory.getAbsolutePath());
                            context.startActivity(intentsketch);
                        } else
                            Toast.makeText(context, "File not available", Toast.LENGTH_SHORT).show();
                        break;
                    case "audio":

                        if (bean.getFromUserName().equalsIgnoreCase(Appreference.loginuserdetails.getUsername()))
                            directory = new File(bean.getTaskDescription());

                        else
                            directory = new File(dir_path + bean.getTaskDescription());
                        if (directory.exists()) {
                            File url = new File(directory.getAbsolutePath());
                            Uri uri = Uri.fromFile(url);

                            Intent intentaudio = new Intent(Intent.ACTION_VIEW);
                            if (url.toString().contains(".wav") || url.toString().contains(".mp3")) {
                                // WAV audio file
                                Log.i("task","audio");
                                intentaudio.setDataAndType(uri, "audio/x-wav");
                                intentaudio.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                try {
                                    context.startActivity(intentaudio);
                                } catch (Exception e) {
                                    Log.e("error", "" + e);
                                }
                            }
                        } else
                            Toast.makeText(context, "File not available", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        if (bean.getFromUserName().equalsIgnoreCase(Appreference.loginuserdetails.getUsername()))
                            directory = new File(bean.getTaskDescription());

                        else
                            directory = new File(dir_path + bean.getTaskDescription());
                        if (directory.exists()) {
                            File url = new File(directory.getAbsolutePath());
                            Uri uri = Uri.fromFile(url);
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            if (url.toString().contains(".doc") || url.toString().contains(".docx")) {
                                // Word document
                                intent.setDataAndType(uri, "application/msword");
                            } else if (url.toString().contains(".pdf")) {
                                // PDF file
                                intent.setDataAndType(uri, "application/pdf");
                            } else if (url.toString().contains(".ppt") || url.toString().contains(".pptx")) {
                                // Powerpoint file
                                intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
                            } else if (url.toString().contains(".xls") || url.toString().contains(".xlsx")) {
                                // Excel file
                                intent.setDataAndType(uri, "application/vnd.ms-excel");
                            } else if (url.toString().contains(".zip") || url.toString().contains(".rar")) {
                                // WAV audio file
                                intent.setDataAndType(uri, "application/x-wav");
                            } else if (url.toString().contains(".rtf")) {
                                // RTF file
                                intent.setDataAndType(uri, "application/rtf");
                            } else if (url.toString().contains(".wav") || url.toString().contains(".mp3")) {
                                // WAV audio file
                                intent.setDataAndType(uri, "audio/x-wav");
                            } else if (url.toString().contains(".gif")) {
                                // GIF file
                                intent.setDataAndType(uri, "image/gif");
                            } else if (url.toString().contains(".jpg") || url.toString().contains(".jpeg") || url.toString().contains(".png")) {
                                // JPG file
                                intent.setDataAndType(uri, "image/jpeg");
                            } else if (url.toString().contains(".txt")) {
                                // Text file
                                intent.setDataAndType(uri, "text/plain");
                            } else if (url.toString().contains(".3gp") || url.toString().contains(".mpg") || url.toString().contains(".mpeg") || url.toString().contains(".mpe") || url.toString().contains(".mp4") || url.toString().contains(".avi")) {
                                // Video files
                                intent.setDataAndType(uri, "video");
                            } else {

                                intent.setDataAndType(uri, "*/*");
                            }
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                            try {
                                context.startActivity(intent);
                            } catch (Exception e) {
                                Log.e("error", "" + e);
                            }

                        }
                        else
                            Toast.makeText(context, "File not available", Toast.LENGTH_SHORT).show();
                        break;

                }


            }


        });
    }
}

class ListofFileAdapter extends BaseAdapter {
    LayoutInflater inflater = null;
    Context adapContext;
    ImageView image;
    TextView from, to, filetype, fileresource;
    ArrayList<TaskDetailsBean> fileArrayList=new ArrayList<>();
    String dir_path = Environment.getExternalStorageDirectory() + "/High Message/downloads/";
    boolean check=false;
    ArrayList<ContactBean> contactList;

    public ListofFileAdapter(Context context, ArrayList<TaskDetailsBean> buddyList1) {
        this.fileArrayList = buddyList1;
        adapContext = context;
        contactList=VideoCallDataBase.getDB(context).getContact(Appreference.loginuserdetails.getUsername());
    }

    @Override
    public int getCount() {
        return fileArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return fileArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View conView, ViewGroup parent) {
        try {
            if (conView == null) {
                inflater = (LayoutInflater) adapContext
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                conView = inflater.inflate(R.layout.filelistrow, null,
                        false);
            }
            final TaskDetailsBean bean = fileArrayList.get(position);
            from = (TextView) conView.findViewById(R.id.fromvalue);
            to = (TextView) conView.findViewById(R.id.tovalue);
            filetype = (TextView) conView.findViewById(R.id.filetype);
            fileresource = (TextView) conView.findViewById(R.id.fileresource);
            image = (ImageView) conView.findViewById(R.id.image);
            if(bean.getFromUserName()!=null) {
                if (bean.getFromUserName().equalsIgnoreCase(Appreference.loginuserdetails.getUsername()))
                    from.setText("Me");
                else {
                    for (ContactBean contact : contactList) {
                        //Log.i("file","name"+bean.getFromUserName());
                        if (contact.getUsername().equalsIgnoreCase(bean.getToUserName())) {
                            String name = contact.getFirstname() + " " + contact.getLastname();
                            from.setText(name);
                            break;
                        }

                    }
                }
            }
            if(bean.getToUserName()!=null){
            if(bean.getToUserName().equalsIgnoreCase(Appreference.loginuserdetails.getUsername()))
                to.setText("Me");
            else {
                for (ContactBean contact : contactList) {
//                    Log.i("file", "name" + bean.getToUserName());
                    if (contact.getUsername().equalsIgnoreCase(bean.getToUserName())) {
                        String name = contact.getFirstname() + " " + contact.getLastname();
                        Log.i("file",name);
                        to.setText(name);
                        break;
                    }

                }
            }
            }
            Log.i("file","name"+bean.getToUserName());
           if(bean.getToUserName()==null||bean.getToUserName()=="") {
                to.setText("");
            }
           if((bean.getTaskType().equalsIgnoreCase("taskConversation"))||(bean.getTaskType().equalsIgnoreCase("individual"))||(bean.getTaskType().equalsIgnoreCase("group"))) {
               fileresource.setText("Task");
               if (bean.getTaskStatus().trim().equalsIgnoreCase("draft")) {
                   fileresource.setText("Draft");
                   to.setText("null");
               }
           }
               else
                fileresource.setText(bean.getTaskType());

            filetype.setText(bean.getMimeType());

            File directory;
            switch (bean.getMimeType().toLowerCase().trim()) {
                case "video":
                    Log.i("group123", "icon clicked video");

                    if(bean.getFromUserName().equalsIgnoreCase(Appreference.loginuserdetails.getUsername()))
                        directory = new File(bean.getTaskDescription());

                    else
                         directory = new File(dir_path+bean.getTaskDescription());

                    if (directory.exists()) {
                        Bitmap bMap = ThumbnailUtils.createVideoThumbnail(directory.getAbsolutePath(), MediaStore.Video.Thumbnails.MICRO_KIND);
                        image.setImageBitmap(bMap);
                    } else {
                        image.setImageResource(R.drawable.unknown);
                        check=true;
                    }
                    break;
                case "image":
                    Log.i("group123", "icon clicked image");
                    File directory1 = new File(bean.getTaskDescription());
                    if (directory1.exists()) {
                        Picasso.with(adapContext).load(directory1).into(image);
                    } else {
                        image.setImageResource(R.drawable.unknown);
                        check=true;
                    }
                    break;
                case "sketch":
                    Log.i("group123", "icon clicked image");
                    File directory2 = new File(bean.getTaskDescription());
                    if (directory2.exists()) {
                        Picasso.with(adapContext).load(directory2).into(image);
                    }  else {
                        image.setImageResource(R.drawable.unknown);
                        check=true;
                    }
                    break;
                case "audio":
                    image.setImageResource(R.drawable.audioios);
                    break;
                default:
                    Log.i("file", "executed");
                    String ext = bean.getTaskDescription().substring(bean.getTaskDescription().lastIndexOf(".") + 1, bean.getTaskDescription().length());
                    Log.i("file", "executed1");
                    switch (ext) {
                        case "pdf":
                            image.setImageResource(R.drawable.pdf12);
                            break;
                        case "doc":
                        case "docx":
                        case "txt":
                            image.setImageResource(R.drawable.msword);
                            break;
                        case "xls":
                            image.setImageResource(R.drawable.excel);
                            break;
                        case "ppt":
                            image.setImageResource(R.drawable.ppt);
                            break;
                        default:
                            image.setImageResource(R.drawable.unknown);
                            break;

                    }
                    break;
                    /*image.setImageResource(R.drawable.unknown);
                    check=true;*/
            }


               /* if(check) {
                    String ext = bean.getTaskDescription().substring(bean.getTaskDescription().lastIndexOf(".") + 1, bean.getTaskDescription().length());
                Log.i("file", "executed1");
                switch (ext) {
                    case "pdf":
                        image.setImageResource(R.drawable.pdf12);
                        break;
                    case "doc":
                    case "docx":
                    case "txt":
                        image.setImageResource(R.drawable.msword);
                        break;
                    case "xls":
                        image.setImageResource(R.drawable.excel);
                        break;
                    case "ppt":
                        image.setImageResource(R.drawable.ppt);
                        break;
                    default:
                        image.setImageResource(R.drawable.unknown);
                        break;

                }
                    check=false;
            }*/
        } catch (Exception e) {
            e.printStackTrace();
        }


        return conView;
    }
}