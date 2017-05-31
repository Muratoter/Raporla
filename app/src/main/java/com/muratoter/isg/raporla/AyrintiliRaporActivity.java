package com.muratoter.isg.raporla;


import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AyrintiliRaporActivity extends AppCompatActivity {
    private TextView tv_detay_rapor_tarihi,tv_detay_rapor_gonderen,tv_detay_kazazede,tv_detay_olay_tarihi
            ,tv_detay_olay_yeri,tv_detay_olay_departmani,tv_detay_olay_sebebi,tv_detay_hasar_goren_yer
            ,tv_rapor_olay_aciklama,tv_rapor_olaya_tanik;
    private TextView rapor_kisi_bilgisi;
    private ProgressDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ayrintili_rapor);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_rapor);
        setSupportActionBar(toolbar);
        setTitle("Raporlar");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


        tv_detay_rapor_tarihi=(TextView)findViewById(R.id.tv_detay_rapor_tarihi);
        tv_detay_rapor_gonderen=(TextView)findViewById(R.id.tv_detay_rapor_gonderen);
        tv_detay_kazazede=(TextView)findViewById(R.id.tv_detay_kazazede);
        tv_detay_olay_tarihi=(TextView)findViewById(R.id.tv_detay_olay_tarihi);
        tv_detay_olay_yeri=(TextView)findViewById(R.id.tv_detay_olay_yeri);
        tv_detay_olay_departmani=(TextView)findViewById(R.id.tv_detay_olay_departmani);
        tv_detay_olay_sebebi=(TextView)findViewById(R.id.tv_detay_olay_sebebi);
        tv_detay_hasar_goren_yer=(TextView)findViewById(R.id.tv_detay_hasar_goren_yer);
        tv_rapor_olay_aciklama=(TextView)findViewById(R.id.tv_rapor_olay_aciklama);
        tv_rapor_olaya_tanik=(TextView)findViewById(R.id.tv_rapor_olaya_tanik);
        rapor_kisi_bilgisi=(TextView)findViewById(R.id.rapor_kisi_bilgisi);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        String activity_name=getIntent().getExtras().getString("activity_name");
        if(activity_name.equals("UretimSorumlusuActivity")){
            rapor_kisi_bilgisi.setText("Rapordan Sorumlu İSG Uzmanı");
        }


        RaporDetayGetir(String.valueOf(IsgRaporlarAdapter.rapor_id));

        //bundle çevir


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }


    //jsonarraya gerek kalmadan bir kod yazmalısın.
    private void RaporDetayGetir(final String rapor_id){
        String string_req="req_rapordetay_getir";
        pDialog.setMessage("Rapor Yükleniyor ...");
        showDialog();
        StringRequest stringRequest=new StringRequest(Request.Method.POST, AppConfig.BaseURL + "json_raporlar.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                hideDialog();
                Log.d("raporlar",response);

                try {
                    JSONObject jObj = new JSONObject(response);
                    JSONArray raporArray = jObj.getJSONArray("raporlar");
                    JSONObject rapor=raporArray.getJSONObject(0);

                    boolean error=rapor.getBoolean("error");
                    if(!error){
                        tv_detay_rapor_tarihi.setText(rapor.getString("rapor_tarihi"));
                        tv_detay_kazazede.setText(rapor.getString("kisi_adsoyad"));
                        tv_detay_olay_tarihi.setText(rapor.getString("olay_tarihi"));
                        tv_detay_olay_yeri.setText(rapor.getString("olay_oldugu_yer"));
                        tv_detay_olay_departmani.setText(rapor.getString("olay_oldugu_departman"));
                        tv_detay_olay_sebebi.setText(rapor.getString("olay_sebebi"));
                        tv_detay_hasar_goren_yer.setText(rapor.getString("hasar_goren_yer"));
                        tv_rapor_olay_aciklama.setText(rapor.getString("olay_kisa_aciklama"));
                        tv_rapor_olaya_tanik.setText(rapor.getString("olaya_sahit_kisi_adsoyad"));
                        if(getIntent().getExtras().getString("activity_name").equals("UretimSorumlusuActivity")){
                            tv_detay_rapor_gonderen.setText(rapor.getString("isg_uzman_adsoyad"));
                        }
                        else {
                            tv_detay_rapor_gonderen.setText(rapor.getString("dep_sorumlu_adsoyad"));
                        }

                    }
                    else {
                        Toast.makeText(AyrintiliRaporActivity.this, "Rapor getirilemiyor.", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(AyrintiliRaporActivity.this, "Json Error:"+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }){
            @Override
            protected Map<String, String> getParams(){
                Map<String,String> params=new HashMap<>();
                params.put("rapor_id",rapor_id);
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest,string_req);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
