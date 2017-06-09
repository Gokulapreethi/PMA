package json;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vignesh on 6/4/2016.
 */
public class ListUserGroupDetails {


    private ArrayList<ListUserGroupObject> postList = new ArrayList<ListUserGroupObject>();

    public List<ListUserGroupObject> getPostList() {
        return postList;
    }

    public void setPostList(List<ListUserGroupObject> postList) {
        this.postList = (ArrayList<ListUserGroupObject>)postList;
    }

//    ArrayList<ListUserGroupObject> list;
//
//
//    public ArrayList<ListUserGroupObject> getList() {
//        return list;
//    }
//
//    public void setList(ArrayList<ListUserGroupObject> list) {
//        this.list = list;
//    }


}
