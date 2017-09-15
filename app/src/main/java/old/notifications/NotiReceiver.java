package old.notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.rpanic1308.Constants;

/**
 * Created by morot on 08.06.2017.
 */

public class NotiReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent in = new Intent(context, NotificationReceiver.class);
        in.putExtra(Constants.onOrOff, intent.getStringExtra(Constants.onOrOff));
        context.sendBroadcast(in);
    }
}
