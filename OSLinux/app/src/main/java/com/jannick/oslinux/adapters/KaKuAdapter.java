package com.jannick.oslinux.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.jannick.oslinux.ApiHelper;
import com.jannick.oslinux.R;
import com.jannick.oslinux.tokens.KaKuToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Jannick on 5-4-2016.
 */
public class KaKuAdapter extends RecyclerView.Adapter<KaKuAdapter.KaKuViewHolder> {

    private List<KaKuToken.KaKu> kaKuTokenList;

    public KaKuAdapter(List<KaKuToken.KaKu> kaKuTokenList) {
        this.kaKuTokenList = kaKuTokenList;
    }

    @Override
    public int getItemCount() {
        return kaKuTokenList.size();
    }

    @Override
    public void onBindViewHolder(KaKuViewHolder kakuViewHolder, int i) {
        final KaKuToken.KaKu token = kaKuTokenList.get(i);
        kakuViewHolder.vId.setText(""+token.getIdletter()+token.getIdnumber());
        kakuViewHolder.vName.setText(token.getLocation());
        kakuViewHolder.vStatus.setChecked(Boolean.parseBoolean(token.getStatus()));
        Log.d(ApiHelper.TAG,""+token.getId());
        kakuViewHolder.v.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View v) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                AsyncHttpClient client = new AsyncHttpClient();
                                Log.d(ApiHelper.TAG,""+token.getId());
                                String url = ApiHelper.getInstance().getBaseURL() + ApiHelper.getInstance().remove + token.getId();
                                client.get(v.getContext(), url, new AsyncHttpResponseHandler() {
                                    @Override
                                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                                    }

                                    @Override
                                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                                    }
                                });
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                dialog.dismiss();
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setMessage("Do you want to delete this KaKu?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
                return true;
            }
        });
    }

    @Override
    public KaKuViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.list_row_kaku, viewGroup, false);

        return new KaKuViewHolder(itemView);
    }

    public static class KaKuViewHolder extends RecyclerView.ViewHolder {
        protected TextView vName;
        protected Button vId;
        protected SwitchCompat vStatus;
        protected View v;

        public KaKuViewHolder(View v) {
            super(v);
            vName = (TextView) v.findViewById(R.id.kaku_text);
            vId = (Button) v.findViewById(R.id.kaku_id);
            vStatus = (SwitchCompat) v.findViewById(R.id.kaku_switch);
            this.v=v;
        }
    }
}
