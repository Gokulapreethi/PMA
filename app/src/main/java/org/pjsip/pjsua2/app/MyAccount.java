package org.pjsip.pjsua2.app;

import android.util.Log;

import com.ase.Appreference;

import org.pjsip.pjsua2.Account;
import org.pjsip.pjsua2.AccountConfig;
import org.pjsip.pjsua2.BuddyConfig;
import org.pjsip.pjsua2.OnIncomingCallParam;
import org.pjsip.pjsua2.OnIncomingSubscribeParam;
import org.pjsip.pjsua2.OnInstantMessageParam;
import org.pjsip.pjsua2.OnInstantMessageStatusParam;
import org.pjsip.pjsua2.OnRegStateParam;

import java.util.ArrayList;

public class MyAccount extends Account {
    public ArrayList<MyBuddy> buddyList = new ArrayList<MyBuddy>();
    public AccountConfig cfg;

    MyAccount(AccountConfig config) {
        super();
        cfg = config;
    }

    public MyBuddy addBuddy(BuddyConfig bud_cfg) {
    /* Create Buddy */
        MyBuddy bud = new MyBuddy(bud_cfg);
        try {
            bud.create(this, bud_cfg);
        } catch (Exception e) {
            bud.delete();
            bud = null;
        }

        if (bud != null) {
            buddyList.add(bud);
            if (bud_cfg.getSubscribe())
                try {
                    Log.i("subscribe", "subscribePresence--true");
                    Log.i("subscribe", "buddy uri--" + bud_cfg.getUri());
                    bud.subscribePresence(true);
                } catch (Exception e) {
                }
        }

        return bud;
    }

    public void delBuddy(MyBuddy buddy) {
        buddyList.remove(buddy);
        buddy.delete();
    }

    public void delBuddy(int index) {
        MyBuddy bud = buddyList.get(index);
        buddyList.remove(index);
        bud.delete();
    }

    @Override
    public void onRegState(OnRegStateParam prm) {
        MyApp.observer.notifyRegState(prm.getCode(), prm.getReason(),
                prm.getExpiration());
    }

    @Override
    public void onIncomingCall(OnIncomingCallParam prm) {
        Log.i("SipVideo", "======== Incoming call ======== ");
       /* if (MainActivity.currentCallArrayList.size() != 0) {
            MainActivity.currentCallArrayList.clear();
            MainActivity.currentCall = null;
        }*/
        MyCall call = new MyCall(this, prm.getCallId());
/*CallOpParam callOpParam = new CallOpParam();
		callOpParam.setStatusCode(pjsip_status_code.PJSIP_SC_BUSY_HERE); */

        MyApp.observer.notifyIncomingCall(call);
    }

//    @Override
//    public void onInstantMessage(OnInstantMessageParam prm)
//    {
//		Log.i("chat", "======== onInstantMessage ======== ");
//		Log.i("chat", "From     : " + prm.getFromUri());
//		Log.i("chat", "To       : " + prm.getToUri());
//		Log.i("chat", "Contact  : " + prm.getContactUri());
//		Log.i("chat", "Mimetype : " + prm.getContentType());
//		Log.i("chat", "Body     : " + prm.getMsgBody());
//		MainActivity.notifyChatReceived(prm.getFromUri(),prm.getToUri(),prm.getContentType(),prm.getMsgBody());
//    }

    @Override
    public void onInstantMessage(OnInstantMessageParam prm) {
        Log.i("chat", "======== onInstantMessage ======== ");
        Log.i("chat", "From     : " + prm.getFromUri());
        Log.i("chat", "To       : " + prm.getToUri());
        Log.i("chat", "Contact  : " + prm.getContactUri());
        Log.i("chat", "Mimetype : " + prm.getContentType());
        super.onInstantMessage(prm);
        Log.i("chat", "Body     : " + prm.getMsgBody());
        Appreference.printLog("SipMessageReceived", prm.getMsgBody(), "DEBUG", null);
//			MainActivity.notifyChatReceived(prm.getFromUri(), prm.getToUri(), prm.getContentType(), prm.getMsgBody());
        MyApp.observer.notifyChatReceived(prm.getFromUri(), prm.getToUri(), prm.getContentType(), prm.getMsgBody());

    }

    @Override
    public void onInstantMessageStatus(OnInstantMessageStatusParam prm) {
        Log.i("chat", "======== onInstantMessageStatus ======== ");
        Log.i("chat", "Reason     : " + prm.getReason());
        Log.i("chat", "To       : " + prm.getToUri());
        Log.i("chat", "Body     : " + prm.getMsgBody());
        Log.i("chat", "code     : " + prm.getCode().swigValue());
        super.onInstantMessageStatus(prm);
        Appreference.printLog("SipMessageStatus", "Reason-->" + prm.getReason() + " To--->" + prm.getToUri() + " Body-->" + prm.getMsgBody(), "DEBUG", null);
        MyApp.observer.notifySipMessageStatus(prm.getReason(), prm.getToUri(), prm.getMsgBody(),prm.getCode().swigValue());
    }

    @Override
    public void onIncomingSubscribe(OnIncomingSubscribeParam prm) {
        super.onIncomingSubscribe(prm);
        Log.i("subscribe", "MyAccount class onInComingSubscribe");
        Log.i("subscribe", "MyAccount class onInComingSubscribe from uri-->" + prm.getFromUri());
        try {
            Log.i("subscribe", "MyAccount class onInComingSubscribe from buddyList.size() is === -->" + buddyList.size());
            for (int i = 0; i < buddyList.size(); i++) {
                String name = MainActivity.account.buddyList.get(i).cfg.getUri();
                Log.i("subscribe", "MyAccount class onInComingSubscribe from name-->" + name);
                if (name.equalsIgnoreCase(prm.getFromUri())) {
                    Log.i("subscribe", "name-->" + name);
                    Log.i("subscribe", "name-->" + prm.getFromUri());
                    MyBuddy myBuddy = new MyBuddy(buddyList.get(i).cfg);
                    myBuddy.subscribePresence(true);
                    break;
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
