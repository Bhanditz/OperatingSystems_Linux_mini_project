package com.jannick.oslinux.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jannick.oslinux.ApiHelper;
import com.jannick.oslinux.R;
import com.jannick.oslinux.adapters.KaKuAdapter;
import com.jannick.oslinux.tokens.KaKuToken;
import com.jannick.oslinux.utils.LayoutUtil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        refreshListView();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RotateAnimation rotateAnimation = new RotateAnimation(0, 359, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                rotateAnimation.setRepeatCount(0);
                rotateAnimation.setRepeatMode(Animation.RESTART);
                rotateAnimation.setDuration(1000);
                view.startAnimation(rotateAnimation);
                refreshListView();
            }
        });

    }

    private void refreshListView(){

        AsyncHttpClient client = new AsyncHttpClient();

        client.get(this, ApiHelper.getInstance().getBaseURL() + ApiHelper.getInstance().getAll, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String responsestr = new String(responseBody);
                Gson gson = new Gson();
                KaKuToken kaKuToken = gson.fromJson(responsestr, KaKuToken.class);
                List<KaKuToken.KaKu> list = kaKuToken.getKakus();
                refreshRecyclerView(list);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("parse", "failed device JSON parse");
            }

            @Override
            public void onRetry(int retryNo) {
                super.onRetry(retryNo);
                Toast.makeText(getBaseContext(), "Error while connecting to server", Toast.LENGTH_SHORT);
                return;
            }
        });
    }

    private void refreshRecyclerView(List<KaKuToken.KaKu> list){
        RecyclerView listView = (RecyclerView)findViewById(R.id.kaku_list);
        listView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        listView.setLayoutManager(llm);
        listView.setNestedScrollingEnabled(true);
        listView.setAdapter(new KaKuAdapter(list));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            LayoutUtil.navigateToActivity(this,SettingsActivity.class);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        refreshListView();
    }
}
