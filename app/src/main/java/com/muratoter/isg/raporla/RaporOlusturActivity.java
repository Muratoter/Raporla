package com.muratoter.isg.raporla;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.mateware.snacky.Snacky;

public class RaporOlusturActivity extends AppCompatActivity {
    private EditText et_kazazede_adsoyad,et_olay_departman_adi,et_hasar_goren_organ,et_olay_aciklama,et_tanik_adsoyad;
    private DatePicker dp_olay_tarih;
    private TimePicker tp_olay_saat;
    private RadioGroup radioGroup_olay_yeri,radioGroup_olay_sebebi;
    private RadioButton rb_olay_isyerinde,rb_olay_isyeri_disinda,rb_sebep1,rb_sebep2;
    private Button btn_rapor_temizle,btn_rapor_gonder;

    private String olay_yeri_text,olay_olus_sebebi_text,olay_tarih;
    private int dep_sorumlu_id,isg_uzman_id;

    private String current_date;
    private LinearLayout ll_rapor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rapor_olustur);

        et_kazazede_adsoyad=(EditText)findViewById(R.id.kazazede_adsoyad);
        et_olay_departman_adi=(EditText)findViewById(R.id.olay_departman_adi);
        et_hasar_goren_organ=(EditText)findViewById(R.id.et_hasar_goren_organ);
        et_olay_aciklama=(EditText)findViewById(R.id.et_olay_aciklama);
        et_tanik_adsoyad=(EditText)findViewById(R.id.et_tanik_adsoyad);

        dp_olay_tarih=(DatePicker)findViewById(R.id.dp_olay_tarih);
        tp_olay_saat=(TimePicker)findViewById(R.id.tp_olay_saat);

        radioGroup_olay_yeri=(RadioGroup) findViewById(R.id.radioGroup_olay_yeri);
        radioGroup_olay_sebebi=(RadioGroup)findViewById(R.id.radioGroup_olay_sebebi);

        rb_olay_isyerinde=(RadioButton)findViewById(R.id.rb_olay_isyerinde);
        rb_olay_isyeri_disinda=(RadioButton)findViewById(R.id.rb_olay_isyeri_disinda);
        rb_sebep1=(RadioButton)findViewById(R.id.rb_sebep1);
        rb_sebep2=(RadioButton)findViewById(R.id.rb_sebep2);

        btn_rapor_temizle=(Button)findViewById(R.id.btn_rapor_temizle);
        btn_rapor_gonder=(Button)findViewById(R.id.btn_rapor_gonder);

        ll_rapor=(LinearLayout)findViewById(R.id.rapor_ll);


        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        dep_sorumlu_id=Integer.parseInt(preferences.getString("uid", ""));
        isg_uzman_id=UretimSorumlusuActivity.isg_uzman_id;


        //şimdiki zamanı alma
        final DateFormat dateFormat=new SimpleDateFormat("yyyy-M-dd");
        current_date=dateFormat.format(Calendar.getInstance().getTime());



        radioGroup_olay_yeri.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                RadioButton checkRadioButton=(RadioButton)radioGroup.findViewById(checkedId);
                boolean ischecked=checkRadioButton.isChecked();
                if(ischecked){
                    olay_yeri_text=checkRadioButton.getText().toString();

                }
            }
        });

        radioGroup_olay_sebebi.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton chckRadioButton=(RadioButton)radioGroup.findViewById(i);
                boolean ischecked=chckRadioButton.isChecked();
                if(ischecked){
                    olay_olus_sebebi_text=chckRadioButton.getText().toString();

                }
            }
        });

        btn_rapor_gonder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //olay tarihi ve saati alma
                DateFormat olayTarihFormat=new SimpleDateFormat("yyyy-M-dd HH:mm");
                int year=dp_olay_tarih.getYear();
                int month=dp_olay_tarih.getMonth();
                int day=dp_olay_tarih.getDayOfMonth();
                int hour=tp_olay_saat.getCurrentHour();
                int minute=tp_olay_saat.getCurrentMinute();
                Calendar calendar=Calendar.getInstance();
                calendar.set(year,month,day,hour,minute);
                olay_tarih=olayTarihFormat.format(calendar.getTime());


                raporEkle(dep_sorumlu_id,current_date,et_kazazede_adsoyad.getText().toString(),olay_tarih,olay_yeri_text,et_olay_departman_adi.getText().toString(),olay_olus_sebebi_text
                ,et_hasar_goren_organ.getText().toString(),et_olay_aciklama.getText().toString(),et_tanik_adsoyad.getText().toString(),isg_uzman_id);


            }
        });


    }




    private void raporEkle(final int dep_sorumlu_id, final String rapor_tarihi,final String kisi_adsoyad,final String olay_tarihi, final String olay_oldugu_yer,
                           final String olay_oldugu_departman,final String olay_sebebi, final String hasar_goren_yer,final String olay_kisa_aciklama, final String olaya_sahit_kisi_adsoyad, final int isg_uzman_id) {
        String string_req = "req_rapor_ekle";

        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.BaseURL + "rapor_ekle.php", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject JO=new JSONObject(response);
                    boolean error=JO.getBoolean("error");
                    if(!error){

                        Snacky.builder()
                                .setActivty(RaporOlusturActivity.this)
                                .setActionText("Kapat")
                                .setActionClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        finish();
                                    }
                                })
                                .setText("Rapor başarıyla oluşturulmuştur.")
                                .setDuration(Snacky.LENGTH_INDEFINITE)
                                .success()
                                .show();
                    }
                    else{
                        String error_mes=JO.getString("error_msg");
                        Toast.makeText(getApplicationContext(), ""+error_mes, Toast.LENGTH_SHORT).show();
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
                params.put("rapor_tarihi",String.valueOf(rapor_tarihi));
                params.put("kisi_adsoyad",String.valueOf(kisi_adsoyad));
                params.put("olay_tarihi",String.valueOf(olay_tarihi));
                params.put("olay_oldugu_yer",String.valueOf(olay_oldugu_yer));
                params.put("olay_oldugu_departman",String.valueOf(olay_oldugu_departman));
                params.put("olay_sebebi",String.valueOf(olay_sebebi));
                params.put("hasar_goren_yer",String.valueOf(hasar_goren_yer));
                params.put("olay_kisa_aciklama",String.valueOf(olay_kisa_aciklama));
                params.put("olaya_sahit_kisi_adsoyad",String.valueOf(olaya_sahit_kisi_adsoyad));
                params.put("isg_uzman_id",String.valueOf(isg_uzman_id));
                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, string_req);
    }

}
