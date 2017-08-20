package org.rpanic1308.recordingActivity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.View;

/**
 * Created by Team_ on 20.08.2017.
 */

public class BitmapSlider {

    static Bitmap bmp1, bmp2;

    public void createBitmaps(Activity activity){

        View root = activity.getWindow().getDecorView().findViewById(android.R.id.content);
        root.setDrawingCacheEnabled(true);
        Bitmap bmp = root.getDrawingCache();

        int splitYCoord = bmp.getHeight()/2;
        bmp1 = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), splitYCoord);
        bmp2 = Bitmap.createBitmap(bmp, 0, splitYCoord, bmp.getWidth(), bmp.getHeight() - splitYCoord);

    }

}
