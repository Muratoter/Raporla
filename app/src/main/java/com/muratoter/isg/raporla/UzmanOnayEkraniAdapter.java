package com.muratoter.isg.raporla;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.mateware.snacky.Snacky;

/**
 * Created by Murat on 1.05.2017.
 */

public class UzmanOnayEkraniAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Eslesmeler> eslesmeler= Collections.emptyList();
    private Activity activity;

    public UzmanOnayEkraniAdapter(Activity activity, List<Eslesmeler> eslesmeler){
        this.activity=activity;
        this.eslesmeler=eslesmeler;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView tv_onay_gonderen_adi,tv_onay_gonderen_eposta,tv_onay_durum;
        public LinearLayout ll_list_onay;
        public MyViewHolder(View itemView) {
            super(itemView);
            tv_onay_gonderen_adi=(TextView)itemView.findViewById(R.id.tv_onay_gonderen_adi);
            tv_onay_gonderen_eposta=(TextView)itemView.findViewById(R.id.tv_onay_gonderen_eposta);
            tv_onay_durum=(TextView)itemView.findViewById(R.id.tv_onay_durum);
            ll_list_onay=(LinearLayout)itemView.findViewById(R.id.ll_list_onay);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_uzman_onay_ekran,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder,final int position) {
        final MyViewHolder myViewHolder=(MyViewHolder)holder;
        myViewHolder.tv_onay_gonderen_eposta.setText(eslesmeler.get(position).getDepSorumluEposta());
        myViewHolder.tv_onay_gonderen_adi.setText(eslesmeler.get(position).getDepSorumluAdSoyadi());
        myViewHolder.tv_onay_durum.setText(eslesmeler.get(position).getEslesmeDurum());

        if(myViewHolder.tv_onay_durum.getText()=="Onay Bekliyor")
            myViewHolder.tv_onay_durum.setTextColor(activity.getResources().getColor(R.color.text_onay_bekliyor));
        else
            myViewHolder.tv_onay_durum.setTextColor(activity.getResources().getColor(R.color.text_onay_verilmis));

        myViewHolder.ll_list_onay.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                if(myViewHolder.tv_onay_durum.getText()=="Onay Bekliyor"){
                    Snacky.builder()
                            .setActivty(activity)
                            .setActionText("Onayla")
                            .setActionClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(view.getContext());
                                    String isg_uzman_id=sharedPreferences.getString("uid","");
                                    eslesme_onay(Integer.parseInt(isg_uzman_id),eslesmeler.get(position).getDepSorumluId());

                                }
                            })
                            .setText(eslesmeler.get(position).getDepSorumluAdSoyadi()+" ile eşleşmeyi onaylıyor musunuz?")
                            .setDuration(Snacky.LENGTH_INDEFINITE)
                            .info()
                            .show();

                }
                else {
                    Snackbar snackbar=Snackbar.make(view,eslesmeler.get(position).getDepSorumluAdSoyadi()+" ile zaten eşleşmiş bulunuyorsunuz.",Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }

                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return eslesmeler.size();
    }




    private void eslesme_onay(final int isg_uzmani_id,final int dep_sorumlu_id) {
        String string_req = "req_eslesme_onay";



        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.BaseURL+"eslesme_onay.php", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("resp",response);

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        Snacky.builder()
                                .setActivty(activity)
                                .setText("Eşleşme isteği onaylandı. Kişiyle raporlaşabilirsiniz.")
                                .setDuration(Snacky.LENGTH_LONG)
                                .success()
                                .show();
                        notifyDataSetChanged();
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

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("isg_uzman_id", String.valueOf(isg_uzmani_id));
                params.put("dep_sorumlu_id", String.valueOf(dep_sorumlu_id));
                return params;
            }

        };
        AppController.getInstance().addToRequestQueue(strReq, string_req);
    }
}
