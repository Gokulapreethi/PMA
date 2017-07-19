package com.ase.expand;

/**
 * Created by vignesh on 6/20/2016.
 */
import java.util.ArrayList;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ase.ImageLoader;
import com.ase.R;

public class ExpandableAdapter extends BaseExpandableListAdapter {

    private Context context;
    private ArrayList<Group> groups;
    ImageLoader imageLoader= new ImageLoader(context);
    

    public ExpandableAdapter(Context context, ArrayList<Group> groups) {
        this.context = context;
        this.groups = groups;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        ArrayList<Child> chList = groups.get(groupPosition)
                .getItems();
        return chList.get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        Child child = (Child) getChild(groupPosition,
                childPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context
                    .getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.child_item, null);
        }
        TextView tv = (TextView) convertView.findViewById(R.id.country_name);
        ImageView iv = (ImageView) convertView.findViewById(R.id.flag);
        Log.i("Picasso","Child"+child.getImage());

        //File myFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/HighMessage/profilePic/"+child.getImage());
        if(child.getImage()!=null){
            imageLoader.DisplayImage(Environment.getExternalStorageDirectory().getAbsolutePath() + "/High Message/profilePic/"+child.getImage(),iv,R.drawable.default_person_circle);
            //Picasso.with(context).load("http://122.165.92.171:8080/uploads/highmessaging/user/" + child.getImage()).into(iv);

        }else{
            iv.setBackgroundResource(R.drawable.default_person_circle);
        }

        tv.setText(child.getName().toString());
        //Picasso.with(context).load("http://122.165.92.171:8080/uploads/highmessaging/user/" + child.getImage()).into(iv);
        imageLoader.DisplayImage(Environment.getExternalStorageDirectory().getAbsolutePath() + "/High Message/profilePic/"+child.getImage(),iv,R.drawable.default_person_circle);
        // tv.setText(child.getName().toString()+"::"+child.getTag());
        // tv.setTag(child.getTag());
        // TODO Auto-generated method stub
        return convertView;

    }

    @Override
    public int getChildrenCount(int groupPosition) {
        ArrayList<Child> chList = groups.get(groupPosition)
                .getItems();

        return chList.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        // TODO Auto-generated method stub
        return groups.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        // TODO Auto-generated method stub
        return groups.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        // TODO Auto-generated method stub
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        Group group = (Group) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater inf = (LayoutInflater) context
                    .getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = inf.inflate(R.layout.group_item, null);
        }
        TextView tv = (TextView) convertView.findViewById(R.id.group_name);
        ImageView iv = (ImageView) convertView.findViewById(R.id.flag1);
//        ImageView clicks = (ImageView) convertView.findViewById(R.id.group_icon);

//        clicks.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(context, TaskHistory.class);
//
//            }
//        });

        Log.i("Picasso","Group"+group.getImage());

        //File myFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/HighMessage/profilePic/"+group.getImage());
        if(group.getImage()!=null){
            Log.i("setting","setting");
            //Picasso.with(context).load("http://122.165.92.171:8080/uploads/highmessaging/group/" + group.getImage()).into(iv);
            imageLoader.DisplayImage(Environment.getExternalStorageDirectory().getAbsolutePath() + "/High Message/groupPic/"+group.getImage(),iv,R.drawable.default_person_circle);
        }else{
            iv.setBackgroundResource(R.drawable.default_person_circle);
        }
        tv.setText(group.getName());
        imageLoader.DisplayImage(Environment.getExternalStorageDirectory().getAbsolutePath() + "/High Message/groupPic/"+group.getImage(),iv,R.drawable.default_person_circle);


        //Picasso.with(context).load("http://122.165.92.171:8080/uploads/highmessaging/group/" + group.getImage()).into(iv);
        // TODO Auto-generated method stub
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        return true;
    }

}

