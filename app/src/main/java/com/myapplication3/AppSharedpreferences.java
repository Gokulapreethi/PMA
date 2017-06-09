package com.myapplication3;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import json.Loginuserdetails;

/**
 * Created by vignesh on 7/13/2016.
 */

public class AppSharedpreferences {

    private static AppSharedpreferences yourPreference;
    private SharedPreferences sharedPreferences;

    public static AppSharedpreferences getInstance(Context context) {
        if (yourPreference == null) {
            yourPreference = new AppSharedpreferences(context);
        }
        return yourPreference;
    }

    private AppSharedpreferences(Context context) {
            sharedPreferences = context.getSharedPreferences("HighMessage", Context.MODE_PRIVATE);

    }

    // set String Values

    public void saveString(String key, String value) {
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.putString(key, value);
        prefsEditor.commit();
    }

  /*  public void setTaskid() {
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        Set<String> set = new HashSet<String>();
        set.addAll(your Arraylist Name);
        prefsEditor.putStringSet("yourKey", set);
        prefsEditor.commit();
    }
*/

    // set Boolean Values

    public void saveBoolean(String key, Boolean value) {
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.putBoolean(key, value);
        prefsEditor.commit();
    }


//    clear Boolean Values
    public void clearBoolean(String key, Boolean value) {
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.putBoolean(key, value);
        prefsEditor.commit();
    }


    // save integer values

    public void saveInteger(String key, Integer value) {
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.putInt(key, value);
        prefsEditor.commit();
    }

    // get String Values

    public String getString(String key) {
        if (sharedPreferences != null) {
            return sharedPreferences.getString(key, "");
        }
        return "";
    }

    // get Boolean Values

    public Boolean getBoolean(String key) {
        if (sharedPreferences != null) {
            return sharedPreferences.getBoolean(key, false);
        }
        return false;
    }

    //get integer values

    public Integer getInteger(String key) {
        if (sharedPreferences != null) {
            return sharedPreferences.getInt(key, 0);
        }
        return 0;
    }
}


