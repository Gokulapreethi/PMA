package com.ase;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.ase.DB.VideoCallDataBase;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by sabarinathan on 12-07-2017.
 */
public class EulaScreen extends AppCompatActivity{
    RadioGroup eularadio;
    Button btn_ok;
    int clicked=0;
    TextView eulatext;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eulascreen);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        eularadio=(RadioGroup)findViewById(R.id.eularadio);
        btn_ok=(Button)findViewById(R.id.btnok);
        eulatext=(TextView)findViewById(R.id.eulatext);
//        eulatext.setText(getResources().getString(R.string.eula)+getResources().getString(R.string.eula)+getResources().getString(R.string.eula));
//        eulatext.setTextAlignment();
        eulatext.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);

        AssetManager assetManager = getAssets();

        // To get names of all files inside the "Files" folder
        try {
            String[] files = assetManager.list("");

            for(int i=0; i<files.length; i++)            {
//                txtFileName.append("\n File :"+i+" Name => "+files[i]);
            }
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        // To load text file
        InputStream input;
        try {
            input = assetManager.open("eula_HM.txt");

            int size = input.available();
            byte[] buffer = new byte[size];
            input.read(buffer);
            input.close();

            // byte buffer into a string
            String text = new String(buffer);

            eulatext.setText(text);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        eularadio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.agreeradio:
                        clicked=1;
                        VideoCallDataBase.getDB(getApplicationContext()).insertvalueeula(true);
                        break;
                    case R.id.disagreeradio:
                        clicked=0;
                        VideoCallDataBase.getDB(getApplicationContext()).insertvalueeula(false);
                        break;
                }
            }
        });
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clicked == 1){
                    Intent login=new Intent(getApplicationContext(),LoginActivity.class);
                    startActivity(login);
                }
                else {
                    Intent startMain = new Intent(Intent.ACTION_MAIN);
                    startMain.addCategory(Intent.CATEGORY_HOME);
                    startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(startMain);
                }
            }
        });
    }
}
