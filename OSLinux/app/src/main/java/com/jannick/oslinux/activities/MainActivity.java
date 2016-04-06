package com.jannick.oslinux.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.EditText;
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

public class MainActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener{

    private SwipeRefreshLayout refreshLayout;
    private AppBarLayout appBarLayout;
    private CoordinatorLayout coordinatorLayout;
    private List<KaKuToken.KaKu> list;
    private AsyncHttpClient client;
    private final Context context = this;
    SwipeRefreshLayout.OnRefreshListener swipeRefreshListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        client = new AsyncHttpClient();

        list = new ArrayList<KaKuToken.KaKu>();

        swipeRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d(ApiHelper.TAG,"Refreshing SwipeRefreshLayout");
                refreshListView();
            }
        };

        coordinatorLayout = (CoordinatorLayout)findViewById(R.id.coordinator_layout);

        appBarLayout = (AppBarLayout)findViewById(R.id.app_bar);

        refreshLayout = (SwipeRefreshLayout)findViewById(R.id.refresh_layout);
        refreshLayout.setOnRefreshListener(swipeRefreshListener);
        refreshLayout.setNestedScrollingEnabled(false);

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
                createAddKakuDialog();
            }
        });

    }

    private void refreshListView(){
        if(client!=null) {
            String url = ApiHelper.getInstance().getBaseURL() + ApiHelper.getInstance().getAll;
            Log.d(ApiHelper.TAG,url);
            client.get(this, url, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    String responsestr = new String(responseBody);
                    Gson gson = new Gson();
                    KaKuToken kaKuToken = gson.fromJson(responsestr, KaKuToken.class);
                    list = kaKuToken.getKakus();
                    refreshRecyclerView(list);
                    refreshLayout.setRefreshing(false);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Log.d("parse", "failed device JSON parse");
                    refreshLayout.setRefreshing(false);
                }

                @Override
                public void onRetry(int retryNo) {
                    super.onRetry(retryNo);
                    client.cancelAllRequests(true);
                    refreshLayout.setRefreshing(false);
                }
            });
        }
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

    private void createAddKakuDialog(){

        LayoutInflater inflater = LayoutInflater.from(this);

        View v = inflater.inflate(R.layout.dialog_add_kaku, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(v);

        final EditText f1 = ((EditText) v.findViewById(R.id.input_id));
        final EditText f2 = ((EditText) v.findViewById(R.id.input_location));
        final EditText f3 = ((EditText) v.findViewById(R.id.input_group_number));
        final EditText f4 = ((EditText) v.findViewById(R.id.input_group_char));

        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                int kakuID = 0, number = 0;
                String location = "No location defined", character = "A";
                boolean isValid = true;
                if (!f1.getText().toString().isEmpty() &&
                        !f2.getText().toString().isEmpty() &&
                        !f3.getText().toString().isEmpty() &&
                        !f4.getText().toString().isEmpty()) {
                    kakuID = Integer.parseInt(f1.getText().toString());
                    location = (f2.getText().toString());
                    number = Integer.parseInt(f3.getText().toString());
                    character = (f4.getText().toString());
                } else {
                    isValid = false;
                }

                if (isValid)
                    addKaku(kakuID,
                            location,
                            character,
                            number);
                else
                    Toast.makeText(getApplicationContext(), "Wrong info", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    private void addKaku(int id, String location, String groupChar, int groupNumber){
        String url =  ApiHelper.getInstance().getBaseURL()
                + ApiHelper.getInstance().add
                + id + "/"
                + groupChar + "/"
                + groupNumber + "/"
                + location + "/";
        Log.d(ApiHelper.TAG,url);
        client.get(this, url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Snackbar.make(coordinatorLayout, "Succesfully added KaKu", Snackbar.LENGTH_SHORT).show();
                swipeRefreshListener.onRefresh();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Snackbar.make(coordinatorLayout, "Action failed", Snackbar.LENGTH_SHORT).show();
            }
        });
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

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        //The Refresh must be only active when the offset is zero :
        refreshLayout.setEnabled(i == 0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        appBarLayout.addOnOffsetChangedListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        appBarLayout.removeOnOffsetChangedListener(this);
    }
}
