package com.muratoter.isg.raporla;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

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
public class RegisterFragment extends Fragment {
    private Switch aSwitch;
    private ProgressDialog pDialog;
    private Button btn_new_user_save;
    private EditText et_login_user_name,et_login_password,et_login_eposta;
    private String durum="0";
    private ViewPager viewPager;

    public RegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        aSwitch=(Switch)getActivity().findViewById(R.id.switch_isg);
        btn_new_user_save=(Button)getActivity().findViewById(R.id.btn_new_user_save);
        et_login_user_name=(EditText)getActivity().findViewById(R.id.et_login_user_name);
        et_login_password=(EditText)getActivity().findViewById(R.id.et_login_password);
        et_login_eposta=(EditText)getActivity().findViewById(R.id.et_login_eposta);
        viewPager=(ViewPager)getActivity().findViewById(R.id.container);

        pDialog = new ProgressDialog(getContext());
        pDialog.setCancelable(false);




        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    durum="1";
                }
                if(!isChecked){
                    durum="0";
                }
            }
        });

        btn_new_user_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username=et_login_user_name.getText().toString().trim();
                String password=et_login_password.getText().toString().trim();
                String eposta=et_login_eposta.getText().toString().trim();
                if(!username.isEmpty() && !password.isEmpty() && !eposta.isEmpty()){
                    register(username,eposta,password,durum);
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


    private void register(final String name, final String email,
                              final String password,final String status ) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        pDialog.setMessage("Kaydediliyor ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.BaseURL+"register.php", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {

                        Snacky.builder()
                                .setActivty(getActivity())
                                .setActionText("Giriş Yap")
                                .setActionClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        viewPager.setCurrentItem(0);
                                        et_login_eposta.setText("");
                                        et_login_password.setText("");
                                        et_login_user_name.setText("");
                                    }
                                })
                                .setText("Kayıt başarılı.")
                                .setDuration(Snacky.LENGTH_INDEFINITE)
                                .success()
                                .show();


                    } else {

                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
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
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", name);
                params.put("email", email);
                params.put("password", password);
                params.put("status",status);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
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
