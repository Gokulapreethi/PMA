package Services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.ase.Appreference;

import org.pjsip.pjsua2.app.MainActivity;

/**
 * Created by Amuthan on 08/12/2016.
 */
public class ScreenReceiver extends BroadcastReceiver {

    public static boolean wasScreenOn = true;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            // DO WHATEVER YOU NEED TO DO HERE
            Log.i("Screen", "ScreenReceiver SCREEN TURNED OFF");
            wasScreenOn = false;
        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            // AND DO WHATEVER YOU NEED TO DO HERE
            Log.i("Screen", "ScreenReceiver SCREEN TURNED ON");
            wasScreenOn = true;

            if (MainActivity.app != null && MainActivity.account != null && MainActivity.accCfg != null) {
                Appreference.printLog("SipRegister", "Sip Re-Register for ScreenReceiver SCREEN TURNED ON", "DEBUG", null);
                if (MainActivity.isPresenceReregister) {
                    MainActivity.reRegister_onAppResume();
                }
            }
        }
    }
}
