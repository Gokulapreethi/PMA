package com.myapplication3.expand;

import java.util.ArrayList;

public class Group {

    private String Name;
    private String id;
    private String Image;
    private Integer msg_count;
    Boolean ischeck = false;
    private ArrayList<Child> Items;

    public Boolean getIscheck() {
        return ischeck;
    }

    public void setIscheck(Boolean ischeck) {
        this.ischeck = ischeck;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getMsg_count() {
        return msg_count;
    }

    public void setMsg_count(Integer msg_count) {
        this.msg_count = msg_count;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String Image) {
        this.Image = Image;
    }

    public ArrayList<Child> getItems() {
        return Items;
    }

    public void setItems(ArrayList<Child> Items) {
        this.Items = Items;
    }


}
