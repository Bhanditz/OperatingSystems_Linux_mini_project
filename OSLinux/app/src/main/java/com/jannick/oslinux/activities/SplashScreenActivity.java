package com.jannick.oslinux.activities;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.Image;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.jannick.oslinux.ApiHelper;
import com.jannick.oslinux.R;
import com.jannick.oslinux.utils.BlurTransformation;
import com.jannick.oslinux.utils.LayoutUtil;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import jp.wasabeef.picasso.transformations.ColorFilterTransformation;
import jp.wasabeef.picasso.transformations.gpu.BrightnessFilterTransformation;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash_screen);

        ImageView mBlurredImage = (ImageView)findViewById(R.id.splashscreen_blur);

        Picasso.with(this)
                .load(R.drawable.illuminati_ii)
                .transform(new BlurTransformation(15))
                .into(mBlurredImage);

        TextView textView = (TextView)findViewById(R.id.splashscreen_text);
        textView.setText(ApiHelper.getInstance().getRandomQuote() + "\n- Bob Ross");

        textView.setVisibility(View.VISIBLE);
        textView.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.animation_splash));

        Thread background = new Thread() {
            public void run() {

                try {
                    // Thread will sleep for 0.5 seconds
                    sleep(5000);

                    // After 1 seconds redirect to another intent
                    loadActivity();

                    //Remove activity
                    finish();

                } catch (Exception e) {

                }
            }
        };

        // start thread
        background.start();
    }

    private void loadActivity(){
            LayoutUtil.navigateToActivity(this, MainActivity.class);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
