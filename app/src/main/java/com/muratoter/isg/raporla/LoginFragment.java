package com.muratoter.isg.raporla;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.mateware.snacky.Snacky;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {
    private Switch aSwitch;
    private Button btn_eposta_log;
    private EditText et_login_eposta2,et_login_password2;
    private ProgressDialog pDialog;
    private SessionManager session;
    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        aSwitch=(Switch)getActivity().findViewById(R.id.switch_hatirla);
        btn_eposta_log=(Button)getActivity().findViewById(R.id.btn_eposta_log);
        et_login_password2=(EditText)getActivity().findViewById(R.id.et_login_password2);
        et_login_eposta2=(EditText)getActivity().findViewById(R.id.et_login_eposta2);
        session = new SessionManager(getContext());
        pDialog = new ProgressDialog(getContext());
        pDialog.setCancelable(false);

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    session.setLogin(true);
                }
                if(!isChecked){
                    session.setLogin(false);
                }
            }
        });

        btn_eposta_log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email=et_login_eposta2.getText().toString().trim();
                String password=et_login_password2.getText().toString().trim();
                if(!email.isEmpty() && !password.isEmpty()){
                    login(email,password);
                }
                else{
                    Snackbar warningSnackbar= Snacky.builder()
                            .setActivty(getActivity())
                            .setText("Boş alanları doldurunuz.")
                            .setDuration(Snacky.LENGTH_LONG)
                            .warning();
                    warningSnackbar.show();
                }
            }
        });


    }



    private void login(final String eposta,final String sifre){
        String req = "req_login";
        pDialog.setMessage("Giriş yapılıyor ...");
        showDialog();

        StringRequest stringRequest=new StringRequest(Request.Method.POST, AppConfig.BaseURL + "login.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                hideDialog();
                Log.d("logresponse",response);
                try{
                    JSONObject JO=new JSONObject(response);
                    boolean error=JO.getBoolean("error");
                    if(!error){
                        SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(getContext());
                        SharedPreferences.Editor editor=preferences.edit();

                        JSONObject user = JO.getJSONObject("user");
                        String status=user.getString("durum");
                        String name=user.getString("name");
                        String email=user.getString("email");
                        String uid=JO.getString("id");

                        editor.putString("name",name);
                        editor.putString("email",email);
                        editor.putString("uid",uid);


                        if(Integer.parseInt(status)==1){
                            Intent isgActivity=new Intent(getContext(),IsgUzmaniActivity.class);
                            startActivity(isgActivity);
                            editor.putString("activity_name","isg_uzmani_activity");
                            getActivity().finish();
                        }
                        else if(Integer.parseInt(status)==0){
                            Intent usActivity=new Intent(getContext(),UretimSorumlusuActivity.class);
                            startActivity(usActivity);
                            editor.putString("activity_name","uretim_sorumlu_activity");
                            getActivity().finish();
                        }
                        editor.commit();


                    }else{
                        String error_mes=JO.getString("error_msg");
                        Toast.makeText(getContext(), ""+error_mes, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email",eposta);
                params.put("password",sifre);
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest,req);
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
