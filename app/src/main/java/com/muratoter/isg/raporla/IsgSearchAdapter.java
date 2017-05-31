package com.muratoter.isg.raporla;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.mateware.snacky.Snacky;

/**
 * Created by Murat on 28.04.2017.
 */

public class IsgSearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Kullanicilar> kullanicilar= Collections.emptyList();
    private Activity activity;
    public static boolean durum=false;

    public IsgSearchAdapter(Activity activity,List<Kullanicilar> kullanicilar) {
        this.kullanicilar = kullanicilar;
        this.activity = activity;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView tv_isg_uzman_adi,tv_isg_uzman_mail;
        public LinearLayout ll_list_item;
        public MyViewHolder(View view){
            super(view);
            tv_isg_uzman_adi=(TextView)view.findViewById(R.id.tv_isg_uzman_adi);
            tv_isg_uzman_mail=(TextView)view.findViewById(R.id.tv_isg_uzman_mail);
            ll_list_item=(LinearLayout)view.findViewById(R.id.ll_list_item);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View myView= LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_search_item,parent,false);
        return new MyViewHolder(myView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final MyViewHolder myViewHolder=(MyViewHolder)holder;
        myViewHolder.tv_isg_uzman_adi.setText(kullanicilar.get(position).getKullaniciAdSoyad());
        myViewHolder.tv_isg_uzman_mail.setText(kullanicilar.get(position).getKullaniciEPosta());

        myViewHolder.ll_list_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snacky.builder()
                        .setActivty(activity)
                        .setActionText("Onayla")
                        .setActionClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(view.getContext());
                                String kayit_eden=sharedPreferences.getString("uid","");

                                eslesme_kayit(kullanicilar.get(position).getKullaniciId(),Integer.parseInt(kayit_eden),0);
                            }
                        })
                        .setText(kullanicilar.get(position).getKullaniciAdSoyad()+" ile eşleşmeyi onaylıyor musunuz?")
                        .setDuration(Snacky.LENGTH_INDEFINITE)
                        .info()
                        .show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return kullanicilar.size();
    }
    public void setFilter(ArrayList<Kullanicilar> newList){
        kullanicilar=new ArrayList<>();
        kullanicilar.addAll(newList);
        notifyDataSetChanged();
    }



    //departman sorumlusu bir isg uzmanı ile eşlesme isteğinde bulunuyor
    private void eslesme_kayit(final int isg_uzmani_id, final int dep_sorumlu_id,
                               final int onay) {
        String string_req = "req_eslesme_kayit";



        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.BaseURL+"eslesme.php", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("resp",response);

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        Snacky.builder()
                                .setActivty(activity)
                                .setText("Eşleşme isteği gönderildi.")
                                .setDuration(Snacky.LENGTH_LONG)
                                .success()
                                .show();
                        durum=true;

                    } else {
                        String errorMsg = jObj.getString("error_msg");

                        Snacky.builder()
                                .setActivty(activity)
                                .setText(errorMsg)
                                .setDuration(Snacky.LENGTH_LONG)
                                .warning()
                                .show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(activity.getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(activity.getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();

            }
        }) {

            //geçirilen parametreler
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("isg_uzmani_id", String.valueOf(isg_uzmani_id));
                params.put("dep_sorumlu_id", String.valueOf(dep_sorumlu_id));
                params.put("onay", String.valueOf(onay));


                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, string_req);
    }


}

