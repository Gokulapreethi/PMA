package com.ase;

import com.ase.Bean.ProjectDetailsBean;

import java.util.Comparator;

/**
 * Created by Preethi on 26-07-2017.
 */
public class projectStatusComparator implements Comparator<ProjectDetailsBean> {
    @Override
    public int compare(ProjectDetailsBean oldbean, ProjectDetailsBean newbean) {
        if(oldbean.getTaskStatus()!=null && newbean.getTaskStatus()!=null)
            return (oldbean.getTaskStatus().compareToIgnoreCase(newbean.getTaskStatus()));
        else
            return (oldbean.getTaskStatus().compareToIgnoreCase(newbean.getTaskStatus()));
    }
}
