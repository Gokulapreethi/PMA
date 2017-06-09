package com.myapplication3.Bean;

/**
 * Created by Ramdhas on 25-07-2016.
 */
public class User_Details {

        String user_Name;
        Integer no_Of_Login;
        public User_Details(String user_Name,Integer no_Of_Login)
        {
            this.user_Name=user_Name;
            this.no_Of_Login=no_Of_Login;
        }

        public int getNo_Of_Login() {
            return no_Of_Login;
        }

        public String getUser_Name() {
            return user_Name;
        }


    }

