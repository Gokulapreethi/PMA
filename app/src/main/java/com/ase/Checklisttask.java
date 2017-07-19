package com.ase;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;

public class Checklisttask extends AppCompatActivity {
    private CheckBox c1,c2,c3,c4,c5,c6;

    private RadioButton low,medium,high;
    private Button cancel,next;
    private String priority="Medium";
    LinearLayout linear2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checking);
        c1=(CheckBox)findViewById(R.id.check1);
        c2=(CheckBox)findViewById(R.id.check2);
        c3=(CheckBox)findViewById(R.id.check3);
        c4=(CheckBox)findViewById(R.id.check4);
        c5=(CheckBox)findViewById(R.id.check5);
        c6=(CheckBox)findViewById(R.id.check6);

        linear2=(LinearLayout)findViewById(R.id.linear2);

        cancel=(Button)findViewById(R.id.cancel);
        next=(Button)findViewById(R.id.next);


        low=(RadioButton) findViewById(R.id.low);
        medium=(RadioButton) findViewById(R.id.medium);
        high=(RadioButton) findViewById(R.id.high);


        low.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (low.isChecked()) {
                    high.setChecked(false);
                    medium.setChecked(false);
                    priority="Low";
                }
            }
        });
        medium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (medium.isChecked()) {
                    low.setChecked(false);
                    high.setChecked(false);
                    priority="Medium";
                }
            }
        });
        high.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (high.isChecked()) {
                    low.setChecked(false);
                    medium.setChecked(false);
                    priority="High";
                }
            }
        });



        cancel.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

        finish();
    }
});
        c6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(c6.isChecked())
                {
                    linear2.setVisibility(View.VISIBLE);
                }else
                    linear2.setVisibility(View.INVISIBLE);
            }
        });
next.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent=new Intent(getApplicationContext(),NewTaskActivity.class);
        intent.putExtra("toUserName","");
        intent.putExtra(c1.getText().toString(),c1.isChecked());
        intent.putExtra(c2.getText().toString(),c2.isChecked());
        intent.putExtra(c3.getText().toString(),c3.isChecked());
        intent.putExtra(c4.getText().toString(),c4.isChecked());
        intent.putExtra(c5.getText().toString(),c5.isChecked());
        intent.putExtra("priority",priority);
        startActivity(intent);
    }
});
    }
}
