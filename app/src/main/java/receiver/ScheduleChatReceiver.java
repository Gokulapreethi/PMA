package receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.ase.Appreference;
import com.ase.DB.VideoCallDataBase;
import com.ase.chat.ChatActivity;
import com.ase.chat.ChatBean;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by vignesh on 2/24/2017.
 */
public class ScheduleChatReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            if (intent != null) {
                String signalid = intent.getExtras().getString("Signalid");
                String message = intent.getExtras().getString("message");
                ChatBean bean = new ChatBean();
                if(signalid != null) {
    //                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss a");
    //                String currentDateandTime = sdf.format(new Date());
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
                    String dateforrow = dateFormat.format(new Date());
                    Log.d("TagValues", "currentDateandTime    :   " + dateforrow);
                    message = message.split(",")[0];
                    if(message.equalsIgnoreCase("scheduled")) {
                        VideoCallDataBase.getDB(context).ChatMsg_StatusUpdate(signalid, dateforrow);
                    }
                    else {
                        bean = VideoCallDataBase.getDB(context).getChatBean(signalid);
                        VideoCallDataBase.getDB(context).ChatMsgDelete(signalid);
                    }

                }
                 else {
                    Log.d("TagValues", "Not updated");
                }

                if (Appreference.context_table.containsKey("chatactivity")) {
                    final ChatActivity chatActivity = (ChatActivity) Appreference.context_table.get("chatactivity");
                    if (chatActivity != null) {
                        Log.i("chat ", "MA chatActivity!=null ");
                        Log.i("chat ", "MA images_type check if ");
                        if(message.equalsIgnoreCase("scheduled")) {
                            bean = VideoCallDataBase.getDB(context).getChatBean(signalid);
                            chatActivity.notifyScheduleReferesh(bean);
                        }
                        else {
                            Log.i("chat ", "MA images_type check if "+bean.getChatname().trim());
                            chatActivity.notifyTimeReferesh(bean);
                        }

                    }

    //                if (isApplicationBroughtToBackground()) {
    //                    ChatBean cb = new ChatBean();
    //                    Intent intent1 = new Intent(context, ChatAlert.class);
    //                    intent1.putExtra("hostname", "chat");
    //                    intent1.putExtra("userName", cb.getUsername());
    //                    context.startActivity(intent1);
    //
    //                }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
