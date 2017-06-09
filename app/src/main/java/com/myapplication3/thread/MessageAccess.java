package com.myapplication3.thread;

/**
 * Created by Amuthan on 03/04/2017.
 */

public interface MessageAccess  {
    void notifyChat_Received(String FromUri, String toUri, String mimeType, String message);

}
