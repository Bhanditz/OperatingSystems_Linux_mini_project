package com.jannick.oslinux.activities;

import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jannick.oslinux.ApiHelper;
import com.jannick.oslinux.R;
import com.jannick.oslinux.tokens.LuxToken;
import com.jannick.oslinux.utils.LayoutUtil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cz.msebera.android.httpclient.Header;

public class SettingsActivity extends AppCompatActivity {

    private EditText ip;
    private EditText port;
    private TextInputLayout ipLayout;
    private TextInputLayout portLayout;
    private DiscreteSeekBar discreteSeekBar;

    private AsyncHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        client = new AsyncHttpClient();

        ip = (EditText)findViewById(R.id.input_ip);
        port = (EditText)findViewById(R.id.input_port);
        ipLayout = (TextInputLayout)findViewById(R.id.input_ip_layout);
        portLayout = (TextInputLayout)findViewById(R.id.input_port_layout);

        discreteSeekBar = (DiscreteSeekBar)findViewById(R.id.luxInput);

        setDisceteSeekBarValue();

        if(ApiHelper.getInstance().getIpAdress() != null)
            ip.setText(ApiHelper.getInstance().getIpAdress());
        if(ApiHelper.getInstance().getPort() != null)
            port.setText(ApiHelper.getInstance().getPort());


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            validateInfo();
        }

        return super.onOptionsItemSelected(item);
    }

    private static final Pattern PARTIAl_IP_ADDRESS =
            Pattern.compile("^((25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[0-9])\\.){0,3}" +
                    "((25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[0-9])){0,1}$");

    private void validateInfo(){

        boolean isValid = true;

        if(!(PARTIAl_IP_ADDRESS.matcher(ip.getText().toString()).matches() && ip.getText().toString().length()>6)||ip.getText().toString()=="") {
            ipLayout.setErrorEnabled(true);
            ipLayout.setError("Invalid IP");
            isValid = false;
        }
        else
        {
            ipLayout.setErrorEnabled(false);
        }

        if(!port.getText().toString().isEmpty())
        {
            if(Integer.parseInt(port.getText().toString()) > 65535){
                portLayout.setErrorEnabled(true);
                portLayout.setError("Invalid Port");
                isValid = false;
            }
            else
            {
                portLayout.setErrorEnabled(false);
            }
        }
        else
        {
            portLayout.setErrorEnabled(true);
            portLayout.setError("Please enter a port");
            isValid = false;
        }

        if(isValid){
            ApiHelper.getInstance().setBaseURL(ip.getText().toString(), port.getText().toString());
            String url = ApiHelper.getInstance().getBaseURL() + ApiHelper.getInstance().luxUpdate + discreteSeekBar.getProgress();
            Log.d(ApiHelper.TAG,url);

            client.get(this, url, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                }

                @Override
                public void onRetry(int retryNo) {
                    super.onRetry(retryNo);
                    client.cancelAllRequests(true);
                }
            });

            onBackPressed();
        }

    }

    private void setDisceteSeekBarValue(){

        client.get(this, ApiHelper.getInstance().getBaseURL() + ApiHelper.getInstance().get, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Gson gson = new Gson();
                String response = new String(responseBody);
                if(response != null)
                    if(!response.isEmpty()){
                        LuxToken token = gson.fromJson(response,LuxToken.class);
                        if(token != null){
                            discreteSeekBar.setProgress(token.getLuxvalue());
                        }
                        Log.d(ApiHelper.TAG, response);
                    }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d(ApiHelper.TAG,"failed to recieve lux value",error);
            }

            @Override
            public void onRetry(int retryNo) {
                super.onRetry(retryNo);
                client.cancelAllRequests(true);
            }
        });
    }

}
