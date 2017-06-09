package org.pjsip.pjsua2.app;

import android.util.Log;

import com.myapplication3.Appreference;

import org.pjsip.pjsua2.Buddy;
import org.pjsip.pjsua2.BuddyConfig;
import org.pjsip.pjsua2.BuddyInfo;
import org.pjsip.pjsua2.pjsip_evsub_state;
import org.pjsip.pjsua2.pjsua_buddy_status;

public class MyBuddy extends Buddy
{
    public BuddyConfig cfg;

    public MyBuddy(BuddyConfig config)
    {
	super();
	cfg = config;
    }

    public String getStatusText()
    {
	BuddyInfo bi;

	try {
	    bi = getInfo();
	} catch (Exception e) {
	    return "";
	}
	Log.i("bstate","buri-->"+bi.getUri());
		Log.i("bstate","substate-->"+bi.getSubState());
    Log.i("bstate","state--->"+bi.getPresStatus().getStatus());
		Log.i("bstate","Activity state--->"+bi.getPresStatus().getActivity().toString());
//		Log.i("bstate","Note state--->"+bi.getPresStatus().getNote());
		Appreference.printLog("sipregister", "buri-->"+bi.getUri(), "DEBUG", null);
		Appreference.printLog("sipregister", "substate-->"+bi.getSubState(), "DEBUG", null);
		Appreference.printLog("sipregister", "state--->"+bi.getPresStatus().getStatus(), "DEBUG", null);
		Appreference.printLog("sipregister", "Activity state--->"+bi.getPresStatus().getActivity().toString(), "DEBUG", null);
//		Appreference.printLog("sipregister", "Note state--->"+bi.getPresStatus().getNote(), "DEBUG", null);
	String status = "";
	if (bi.getSubState() == pjsip_evsub_state.PJSIP_EVSUB_STATE_ACTIVE) {
	    if (bi.getPresStatus().getStatus() ==
		pjsua_buddy_status.PJSUA_BUDDY_STATUS_ONLINE)
	    {
//		status = bi.getPresStatus().getStatusText();
//		if (status == null || status.length()==0) {
		    status = "Online";
//		}
	    } else if (bi.getPresStatus().getStatus() ==
		       pjsua_buddy_status.PJSUA_BUDDY_STATUS_OFFLINE)
	    {
		status = "Offline";
	    }else if(bi.getPresStatus().getStatus()==pjsua_buddy_status.PJSUA_BUDDY_STATUS_UNKNOWN){
			status="Unkown";
		}
		else {
		status = "no status";
	    }
	}
	return status;
    }

    @Override
    public void onBuddyState()
    {
	MyApp.observer.notifyBuddyState(this);
    }

}
