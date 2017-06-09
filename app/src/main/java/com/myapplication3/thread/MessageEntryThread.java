package com.myapplication3.thread;

import android.util.Log;

import json.Queue;

/**
 * Created by Amuthan on 01/04/2017.
 */

public class MessageEntryThread extends Thread {

    private Queue queue;
    static boolean isRunning = true;
    private static MessageEntryThread messageEntryThread;

    public static MessageEntryThread getMessageEntryThread(boolean isRun) {
        try {
            Log.i("dbentry","inside Message Entry Thread \n messageEntryThread : "+messageEntryThread);
//            isRunning = isRun;
            if (messageEntryThread == null) {
                messageEntryThread = new MessageEntryThread();
                messageEntryThread.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return messageEntryThread;
    }

    public void stopThread(){
        setRunning(false);
        setQueue(null);
        messageEntryThread = null;

    }

    public Queue getQueue() {
        return queue;
    }

    public void setQueue(Queue queue) {
        this.queue = queue;
    }

    public void setRunning(boolean isRunning) {
        this.isRunning = isRunning;
    }

    @Override
    public void run() {
        super.run();

        try {

            while(isRunning){
//                Log.i("dbentry","run : isRunning : = > "+isRunning+" \n queue :=> "+queue);
                if(queue != null && queue.getSize() > 0) {
//                    Log.i("dbentry","run : isRunning : = > "+isRunning+" \n queue :=> "+queue.getSize());
                    try {
                       Log.i("dbentry","inside Message Entry Thread");
                        DbEntryBean dbEntryBean = (DbEntryBean) queue.getMsg();
                        if(dbEntryBean != null) {
                            if(dbEntryBean.getCall_back() != null) {
                                dbEntryBean.getCall_back().notifyChat_Received(dbEntryBean.getFromUri(),dbEntryBean.getToUri(),dbEntryBean.getMimeType(),dbEntryBean.getMessage());
                            }
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
