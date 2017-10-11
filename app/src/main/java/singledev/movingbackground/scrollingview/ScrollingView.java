package singledev.movingbackground.scrollingview;

/**
 * Created by batman on 11/10/17.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;

public class ScrollingView implements SensorEventListener {

    private static ScrollingView ScrollingView;
    private static SensorManager mSensorManager;
    private static Sensor mSensor;
    private final Context mContext;
    private float smoothedValue;
    private ImageView image_view;
    private int mImageWidth;
    private int mMaxScroll;


    public static synchronized ScrollingView getInstance(Context context) {
        if (ScrollingView == null) {
            ScrollingView = new ScrollingView(context.getApplicationContext());
        }
        return ScrollingView;
    }

    private ScrollingView(Context context) {
        this.mContext = context;
        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

    }

    public ScrollingView setImage(ImageView image, int drawable) {
        image_view = image;
        Bitmap bmp = resizeBitmap(Common.getDeviceHeight(mContext), drawable);
        image_view.setLayoutParams(new HorizontalScrollView.LayoutParams(bmp.getWidth(), bmp.getHeight()));
        image_view.setImageBitmap(bmp);
        mMaxScroll = bmp.getWidth();
        if (image.getParent() instanceof HorizontalScrollView) {
            ((HorizontalScrollView) image.getParent()).setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    return true;
                }
            });
        }
        return ScrollingView;
    }

    public ScrollingView setImage(ImageView image, Bitmap bitmap) {
        image_view = image;
        Bitmap bmp = resizeBitmap(Common.getDeviceHeight(mContext), bitmap);
        image_view.setLayoutParams(new HorizontalScrollView.LayoutParams(bmp.getWidth(), bmp.getHeight()));
        image_view.setImageBitmap(bmp);
        mMaxScroll = bmp.getWidth();
        if (image.getParent() instanceof HorizontalScrollView) {
            ((HorizontalScrollView) image.getParent()).setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    return true;
                }
            });
        }
        return ScrollingView;
    }

    private Bitmap resizeBitmap(int targetH, int drawable) {
        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), drawable);
        mImageWidth = (bitmap.getWidth() * Common.getDeviceHeight(mContext)) / bitmap.getHeight();

        return Bitmap.createScaledBitmap(bitmap, mImageWidth, targetH, true);
    }

    private Bitmap resizeBitmap(int targetH, Bitmap bitmap) {
        mImageWidth = (bitmap.getWidth() * Common.getDeviceHeight(mContext)) / bitmap.getHeight();

        return Bitmap.createScaledBitmap(bitmap, mImageWidth, targetH, true);
    }

    public ScrollingView center() {
        image_view.post(new Runnable() {
            @Override
            public void run() {
                if (mImageWidth > 0) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH)
                        ((HorizontalScrollView) image_view.getParent()).setScrollX(mImageWidth / 4);
                    else
                        ((HorizontalScrollView) image_view.getParent()).setX(mImageWidth / 4);
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH)
                        ((HorizontalScrollView) image_view.getParent()).setScrollX(((HorizontalScrollView) image_view.getParent()).getWidth() / 2);
                    else
                        ((HorizontalScrollView) image_view.getParent()).setX(((HorizontalScrollView) image_view.getParent()).getWidth() / 2);
                }
            }
        });
        return ScrollingView;
    }


    public boolean deviceSupported() {
        return (null != mSensorManager && null != mSensor);
    }

    public void registerListener() {
        if (deviceSupported())
            mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_GAME);
    }

    public void unRegisterListener() {
        if (deviceSupported())
            mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float value = event.values[1];
        value = value * 50;
        smoothedValue = Common.smooth(value, smoothedValue);
        value = smoothedValue;

        int scrollX = ((HorizontalScrollView) image_view.getParent()).getScrollX();
        if (scrollX + value >= mMaxScroll) value = mMaxScroll - scrollX;
        if (scrollX + value <= -mMaxScroll) value = -mMaxScroll - scrollX;
        ((HorizontalScrollView) image_view.getParent()).scrollBy((int) value, 0);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}