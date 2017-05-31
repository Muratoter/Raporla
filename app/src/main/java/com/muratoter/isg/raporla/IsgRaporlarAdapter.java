package com.muratoter.isg.raporla;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.mateware.snacky.Snacky;

/**
 * Created by Murat on 12.05.2017.
 */

public class IsgRaporlarAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Raporlar> raporlar= Collections.emptyList();
    private Activity activity;
    public static boolean durum=false;
    public static int rapor_id=0;

    public IsgRaporlarAdapter(Activity activity,List<Raporlar> raporlar) {
        this.raporlar = raporlar;
        this.activity = activity;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView tv_rapor_adi,tv_rapor_gonderen,tv_rapor_taraf;
        public LinearLayout list_item_rapor;
        public MyViewHolder(View view){
            super(view);
            tv_rapor_adi=(TextView)view.findViewById(R.id.tv_rapor_adi);
            tv_rapor_gonderen=(TextView)view.findViewById(R.id.tv_rapor_gonderen);
            list_item_rapor=(LinearLayout)view.findViewById(R.id.list_item_rapor);
            tv_rapor_taraf=(TextView)view.findViewById(R.id.tv_rapor_taraf);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View myView= LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_isg_uzman_raporlar,parent,false);
        return new MyViewHolder(myView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final MyViewHolder myViewHolder=(MyViewHolder)holder;
        myViewHolder.tv_rapor_adi.setText(raporlar.get(position).getRaporTarihi()+"/"+raporlar.get(position).getKisiAdSoyad());
        myViewHolder.tv_rapor_gonderen.setText(raporlar.get(position).getDepSorumluAdi());

        myViewHolder.list_item_rapor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rapor_id=raporlar.get(position).getRaporId();
                Intent intent=new Intent(activity.getApplicationContext(),AyrintiliRaporActivity.class);
                intent.putExtra("activity_name",activity.getClass().getSimpleName());
                view.getContext().startActivity(intent);
            }
        });
        if(activity.getClass().getSimpleName().equals("UretimSorumlusuActivity"))
            myViewHolder.tv_rapor_taraf.setText("Rapor Sorumlusu : ");

    }

    @Override
    public int getItemCount() {
        return raporlar.size();
    }
}
