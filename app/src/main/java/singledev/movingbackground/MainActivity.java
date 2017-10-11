package singledev.movingbackground;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import singledev.movingbackground.scrollingview.ScrollingView;

public class MainActivity extends AppCompatActivity {
    private ScrollingView scrollingView;
    private ImageView background;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        background = (ImageView) findViewById(R.id.bg);
        scrollingView = ScrollingView.getInstance(this).setImage(background,R.drawable.bck).center();

    }

    @Override
    protected void onResume() {
        super.onResume();
        scrollingView.registerListener();
    }

    @Override
    protected void onStop() {
        super.onStop();
        scrollingView.unRegisterListener();
    }
}
