package com.myapplication3;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Created by sabarinathan on 01-03-2017.
 */

public class bitmaploader extends AsyncTask<Uri, Void, Void> {
    String sig_id,strIPath,subType;
    NewTaskConversation newTaskConversation = (NewTaskConversation) Appreference.context_table.get("taskcoversation");
    Context context;

    public bitmaploader(String sig_id, String strIPath, String subType, Context context) {
        this.sig_id = sig_id;
        this.strIPath = strIPath;
        this.subType = subType;
        this.context = context;
    }

    @Override
    protected void onPostExecute(Void result) {
        // TODO Auto-generated method stub
        try {
            super.onPostExecute(result);
            Log.d("image", "came to post execute for image");
            Bitmap img = null;
            if (strIPath != null) {
                img = newTaskConversation.convertpathToBitmap(strIPath);
                Log.i("task", "extension" + strIPath);
                String pathExtn = strIPath.split("/")[5];
                Log.i("task", "spilting" + pathExtn);
                pathExtn = pathExtn.substring(pathExtn.length() - 3);
                Log.i("task", "root---->" + pathExtn);
                if (subType == null && subType.equalsIgnoreCase(null) && subType.equalsIgnoreCase("")) {
                    subType = "normal";
                } else if (subType != null && !subType.equalsIgnoreCase("taskDescription")) {
                    subType = "normal";
                }
                if (newTaskConversation.isTaskName) {
                    newTaskConversation.is_mmfile = true;
                    if (newTaskConversation.des.getText().toString().trim().length() > 0) {
                        newTaskConversation.taskName =newTaskConversation.des.getText().toString();
                    } else {
                        if (!newTaskConversation.template && !newTaskConversation.note) {
                            newTaskConversation.taskName = "New Task";
                            Log.i("taskconversation", "headername  12 ");
                        } else if (newTaskConversation.note) {
                            newTaskConversation.taskName = "New Note";
                            Log.i("taskconversation", "headername  13 ");
                        } else {
                            newTaskConversation.taskName = "New Template";
                            Log.i("taskconversation", "headername 14 ");
                        }
                    }
                    newTaskConversation.sendMessage(newTaskConversation.taskName, null, "text", null, "", sig_id,null);
                    newTaskConversation.des.setText("");
                    Log.i("Task conversation", "image>19 * " + sig_id);
                } else {
                    newTaskConversation.PercentageWebService("image", strIPath, "", sig_id,0);
                    Log.i("Task conversation", "image>19 ** " + sig_id);
                }
                newTaskConversation.refreshListViewCache();
            }
            if (img != null) {
                Log.d("OnActivity", "_____On Activity Called______");
            } else {
                strIPath = null;
            }
        } catch (Exception e) {
            Log.e("profile", "====> " + e.getMessage());
        }
    }

    @Override
    protected void onPreExecute() {
        // TODO Auto-generated method stub
        try {
            super.onPreExecute();
            ProgressDialog dialog = new ProgressDialog(context);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.e("profile", "====> " + e.getMessage());
        }
    }

    @Override
    protected Void doInBackground(Uri... params) {
        // TODO Auto-generated method stub
        try {
            for (Uri uri : params) {
                Log.d("image", "came to doin backgroung for image");
                FileInputStream fin = (FileInputStream)context.getContentResolver()
                        .openInputStream(uri);
                ByteArrayOutputStream straam = new ByteArrayOutputStream();
                byte[] content = new byte[1024];
                int bytesread;
                while ((bytesread = fin.read(content)) != -1) {
                    straam.write(content, 0, bytesread);
                }
                byte[] bytes = straam.toByteArray();
                FileOutputStream fout = new FileOutputStream(strIPath);
                straam.flush();
                straam.close();
                straam = null;
                fin.close();
                fin = null;
                fout.write(bytes);
                fout.flush();
                fout.close();
                fout = null;
            }
        } catch (Exception e) {
        }
        return null;
    }
}
