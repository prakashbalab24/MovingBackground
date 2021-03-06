package singledev.movingbackground.scrollingview;

/**
 * Created by batman on 11/10/17.
 */

import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.view.Display;
import android.view.WindowManager;
class Common {

    static float smooth(float input, float output) {
        float ALPHA = 0.2f;
        return (int) (output + ALPHA * (input - output));
    }

    static int getDeviceHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            display.getSize(size);
        } else {
            display.getHeight();
        }
        return size.y;

    }

}