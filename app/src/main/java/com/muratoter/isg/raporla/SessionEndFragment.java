package com.muratoter.isg.raporla;


import android.app.Application;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import de.mateware.snacky.Snacky;


/**
 * A simple {@link Fragment} subclass.
 */
public class SessionEndFragment extends DialogFragment {
    private ProgressDialog progressDialog;
    private SessionManager session;
    public SessionEndFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_session_end,container,false);
        getDialog().setTitle("Çıkış");
        return view;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        final View dialogView = inflater.inflate(R.layout.fragment_session_end, null);
        builder.setView(dialogView);
        final Button btn_session_end;
        session=new SessionManager(getContext());
        progressDialog=new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        btn_session_end=(Button)dialogView.findViewById(R.id.btn_session_end);
        btn_session_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                session.setLogin(false);
                progressDialog.setMessage("Çıkış yapılıyor...");
                progressDialog.show();
                setTimerProgress(2000,progressDialog);

            }
        });


        return builder.create();
    }
    private void setTimerProgress(long time,final ProgressDialog pd){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                pd.dismiss();
                System.exit(0);
            }
        },time);
    }
}
