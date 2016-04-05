package com.jannick.oslinux.activities;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
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
        EditText ip = (EditText)findViewById(R.id.input_ip);
        EditText port = (EditText)findViewById(R.id.input_port);


        if(!(PARTIAl_IP_ADDRESS.matcher(ip.getText().toString()).matches() && ip.getText().toString().length()>6)||ip.getText().toString()=="") {
            Toast.makeText(this, "Invalid IP", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!port.getText().toString().isEmpty())
            if(Integer.parseInt(port.getText().toString()) > 65535){
                Toast.makeText(this, "Invalid Port", Toast.LENGTH_SHORT).show();
                return;
            }

        ApiHelper.getInstance().setBaseURL(ip.getText().toString(),port.getText().toString());
        onBackPressed();
    }

}
