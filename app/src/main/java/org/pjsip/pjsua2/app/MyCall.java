package org.pjsip.pjsua2.app;

import android.util.Log;

import com.ase.Appreference;

import org.pjsip.pjsua2.AudioMedia;
import org.pjsip.pjsua2.Call;
import org.pjsip.pjsua2.CallInfo;
import org.pjsip.pjsua2.CallMediaInfo;
import org.pjsip.pjsua2.CallMediaInfoVector;
import org.pjsip.pjsua2.Media;
import org.pjsip.pjsua2.OnCallMediaStateParam;
import org.pjsip.pjsua2.OnCallRxOfferParam;
import org.pjsip.pjsua2.OnCallStateParam;
import org.pjsip.pjsua2.OnCallTransferRequestParam;
import org.pjsip.pjsua2.OnCallTransferStatusParam;
import org.pjsip.pjsua2.OnCallTxOfferParam;
import org.pjsip.pjsua2.VideoPreview;
import org.pjsip.pjsua2.VideoWindow;
import org.pjsip.pjsua2.pjmedia_type;
import org.pjsip.pjsua2.pjsua2;
import org.pjsip.pjsua2.pjsua_call_media_status;

public class MyCall extends Call {
    public VideoWindow vidWin;
    public VideoPreview vidPrev;

    public MyCall(MyAccount acc, int call_id) {
        super(acc, call_id);
        vidWin = null;
    }

    @Override
    public void onCallState(OnCallStateParam prm) {


        Log.i("SipVideo", "onCallState in MyCall 1 : "+swigCPtr);
        try {
            if(this != null && swigCPtr != 0) {
                Log.i("SipVideo", "onCallState in MyCall check with condition");
                Log.i("SipVideo", "onCallState in MyCall" + this);
                MyApp.observer.notifyCallState(this);
            }
		/*CallInfo ci = getInfo();
		if (ci.getState() ==
		    pjsip_inv_state.PJSIP_INV_STATE_DISCONNECTED)
		{
//            Log.i("SipVideo", " delete: b6");
		    this.delete();
		}*/
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

    @Override
    public void onCallMediaState(OnCallMediaStateParam prm) {
        try {
            Log.i("SipVideo", "came to onCallMediaState");
            CallInfo ci;
            try {
                ci = getInfo();
            } catch (Exception e) {
                return;
            }
            Log.i("SipVideo", "onCallMediaState :=> Call Id :" + ci.getId());
            CallMediaInfoVector cmiv = ci.getMedia();

            for (int i = 0; i < cmiv.size(); i++) {
                CallMediaInfo cmi = cmiv.get(i);
                if (cmi.getType() == pjmedia_type.PJMEDIA_TYPE_AUDIO &&
                        (cmi.getStatus() ==
                                pjsua_call_media_status.PJSUA_CALL_MEDIA_ACTIVE ||
                                cmi.getStatus() ==
                                        pjsua_call_media_status.PJSUA_CALL_MEDIA_REMOTE_HOLD)) {
                    // unfortunately, on Java too, the returned Media cannot be
                    // downcasted to AudioMedia
                    Media m = getMedia(i);
                    AudioMedia am = AudioMedia.typecastFromMedia(m);
                    MainActivity.isAudioCall = true;
                    // connect ports
                    try {
                        MyApp.ep.audDevManager().getCaptureDevMedia().
                                startTransmit(am);

                        if (!Appreference.broadcast_call) {
                            am.startTransmit(MyApp.ep.audDevManager().
                                    getPlaybackDevMedia());
                        }

                        Log.i("audio", "My Call OncallMediaState method");
                        Log.i("audio", "My Call OncallMediaState method ci.getId-->" + ci.getId());
                        MainActivity.audioMediaHashMap.put(ci.getId(), am);
                        if (Appreference.context_table.containsKey("callactivity")) {
                            CallActivity callActivity = (CallActivity) Appreference.context_table.get("callactivity");
                            if (callActivity != null && callActivity.recording_start) {
                                Log.i("audio", "My Call OncallMediaState method CallActivity Record Method call");
                                callActivity.Recording(ci.getId(), am);
                            }

                        }
                    } catch (Exception e) {
                        continue;
                    }
                    connectBuddies(ci.getId(), am);

                } else if (cmi.getType() == pjmedia_type.PJMEDIA_TYPE_VIDEO &&
                        cmi.getStatus() ==
                                pjsua_call_media_status.PJSUA_CALL_MEDIA_ACTIVE &&
                        cmi.getVideoIncomingWindowId() != pjsua2.INVALID_ID) {
                    Log.d("HMLog","Video Call Method fire inside true");
                    if(!MainActivity.medreceived && !Appreference.changehost_request_received) {
                        MainActivity.medreceived =true;
                        MainActivity.isAudioCall = false;
                        vidWin = new VideoWindow(cmi.getVideoIncomingWindowId());
                        MainActivity.totalvideoWindows.add(vidWin);
                        if (!Appreference.received_broadcastcall)
                            vidPrev = new VideoPreview(cmi.getVideoCapDev());
                        Log.d("SipVideo", "vidPrev : " + vidPrev + " vidWin : " + vidWin);
                        if (vidPrev != null) {
                            Log.d("SipVideo", "vidPrev 1 : " + vidPrev + " vidWin : " + vidWin);
                            MainActivity.vidPreview = vidPrev;
                        }
                        MyApp.observer.notifyCallMediaState(this);
                    }
                }
            }
            if (!MainActivity.medreceived)
                MyApp.observer.notifyCallMediaState(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void connectBuddies(int id, AudioMedia am_new) {
        Log.i("SipVideo", "came to connectBuddies : id = " + id);
        try {
            for (int j = 0; j < MainActivity.currentCallArrayList.size(); j++) {
                MyCall myCall = MainActivity.currentCallArrayList.get(j);
                Log.i("SipVideo", "myCall.getId() = " + myCall.getId());
                if (myCall.getId() != -1 && myCall.getId() != id) {
                    //				CallInfo ci_renaining_user = null;
                    try {
                        //					ci_renaining_user = myCall.getInfo();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        if (MainActivity.audioMediaHashMap.containsKey(myCall.getId())) {
                            if(!Appreference.broadcast_call) {
                                am_new.startTransmit(MainActivity.audioMediaHashMap.get(myCall.getId()));
                                MainActivity.audioMediaHashMap.get(myCall.getId()).startTransmit(am_new);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //				CallMediaInfoVector cmiv = ci_renaining_user.getMedia();
                    //
                    //				for (int i = 0; i < cmiv.size(); i++) {
                    //					CallMediaInfo cmi = cmiv.get(i);
                    //					if (cmi.getType() == pjmedia_type.PJMEDIA_TYPE_AUDIO &&
                    //							(cmi.getStatus() ==
                    //									pjsua_call_media_status.PJSUA_CALL_MEDIA_ACTIVE ||
                    //									cmi.getStatus() ==
                    //											pjsua_call_media_status.PJSUA_CALL_MEDIA_REMOTE_HOLD))
                    //					{
                    //						// unfortunately, on Java too, the returned Media cannot be
                    //						// downcasted to AudioMedia
                    //						Media m = getMedia(i);
                    //						AudioMedia am = AudioMedia.typecastFromMedia(m);
                    //
                    //						// connect ports
                    //						try {
                    ////							MyApp.ep.audDevManager().getCaptureDevMedia().
                    ////									startTransmit(am);
                    ////							am.startTransmit(MyApp.ep.audDevManager().
                    ////									getPlaybackDevMedia());
                    //
                    //							am_new.	startTransmit(am);
                    //							am.startTransmit(am_new);
                    //						} catch (Exception e) {
                    //							continue;
                    //						}
                    //					}
                    //					else if (cmi.getType() == pjmedia_type.PJMEDIA_TYPE_VIDEO &&
                    //							cmi.getStatus() ==
                    //									pjsua_call_media_status.PJSUA_CALL_MEDIA_ACTIVE &&
                    //							cmi.getVideoIncomingWindowId() != pjsua2.INVALID_ID)
                    //					{
                    //						vidWin = new VideoWindow(cmi.getVideoIncomingWindowId());
                    //						vidPrev = new VideoPreview(cmi.getVideoCapDev());
                    //					}
                    //				}
                }


            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onCallTxOffer(OnCallTxOfferParam prm) {
//        super.onCallTxOffer(prm);
        Log.i("SipVideo", "came to onCallTxOffer ");
    }

    @Override
    public void onCallRxOffer(OnCallRxOfferParam prm) {
//        super.onCallRxOffer(prm);
        Log.i("SipVideo", "came to onCallRxOffer ");
    }

    @Override
    public void onCallTransferRequest(OnCallTransferRequestParam prm) {
        super.onCallTransferRequest(prm);
        Log.i("changehost","onCallTransferRequest");
    }

    @Override
    public void onCallTransferStatus(OnCallTransferStatusParam prm) {
        super.onCallTransferStatus(prm);
        Log.i("changehost","onCallTransferStatus");
    }
}
