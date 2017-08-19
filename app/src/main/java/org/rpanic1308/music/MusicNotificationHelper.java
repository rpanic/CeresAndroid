package org.rpanic1308.music;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by Team_ on 30.03.2017.
 */

public class MusicNotificationHelper extends Activity {
    private MusicNotificationHelper ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        ctx = this;
        String action = (String) getIntent().getExtras().get("DO");
        if (action.equals("radio")) {
            System.out.println("Radio");
        } else if (action.equals("volume")) {
            System.out.println("Volume");
        } else if (action.equals("reboot")) {
            //Your code
        } else if (action.equals("top")) {
            System.out.println("Top");
        } else if (action.equals("app")) {
            System.out.println("App");
        }

        if (!action.equals("reboot"))
            finish();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }
}
