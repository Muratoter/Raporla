package com.muratoter.isg.raporla;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.mateware.snacky.Snacky;

public class UretimSorumlusuActivity extends AppCompatActivity {
    private CardView cw_eslestirme, cv_onay_durum;
    private TextView tv_onay_yazisi, tv_person_name;
    private LinearLayout ll_person_settings;
    private FloatingActionButton fab_rapor;
    private boolean fab_durum=false;
    public static int isg_uzman_id=0;

    //raporlar
    private TextView tv_dep_sorumlu_rapor_yok;
    private RecyclerView rv_dep_sorumlu_raporlar;
    private List<Raporlar> raporlarList=new ArrayList<>();
    private IsgRaporlarAdapter raporlarAdapter;
    private ImageView iv_dep_sorumlusu_refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uretim_sorumlusu);
        cw_eslestirme = (CardView) findViewById(R.id.cw_eslestirme);
        cv_onay_durum = (CardView) findViewById(R.id.cv_onay_durum);
        tv_onay_yazisi = (TextView) findViewById(R.id.tv_onay_yazisi);
        tv_person_name = (TextView) findViewById(R.id.tv_person_name);
        ll_person_settings = (LinearLayout) findViewById(R.id.ll_person_settings);
        fab_rapor = (FloatingActionButton) findViewById(R.id.fab_rapor);
        tv_dep_sorumlu_rapor_yok=(TextView)findViewById(R.id.tv_dep_sorumlu_rapor_yok);
        rv_dep_sorumlu_raporlar=(RecyclerView)findViewById(R.id.rv_dep_sorumlu_raporlar);
        iv_dep_sorumlusu_refresh=(ImageView)findViewById(R.id.iv_dep_sorumlusu_refresh);



        ll_person_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SessionEndFragment fragment=new SessionEndFragment();
                fragment.show(getSupportFragmentManager(),"Fragment");
            }
        });

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        final String dep_sorumlu_id = sharedPreferences.getString("uid", "");
        String dep_sorumlu_adi = sharedPreferences.getString("name", "");
        tv_person_name.setText(dep_sorumlu_adi);

        eslesme_durumu(Integer.parseInt(dep_sorumlu_id));

        cw_eslestirme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), IsgUzmaniAraActivity.class);
                startActivity(intent);
            }
        });


        fab_rapor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(fab_durum)
                    startActivity(new Intent(getApplicationContext(), RaporOlusturActivity.class));
                else
                    Toast.makeText(getApplicationContext(), "Rapor oluşturmak için bir İSG Uzmanı ile eşleşmiş olmanız gerekmektedir.", Toast.LENGTH_LONG).show();
            }
        });



        //Departman sorumlusu raporları getir
        RecyclerView.LayoutManager lm = new LinearLayoutManager(getApplicationContext());
        raporGetir(Integer.parseInt(dep_sorumlu_id));
        raporlarAdapter=new IsgRaporlarAdapter(this,raporlarList);
        rv_dep_sorumlu_raporlar.setLayoutManager(lm);
        rv_dep_sorumlu_raporlar.setHasFixedSize(true);
        rv_dep_sorumlu_raporlar.setAdapter(raporlarAdapter);

        iv_dep_sorumlusu_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                raporlarList.clear();
                raporGetir(Integer.parseInt(dep_sorumlu_id));
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (IsgSearchAdapter.durum) {
            cw_eslestirme.setVisibility(View.INVISIBLE);
        }
    }


    private void eslesme_durumu(final int dep_sorumlu_id) {

        String string_req = "req_eslesme_durum";


        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.BaseURL + "eslesme_durum.php", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("rpnse", response);

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        boolean eslesme = jObj.getBoolean("eslesme");
                        if (!eslesme) { //kullanici eslesme yapmamışsa
                            cw_eslestirme.setVisibility(View.VISIBLE);
                        } else { //eslesme var onay kontrolü
                            Log.d("cikti", response);
                            boolean onay = jObj.getBoolean("onay");
                            JSONObject uzman = jObj.getJSONObject("uzman");
                            String uzman_adsoyad = uzman.getString("adsoyad");
                            String uzman_eposta = uzman.getString("eposta");
                            isg_uzman_id=uzman.getInt("isg_uzman_id");

                            if (onay) { //gönderilen istek onaylanmış
                                tv_onay_yazisi.setText("İsg uzmanı '" + uzman_adsoyad + "' ile eşleşme yapılmıştır.");
                                cv_onay_durum.setVisibility(View.VISIBLE);
                                fab_durum=true;
                            } else {
                                tv_onay_yazisi.setText("'" + uzman_adsoyad + "' tarafından onay bekleniyor...");
                                cv_onay_durum.setVisibility(View.VISIBLE);
                                fab_durum=false;
                            }
                        }

                    } else {
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(UretimSorumlusuActivity.this, "" + errorMsg, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("dep_sorumlu_id", String.valueOf(dep_sorumlu_id));

                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, string_req);
    }



    private void raporGetir(final int dep_sorumlu_id) {
        String string_req = "req_dep_sorumlu_raporlari";

        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.BaseURL + "dep_sorumlusu_icin_raporlar.php", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("dep_sorumlusu_raporlari", response);

                try {
                    JSONObject jObj = new JSONObject(response);
                    JSONArray raporArray = jObj.getJSONArray("raporlar");
                    int count = 0;
                    while (count < raporArray.length()) {
                        JSONObject rapor = raporArray.getJSONObject(count);
                        count++;
                        //servisten dönen bilgileri kullanma
                        boolean error = rapor.getBoolean("error");
                        if (!error) {
                            int rapor_id=rapor.getInt("rapor_id");
                            int dep_sorumlu_id=rapor.getInt("dep_sorumlu_id");
                            String dep_sorumlu_adsoyad=rapor.getString("dep_sorumlu_adsoyad");
                            int isg_uzman_id=rapor.getInt("isg_uzman_id");
                            String rapor_tarihi=rapor.getString("rapor_tarihi");
                            String kazazede_adsoyad=rapor.getString("kazazede_adsoyad");
                            String olay_tarihi=rapor.getString("olay_tarihi");
                            String olay_oldugu_yer=rapor.getString("olay_oldugu_yer");
                            String olay_oldugu_departman=rapor.getString("olay_oldugu_departman");
                            String olay_sebebi=rapor.getString("olay_sebebi");
                            String hasar_goren_yer=rapor.getString("hasar_goren_yer");
                            String olay_kisa_aciklama=rapor.getString("olay_kisa_aciklama");
                            String olaya_sahit_kisi_adsoyad=rapor.getString("olaya_sahit_kisi_adsoyad");

                            Raporlar raporum=new Raporlar(rapor_id,dep_sorumlu_id,dep_sorumlu_adsoyad,isg_uzman_id,rapor_tarihi,kazazede_adsoyad,olay_tarihi
                                    ,olay_oldugu_yer,olay_oldugu_departman,olay_sebebi,hasar_goren_yer,olay_kisa_aciklama,olaya_sahit_kisi_adsoyad);


                            raporlarList.add(raporum);
                            raporlarAdapter.notifyDataSetChanged();
                        } else {
                            String errorMsg = jObj.getString("error_msg");
                            Snacky.builder()
                                    .setActivty(UretimSorumlusuActivity.this)
                                    .setText(errorMsg)
                                    .setDuration(Snacky.LENGTH_LONG)
                                    .warning()
                                    .show();
                        }

                    }
                    if(raporArray.length()==0){
                        tv_dep_sorumlu_rapor_yok.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("dep_sorumlu_id", String.valueOf(dep_sorumlu_id));
                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, string_req);
    }



}
