
package com.ase;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;


public class FilePicker extends Activity {


    ArrayList<String> str = new ArrayList<String>();
    private Boolean firstLvl = true;
    private static final String TAG = "F_PATH";
    private String copyFilePath = Environment.getExternalStorageDirectory()
            .getAbsolutePath() + "/High Message/";
    private Item[] fileList;
    private File path = new File(Environment.getExternalStorageDirectory() + "");
    private String chosenFile;
    private static final int DIALOG_LOAD_FILE = 1000;
    private Button btnDone = null;
    private Button btnBack = null;
    private TextView title = null;
    private Button selectFile = null;
    private Context context = null;
    ListAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);

            setContentView(R.layout.filepicker);

            context = this;
            DisplayMetrics displaymetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
            int noScrHeight = displaymetrics.heightPixels;
            int noScrWidth = displaymetrics.widthPixels;

            displaymetrics = null;
            btnDone = (Button) findViewById(R.id.btncomp);
            btnDone.setVisibility(View.GONE);
            btnBack = (Button) findViewById(R.id.settings);
            btnBack.setTextSize(14);
            selectFile = (Button) findViewById(R.id.selectFile);
            selectFile.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    loadFileList();
                    showDialog(DIALOG_LOAD_FILE);
                }
            });
            btnBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    finish();
                }
            });
            loadFileList();
            showDialog(DIALOG_LOAD_FILE);
            Log.d(TAG, path.getAbsolutePath());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void loadFileList() {
        try {
            path.mkdirs();
            if (path.exists()) {
                FilenameFilter filter = new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String filename) {
                        File sel = new File(dir, filename);
                        return (sel.isFile() || sel.isDirectory())
                                && !sel.isHidden();
                    }
                };
                String[] fList = path.list(filter);
                fileList = new Item[fList.length];
                for (int i = 0; i < fList.length; i++) {
                    fileList[i] = new Item(fList[i], R.drawable.filetxt);
                    File sel = new File(path, fList[i]);
                    if (sel.isDirectory()) {
                        fileList[i].icon = R.drawable.filetxt;
                        Log.d("DIRECTORY", fileList[i].file);
                    } else {
                        Log.d("FILE", fileList[i].file);
                    }
                }
                if (!firstLvl) {
                    Item temp[] = new Item[fileList.length + 1];
                    for (int i = 0; i < fileList.length; i++) {
                        temp[i + 1] = fileList[i];
                    }
                    temp[0] = new Item("Up", R.drawable.filetxt);
                    fileList = temp;
                }
            } else {
                Log.e(TAG, "path does not exist");
            }

            adapter = new ArrayAdapter<Item>(this,
                    android.R.layout.select_dialog_item, android.R.id.text1,
                    fileList) {
                @Override
                public View getView(int position, View convertView,
                                    ViewGroup parent) {
                    try {
                        // creates view
                        View view = super
                                .getView(position, convertView, parent);
                        TextView textView = (TextView) view
                                .findViewById(android.R.id.text1);

                        // put the image on the text view
                        textView.setCompoundDrawablesWithIntrinsicBounds(
                                fileList[position].icon, 0, 0, 0);
                        int dp5 = (int) (5 * getResources().getDisplayMetrics().density + 0.5f);
                        textView.setCompoundDrawablePadding(dp5);

                        return view;
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        return null;
                    }
                }
            };
        } catch (Exception e) {
            Log.e(TAG, "unable to write on the sd card ");
        }
    }

    private class Item {
        public String file;
        public int icon;

        public Item(String file, Integer icon) {
            this.file = file;
            this.icon = icon;
        }

        @Override
        public String toString() {
            return file;
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        try {
            Dialog dialog = null;
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);

            if (fileList == null) {
                Log.e(TAG, "No files loaded");
                dialog = builder.create();
                return dialog;
            }
            switch (id) {
                case DIALOG_LOAD_FILE:
                    builder.setAdapter(adapter,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    ((Dialog) dialog)
                                            .setCanceledOnTouchOutside(true);


                                    chosenFile = fileList[which].file;
                                    File sel = new File(path + "/" + chosenFile);
                                    if (sel.isDirectory()) {
                                        firstLvl = false;
                                        str.add(chosenFile);
                                        fileList = null;
                                        path = new File(sel + "");
                                        loadFileList();
                                        removeDialog(DIALOG_LOAD_FILE);
                                        showDialog(DIALOG_LOAD_FILE);
                                        Log.d(TAG, path.getAbsolutePath());
                                    } else if (chosenFile.equalsIgnoreCase("up")
                                            && !sel.exists()) {
                                        // present directory removed from list
                                        String s = str.remove(str.size() - 1);
                                        path = new File(path.toString().substring(
                                                0, path.toString().lastIndexOf(s)));
                                        fileList = null;
                                        if (str.isEmpty()) {
                                            firstLvl = true;
                                        }
                                        loadFileList();

                                        removeDialog(DIALOG_LOAD_FILE);
                                        showDialog(DIALOG_LOAD_FILE);
                                        Log.d(TAG, path.getAbsolutePath());

                                    } else {
                                        try {
                                            if (sel.isFile()) {
                                                File ComFile = new File(
                                                        copyFilePath);

                                                if (!ComFile.exists()) {
                                                    ComFile.mkdir();
                                                }
                                                String destinationPath = copyFilePath
                                                        + chosenFile;

                                                File destination = new File(
                                                        destinationPath);
                                                try {
                                                    InputStream in = new FileInputStream(
                                                            sel.getAbsolutePath());

                                                    OutputStream out = new FileOutputStream(
                                                            destinationPath);
                                                    byte[] buf = new byte[1024];
                                                    int len;
                                                    while ((len = in.read(buf)) > 0) {
                                                        out.write(buf, 0, len);
                                                    }
                                                    in.close();
                                                    out.close();
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }

                                                int n = chosenFile.lastIndexOf(".");
                                                String name = chosenFile;
                                                String fileName, fileExt;

                                                if (n == -1)
                                                    return;

                                                else {
                                                    fileName = name.substring(0, n);
                                                    fileExt = name.substring(n);
//                                                    if
//                                                            (!fileExt.equals(".docx")
//                                                            &&
//                                                            !fileExt.equals(".pdf")
//                                                            && !fileExt.equals("doc")
//                                                            && !fileExt.equals("xls")
//                                                            &&
//                                                            !fileExt.equals("xlsx")
//                                                            &&
//                                                            !fileExt.equals(".txt"))
//                                                        return;
//                                                    else {
//                                                        Toast.makeText(getApplicationContext(), "please select only document file ", Toast.LENGTH_LONG);
//                                                    }
                                                }
                                                Log.i("path-->", "desti.." + destinationPath);
                                                if (fileExt.contains(".")) {
                                                    fileExt = fileExt.substring(1);
                                                    Log.i("file", "ext" + fileExt);
                                                }
                                                Intent i = new Intent();
                                                i.putExtra("filePath", destinationPath);
                                                i.putExtra("fileName", fileName);
                                                i.putExtra("fileExt", fileExt);
                                                setResult(RESULT_OK, i);
                                                finish();


                                            }
                                        } catch (Exception e) {
                                            Log.e("sharefile",
                                                    "===> " + e.getMessage());
                                        }
                                    }

                                }
                            });
                    break;
            }

            dialog = builder.show();

            dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        Intent i = new Intent();
                        setResult(RESULT_FIRST_USER, i);
                        finish();
                        dialog.dismiss();
                    }
                    return false;
                }
            });

            return dialog;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        try {
            // TODO Auto-generated method stub
            if ((keyCode == KeyEvent.KEYCODE_BACK)) {
                if ((keyCode == KeyEvent.KEYCODE_BACK)) {
                    finish();
                }
            }
            finish();
            return super.onKeyDown(keyCode, event);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
    }


}