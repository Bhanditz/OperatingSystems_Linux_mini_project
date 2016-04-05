package com.jannick.oslinux.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.jannick.oslinux.R;
import com.jannick.oslinux.tokens.KaKuToken;

import java.util.List;

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
        KaKuToken.KaKu token = kaKuTokenList.get(i);
        kakuViewHolder.vId.setText(""+token.getIdletter()+token.getIdnumber());
        kakuViewHolder.vName.setText(token.getLocation());
        kakuViewHolder.vStatus.setChecked(Boolean.parseBoolean(token.getStatus()));
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

        public KaKuViewHolder(View v) {
            super(v);
            vName =  (TextView) v.findViewById(R.id.kaku_text);
            vId = (Button) v.findViewById(R.id.kaku_id);
            vStatus = (SwitchCompat)v.findViewById(R.id.kaku_switch);
        }
    }
}
