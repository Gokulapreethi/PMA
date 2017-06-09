package com.myapplication3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

/**
 * Created by vignesh on 6/17/2016.
 */
public class Splash extends Activity {
    @Override
    protected void onCreate(Bundle SaveInstancestate){
        super.onCreate(SaveInstancestate);
        setContentView(R.layout.splash);
        final ImageView logo=(ImageView)findViewById(R.id.logo);

        Thread timerThread = new Thread(){
            public void run(){
                try{
                    logo.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.bounce));
                    sleep(2000);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }finally{
                    Intent intent = new Intent(Splash.this,LoginActivity.class);
                    startActivity(intent);
                }
            }
        };
        timerThread.start();
    }
    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        finish();
    }
}
