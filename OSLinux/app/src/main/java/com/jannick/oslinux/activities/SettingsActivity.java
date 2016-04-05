package com.jannick.oslinux.activities;

import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.jannick.oslinux.ApiHelper;
import com.jannick.oslinux.R;
import com.jannick.oslinux.utils.LayoutUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SettingsActivity extends AppCompatActivity {

    private EditText ip;
    private EditText port;
    private TextInputLayout ipLayout;
    private TextInputLayout portLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ip = (EditText)findViewById(R.id.input_ip);
        port = (EditText)findViewById(R.id.input_port);
        ipLayout = (TextInputLayout)findViewById(R.id.input_ip_layout);
        portLayout = (TextInputLayout)findViewById(R.id.input_port_layout);

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
            ApiHelper.getInstance().setBaseURL(ip.getText().toString(),port.getText().toString());
            onBackPressed();
        }

    }

}
