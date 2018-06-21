package com.ase;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfDocument;
import android.graphics.pdf.PdfRenderer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import net.sf.andpdf.pdfviewer.PdfViewerActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Preethi on 5/31/2018.
 */

public class PdfRenderererActivity extends AppCompatActivity implements View.OnClickListener {
    /**
     * For identifying current view mode read/create/listing/options
     *
     * @author androidsrc.net
     */
    interface CurrentView {
        int OPTIONS_LAYOUT = 1;
        int READ_LAYOUT = 2;
        int PDF_SELECTION_LAYOUT = 4;
    }

    /**
     * FrameLayout child views. We will manage our UI to one layout
     * Hide/Show these views as per requirement
     */
    LinearLayout readLayout;
    LinearLayout pdfSelectionLayout;

    private static int currentView;

    // Pdf content will be generated with User Input Text
    EditText pdfContentView;
    //For navigating back
    MenuItem closeOption;
    // List view for showing pdf files
    ListView pdfList;
    //Background task to generate pdf file listing
    PdfListLoadTask listTask;
    //Adapter to list view
    ArrayAdapter<String> adapter;
    // array of pdf files
    File[] filelist;

    //index to track currentPage in rendered Pdf
    private static int currentPage = 0;
    //View on which Pdf content will be rendered
    ImageView pdfView;

    //Currently rendered Pdf file
    String openedPdfFileName;
    Button generatePdf;
    Button next;
    Button previous;

    //File Descriptor for rendered Pdf file
    private ParcelFileDescriptor mFileDescriptor;
    //For rendering a PDF document
    private PdfRenderer mPdfRenderer;
    //For opening current page, render it, and close the page
    private PdfRenderer.Page mCurrentPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_view);

        readLayout = (LinearLayout) findViewById(R.id.read_layout);
        pdfSelectionLayout = (LinearLayout) findViewById(R.id.pdf_selection_layout);
        pdfContentView = (EditText) findViewById(R.id.pdf_content);

        next = (Button) findViewById(R.id.next);
        next.setOnClickListener(this);
        previous = (Button) findViewById(R.id.previous);
        previous.setOnClickListener(this);
        generatePdf = (Button) findViewById(R.id.generate_pdf);
        generatePdf.setOnClickListener(this);

        pdfList = (ListView) findViewById(R.id.pdfList);
        pdfList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //On Clicking list item, Render Pdf file corresponding to filePath
//                openedPdfFileName = adapter.getItem(position);
                openedPdfFileName = adapter.getItem(position);
                String openedPdfFileName_url = Environment
                        .getExternalStorageDirectory().getAbsolutePath()
                        + "/High Message/servicemanual/" + openedPdfFileName;
                openRenderer(openedPdfFileName_url);
                updateView(CurrentView.READ_LAYOUT);
            }
        });
        pdfView = (ImageView) findViewById(R.id.pdfView);

        currentView = CurrentView.PDF_SELECTION_LAYOUT;
        updateView(CurrentView.PDF_SELECTION_LAYOUT);
        Log.i("pdf123", "inside updateView currentView==>" + currentView);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        Log.i("pdf123", "inside onCreateOptionsMenu");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.pdf_main_menu, menu);
//        getMenuInflater().inflate(R.menu.pdf_main_menu, menu);
        closeOption = (MenuItem) MenuItemCompat.getActionView(menu.findItem(R.id.action_close_pdf));
//        closeOption.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_close_pdf) {
            if (currentView == CurrentView.PDF_SELECTION_LAYOUT) {
                finish();
            } else if (currentView == CurrentView.READ_LAYOUT) {
                if (listTask != null)
                    listTask.cancel(true);
                listTask = new PdfListLoadTask();
                listTask.execute();
                updateView(CurrentView.PDF_SELECTION_LAYOUT);
            } else {
                finish();
            }
            return true;
        } else if (id == R.id.action_crop_pdf) {
            if (currentView == CurrentView.READ_LAYOUT) {
                openCropPage();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void openCropPage() {
        View v1 = getWindow().getDecorView().getRootView();
        String path = Appreference.takeScreenshot(v1);
        Appreference.PDFConvImagePath.clear();
        Appreference.PDFConvImagePath.add(path);
        callFullscreenpdfview(Appreference.PDFConvImagePath);
    }

    /**
     * Handle selected page view as jpg
     * selected page and their path goes to crop selection
     */
    private void callFullscreenpdfview(ArrayList<String> imagePath) {
        try {
            String ImageName = imagePath.get(0);
            File file = null;
            if (ImageName != null && !ImageName.equalsIgnoreCase("")) {
                file = new File(ImageName);
                if (file.exists()) {
                    Intent i = new Intent(this, FullScreenViewActivity.class);
                    i.putExtra("position", 0);
                    i.putExtra("fromPDFcrop", true);
                    /*
                    * "fromConvCrop" is for to close fullscreenViewActivity immediately after crop
                    * */
                    i.putExtra("fromConvCrop", true);
                    if (imagePath != null && imagePath.size() > 0) {
                        i.putExtra("pathSketch", imagePath);
                    }
                    startActivityForResult(i, 433);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Handler back key
     * Update UI current view is not options view
     * else call super.onBackPressed()
     */
    @Override
    public void onBackPressed() {
        if (currentView == CurrentView.PDF_SELECTION_LAYOUT) {
//            updateView(CurrentView.OPTIONS_LAYOUT);
//            updateActionBarText();
            super.onBackPressed();
        } else if (currentView == CurrentView.READ_LAYOUT) {
            if (listTask != null)
                listTask.cancel(true);
            listTask = new PdfListLoadTask();
            listTask.execute();
            updateView(CurrentView.PDF_SELECTION_LAYOUT);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * API to hide keyboard if shown
     * Will be used when user naviagates after generating Pdf
     */
    private void hideInputMethodIfShown() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(pdfContentView.getWindowToken(), 0, null);
    }


    /**
     * API to update ActionBar text
     */
    private void updateActionBarText() {
        try {
            if (currentView == CurrentView.READ_LAYOUT) {
                int index = mCurrentPage.getIndex();
                int pageCount = mPdfRenderer.getPageCount();
                previous.setEnabled(0 != index);
                next.setEnabled(index + 1 < pageCount);
                String Currentfilename = null;
                if (openedPdfFileName != null && openedPdfFileName.contains("/storage/emulated/")) {
                    Currentfilename = openedPdfFileName.substring(openedPdfFileName.lastIndexOf("/") + 1);
                } else {
                    Currentfilename = openedPdfFileName;
                }

                getSupportActionBar().setTitle(
                        Currentfilename + "(" + (index + 1) + "/" + pageCount
                                + ")");
            } else {
                getSupportActionBar().setTitle("Service Manuals");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * API to update View
     *
     * @param updateView updateView specifies the target view
     */
    private void updateView(int updateView) {
        switch (updateView) {
            case CurrentView.READ_LAYOUT:
                currentView = CurrentView.READ_LAYOUT;
                Log.i("pdf123", "inside updateView currentView==>" + currentView);
                closeRenderer();
                if (openedPdfFileName != null && !openedPdfFileName.contains("/storage/emulated/")) {
                    openedPdfFileName = Environment
                            .getExternalStorageDirectory().getAbsolutePath()
                            + "/High Message/servicemanual/" + openedPdfFileName;
                }
                openRenderer(openedPdfFileName);
                currentPage = 0;
                showPage(currentPage);
                readLayout.setVisibility(View.VISIBLE);
                pdfSelectionLayout.setVisibility(View.INVISIBLE);
                break;
            case CurrentView.PDF_SELECTION_LAYOUT:
                currentView = CurrentView.PDF_SELECTION_LAYOUT;
                Log.i("pdf123", "inside updateView currentView==>" + currentView);
                closeRenderer();
                if (listTask != null)
                    listTask.cancel(true);
                listTask = new PdfListLoadTask();
                listTask.execute();
                readLayout.setVisibility(View.INVISIBLE);
                pdfSelectionLayout.setVisibility(View.VISIBLE);
                break;

        }
    }


    /**
     * Callback for handling view click events
     */
    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        switch (viewId) {
            case R.id.next:
                currentPage++;
                showPage(currentPage);
                break;
            case R.id.previous:
                currentPage--;
                showPage(currentPage);
                break;
        }

    }


    /**
     * API for initializing file descriptor and pdf renderer after selecting pdf from list
     *
     * @param filePath
     */
    private void openRenderer(String filePath) {
        File file = new File(filePath);
        try {
            mFileDescriptor = ParcelFileDescriptor.open(file,
                    ParcelFileDescriptor.MODE_READ_ONLY);
            mPdfRenderer = new PdfRenderer(mFileDescriptor);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            closeRenderer();
            Toast.makeText(getApplicationContext(),
                    "Sync Error ,Try Again...",
                    Toast.LENGTH_LONG).show();
        }
    }

    /**
     * API for cleanup of objects used in rendering
     */
    private void closeRenderer() {

        try {
            if (mCurrentPage != null)
                mCurrentPage.close();
            if (mPdfRenderer != null)
                mPdfRenderer.close();
            if (mFileDescriptor != null)
                mFileDescriptor.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * API show to particular page index using PdfRenderer
     *
     * @param index
     */
    private void showPage(int index) {
        try {
            if (mPdfRenderer == null || mPdfRenderer.getPageCount() <= index
                    || index < 0) {
                return;
            }
            // For closing the current page before opening another one.
            try {
                if (mCurrentPage != null) {
                    mCurrentPage.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            // Open page with specified index
            mCurrentPage = mPdfRenderer.openPage(index);
            Bitmap bitmap = Bitmap.createBitmap(mCurrentPage.getWidth(),
                    mCurrentPage.getHeight(), Bitmap.Config.ARGB_8888);

            //Pdf page is rendered on Bitmap
            mCurrentPage.render(bitmap, null, null,
                    PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
            //Set rendered bitmap to ImageView
            pdfView.setImageBitmap(bitmap);
            updateActionBarText();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Background task for listing pdf files
     *
     * @author androidsrc.net
     */
    private class PdfListLoadTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            File files = new File(Environment
                    .getExternalStorageDirectory().getAbsolutePath()
                    + "/High Message/servicemanual/");
            filelist = files.listFiles(new FilenameFilter() {
                public boolean accept(File dir, String name) {
                    return ((name.endsWith(".pdf")));
                }
            });

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub

            if (filelist != null && filelist.length >= 1) {
                ArrayList<String> fileNameList = new ArrayList<>();
                for (int i = 0; i < filelist.length; i++) {
                    String filename = filelist[i].getPath().substring(filelist[i].getPath().lastIndexOf("/") + 1);
                    fileNameList.add(filename);
//                    fileNameList.add(filelist[i].getPath());
                }
                adapter = new ArrayAdapter<>(getApplicationContext(),
                        R.layout.list_pdf_item, fileNameList);
                pdfList.setAdapter(adapter);
            } else {
                Toast.makeText(getApplicationContext(),
                        "No pdf file found, Please create new Pdf file",
                        Toast.LENGTH_LONG).show();
//                updateView(CurrentView.OPTIONS_LAYOUT);
//                updateActionBarText();
            }
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 433) {
                try {
                    Log.i("pdf123", "strIPath requestCode==>  + 423 received");
                    Appreference.PDFConvImagePath.clear();
                    ArrayList<String> myPDF_strIPath = data.getExtras().getStringArrayList("pathpdf");
                    String path = myPDF_strIPath.get(myPDF_strIPath.size() - 1);
                    Intent i = new Intent();
                    i.putExtra("path", path);
                    setResult(RESULT_OK, i);
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
