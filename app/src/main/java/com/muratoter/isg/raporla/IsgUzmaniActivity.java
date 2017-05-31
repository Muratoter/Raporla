package com.muratoter.isg.raporla;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.mateware.snacky.Snacky;

public class IsgUzmaniActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<Eslesmeler> eslesmelerList = new ArrayList<>();
    private UzmanOnayEkraniAdapter onayEkraniAdapter;
    private TextView tv_eslesme_yok,person_tv;
    private LinearLayout ll_isg_person_set;
    private ImageView iv_onay_refresh;

    //isg uzmani için raporlar
    private RecyclerView recyclerViewRaporlar;
    private List<Raporlar> raporlarList=new ArrayList<>();
    private IsgRaporlarAdapter raporlarAdapter;
    private TextView isg_onay_ekran_rapor_yok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_isg_uzmani);
        tv_eslesme_yok=(TextView)findViewById(R.id.isg_onay_ekran_eslesme_yok);
        isg_onay_ekran_rapor_yok=(TextView)findViewById(R.id.isg_onay_ekran_rapor_yok);
        person_tv=(TextView)findViewById(R.id.person_tv);
        ll_isg_person_set=(LinearLayout)findViewById(R.id.isg_person_set);
        iv_onay_refresh=(ImageView)findViewById(R.id.iv_onay_refresh);

        ll_isg_person_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SessionEndFragment fragment=new SessionEndFragment();
                fragment.show(getSupportFragmentManager(),"Fragment");
            }
        });
        //sisteme giriş yapan kişinin bilgilerini shared preferences dan getir.
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        final String isg_uzman_id = preferences.getString("uid", "");
        String isg_uzman_adi=preferences.getString("name","");
        person_tv.setText(isg_uzman_adi);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_onay_ekrani);
        recyclerViewRaporlar=(RecyclerView)findViewById(R.id.recycler_view_onay_ekrani_raporlar);

        eslesmeBilgiGetir(Integer.parseInt(isg_uzman_id));

        //isg uzmanıyla eşleşmiş ve eşleşmek isteyen kişileri listeleme
        onayEkraniAdapter = new UzmanOnayEkraniAdapter(this, eslesmelerList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(onayEkraniAdapter);

        iv_onay_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eslesmelerList.clear();
                eslesmeBilgiGetir(Integer.parseInt(isg_uzman_id));
            }
        });


        //isg uzmanına gönderilen raporları listeleme
        RecyclerView.LayoutManager lm = new LinearLayoutManager(getApplicationContext());
        raporlariGetir(Integer.parseInt(isg_uzman_id));
        raporlarAdapter=new IsgRaporlarAdapter(this,raporlarList);
        recyclerViewRaporlar.setLayoutManager(lm);
        recyclerViewRaporlar.setHasFixedSize(true);
        recyclerViewRaporlar.setAdapter(raporlarAdapter);

    }



    //isg uzmanına gönderilen istekleri listeler.
    private void eslesmeBilgiGetir(final int isg_uzmani_id) {
        String string_req = "req_eslesme_bilgi_getir";

        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.BaseURL + "json_eslesme_bilgileri.php", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("esleme_bilgileri", response);

                try {
                    JSONObject jObj = new JSONObject(response);
                    JSONArray eslesmeArray = jObj.getJSONArray("eslesme");
                    int count = 0;
                    while (count < eslesmeArray.length()) {
                        JSONObject eslesme = eslesmeArray.getJSONObject(count);
                        count++;

                        boolean error = eslesme.getBoolean("error");
                        if (!error) {
                            int eslesme_id = eslesme.getInt("eslesme_id");
                            int isg_uzmani_id = eslesme.getInt("isg_uzman_id");
                            int dep_sorumlu_id = eslesme.getInt("dep_sorumlu_id");
                            String dep_sorumlu_adsoyad = eslesme.getString("dep_sorumlu_adsoyad");
                            String dep_sorumlu_eposta = eslesme.getString("dep_sorumlu_eposta");
                            int eslesme_onay = eslesme.getInt("onay");
                            String eslesme_onay_yazisi = null;
                            if (eslesme_onay == 0)
                                eslesme_onay_yazisi = "Onay Bekliyor";
                            else if (eslesme_onay == 1)
                                eslesme_onay_yazisi = "Onaylanmış";


                            Eslesmeler eslesme_bilgileri = new Eslesmeler(eslesme_id, isg_uzmani_id, dep_sorumlu_id, dep_sorumlu_adsoyad, dep_sorumlu_eposta, eslesme_onay_yazisi);
                            eslesmelerList.add(eslesme_bilgileri);
                            onayEkraniAdapter.notifyDataSetChanged();
                        } else {
                            String errorMsg = jObj.getString("error_msg");

                            Snacky.builder()
                                    .setActivty(IsgUzmaniActivity.this)
                                    .setText(errorMsg)
                                    .setDuration(Snacky.LENGTH_LONG)
                                    .warning()
                                    .show();
                        }

                    }
                    if(eslesmeArray.length()==0){
                        tv_eslesme_yok.setVisibility(View.VISIBLE);
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
            //gönderilen parametreler
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("isg_uzmani_id", String.valueOf(isg_uzmani_id));
                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, string_req);
    }



    //isg uzmanına gönderilen raporları getirmek için kullanılan metot
    private void raporlariGetir(final int isg_uzmani_id) {
        String string_req = "req_rapor_getir";

        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.BaseURL + "isg_uzman_icin_raporlar.php", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("rapor_response", response);

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
                                    .setActivty(IsgUzmaniActivity.this)
                                    .setText(errorMsg)
                                    .setDuration(Snacky.LENGTH_LONG)
                                    .warning()
                                    .show();
                        }

                    }
                    if(raporArray.length()==0){
                        isg_onay_ekran_rapor_yok.setVisibility(View.VISIBLE);
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
                params.put("isg_uzmani_id", String.valueOf(isg_uzmani_id));
                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, string_req);
    }





}
